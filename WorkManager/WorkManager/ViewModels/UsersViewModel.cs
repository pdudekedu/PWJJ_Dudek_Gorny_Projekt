using WPFTools;
using WPFTools.BusinessLogic;
using WPFTools.Communication.ServiceClients;
using WPFTools.Communication.Services;
using WPFTools.Enums;
using WPFTools.Extensions;
using WPFTools.Models;
using WPFTools.ViewModels;
using WPFTools.ViewModels.Docking;
using WPFTools.Views.Windows;
using WPFTools.Windows;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using WorkManager.Clients;
using WorkManager.Models;

namespace WorkManager.ViewModels
{
    /// <summary>
    /// Klasa reprezentująca VM gatunków.
    /// </summary>
    public class UsersViewModel : AsynchonizableViewModel
    {
        public UsersViewModel()
        {
            UsersLoading = BackgroundTaskDescriptor<UsersViewModel, IEnumerable<EFAccountView>>.RegisterDescriptor(this, "Ładowanie listy użytkowników", GetUsers)
                .OnCompleted((vm, res) => vm.Users = new ObservableCollection<EFAccountView>(res));
            UserLoading = BackgroundTaskDescriptor<UsersViewModel, EFAccountEdit>.RegisterDescriptor(this, "Ładowanie użytkownika", GetUser)
                .OnCompleted((vm, res) => vm.EditingUser = res);
            UsersLoading.Invoke();
        }
        #region Properties
        public BackgroundTaskDescriptor<UsersViewModel, IEnumerable<EFAccountView>> UsersLoading { get; }
        public BackgroundTaskDescriptor<UsersViewModel, EFAccountEdit> UserLoading { get; }
        public TrackingGroup TrackingGroup { get; } = TrackingGroup.GetGroup();
        public ObservableCollection<EFAccountView> Users
        {
            get { return _Users; }
            set
            {
                _Users = value;
                OnPropertyChanged();
            }
        }
        private ObservableCollection<EFAccountView> _Users;
        public EFAccountView SelectedUser
        {
            get { return _SelectedUser; }
            set
            {
                _SelectedUser = value;
                UserLoading.Invoke();
                OnPropertyChanged();
            }
        }
        protected EFAccountView _SelectedUser;
        public EFAccountEdit EditingUser
        {
            get { return _EditingUser; }
            set
            {
                ResetPassword = true;
                if (_EditingUser != null)
                    TrackingGroup.UnregisterObject(_EditingUser.EFAccount);
                _EditingUser = value;
                if (_EditingUser != null)
                {
                    TrackingGroup.RegisterObject(_EditingUser.EFAccount);
                    _SelectedUserType = UserType.GetUserType(_EditingUser.EFAccount.Permissions);
                    OnPropertyChanged(nameof(SelectedUserType));
                }
                OnPropertyChanged();
            }
        }
        private EFAccountEdit _EditingUser;
        public bool? InUseFilter
        {
            get { return _InUseFilter; }
            set
            {
                _InUseFilter = value;
                UsersLoading.Invoke();
                OnPropertyChanged();
            }
        }
        private bool? _InUseFilter = true;
        /// <summary>
        /// Wartość resetująca PasswordBoxy.
        /// </summary>
        public bool ResetPassword
        {
            get { return _ResetPassword; }
            set
            {
                _ResetPassword = value;
                OnPropertyChanged();
            }
        }
        private bool _ResetPassword = false;
        /// <summary>
        /// Wybrany typ użytkownika.
        /// </summary>
        public UserType SelectedUserType
        {
            get { return _SelectedUserType; }
            set
            {
                _SelectedUserType = value;
                if(_SelectedUserType?.Permissions != null && EditingUser != null)
                    EditingUser.UserPermissions = UserPermissionEdit.GetPermissionEdits(BaseApplication.CurrentBaseApp.UserPermissionAttributes, _SelectedUserType.Permissions);
                OnPropertyChanged();
            }
        }
        private UserType _SelectedUserType = UserType.UserTypes.ElementAt(1);
        #endregion
        #region Methods
        public IEnumerable<EFAccountView> GetUsers()
        {
            using (IMetadataUsers client = App.CurrentBaseApp.GetUsersContext())
                return client.GetUsers(InUseFilter).Select(x => new EFAccountView() { Name = x.Name, Surname = x.Surname, UserName = x.UserName, InUse = x.InUse, Id = x.Id, Type = x.Type }).ToList();
        }
        public EFAccountEdit GetUser()
        {
            if (SelectedUser == null)
                return null;
            using (IMetadataUsers client = App.CurrentBaseApp.GetUsersContext())
            {
                var user = client.GetUser(SelectedUser.Id);
                if (user != null)
                {
                    var account = new EFAccountEdit();
                    account.EFAccount = user;
                    if (user.Permissions == null)
                        user.Permissions = ByteExtensions.GetDefaultPermissions(BaseApplication.CurrentBaseApp.UserPermissionAttributes, Permission.Read);
                    account.UserPermissions = UserPermissionEdit.GetPermissionEdits(BaseApplication.CurrentBaseApp.UserPermissionAttributes, user.Permissions);
                    return account;
                }
                return null;
            }
        }
        #endregion
        #region Commands

        /// <summary>
        /// Obsługa przycisku edycji elementu.
        /// </summary>
        public WPFTools.RelayCommand<EFAccountView> EditUserCommand
        {
            get
            {
                return new WPFTools.RelayCommand<EFAccountView>((user) =>
                {
                    try
                    {
                        using (new Busy())
                            SelectedUser = user;
                    }
                    catch (Exception exc)
                    {
                        MessageBox.Show("Wystąpił błąd podczas próby pobrania użytkownika do edycji.", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                        Log.LogException(exc);
                    }
                });
            }
        }
        /// <summary>
        /// Obsługa zmiany hasła użytkownika.
        /// </summary>
        public WPFTools.RelayCommand<EFAccountView> ChangePasswordCommand
        {
            get
            {
                return new WPFTools.RelayCommand<EFAccountView>((user) =>
                {
                    try
                    {
                        new ChangePasswordWindow(user.Id).ShowDialog();
                    }
                    catch (Exception exc)
                    {
                        MessageBox.Show("Wystąpił błąd podczas próby zmiany hasła użytkownika.", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                        Log.LogException(exc);
                    }
                });
            }
        }
        /// <summary>
        /// Obsługa przycisku anulowania zmian lub tworzenia nowego elementu.
        /// </summary>
        public WPFTools.RelayCommand CancelCommand
        {
            get
            {
                return new WPFTools.RelayCommand(() =>
                {
                    try
                    {
                        using (new Busy())
                        {
                            TrackingGroup.Reset();
                            if (EditingUser.EFAccount.TrackingState.HasFlag(TrackingState.Added))
                            {
                                EditingUser = null;
                            }
                            UserLoading.Invoke();
                        }
                    }
                    catch (Exception exc)
                    {
                        MessageBox.Show("Wystąpił błąd podczas próby anulowania zmian.", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                        Log.LogException(exc);
                    }
                },
                () => TrackingGroup.HasChanges);
            }
        }
        /// <summary>
        /// Obsługa zapisu zmian.
        /// </summary>
        public WPFTools.RelayCommand SaveCommand
        {

            get
            {
                return new WPFTools.RelayCommand(() =>
                {
                    using (var client = new SavingServiceClient())
                    {
                        if (EditingUser == null || EditingUser.EFAccount.TrackingState == TrackingState.None)
                            return;
                        try
                        {
                            using (new Busy())
                            {

                                EditingUser.EFAccount.Name = EditingUser.EFAccount.Name.Trim();
                                EditingUser.EFAccount.Surname = EditingUser.EFAccount.Surname.Trim();
                                if (EditingUser.EFAccount.Title != null)
                                    EditingUser.EFAccount.Title = EditingUser.EFAccount.Title.Trim();
                                EditingUser.EFAccount.UserName = EditingUser.EFAccount.UserName.Trim();
                                if (EditingUser.EFAccount.TrackingState.HasFlag(TrackingState.Added))
                                {
                                    if (EditingUser.EFAccount.Password != EditingUser.EFAccount.PasswordConfirm)
                                    {
                                        MessageBox.Show($"Podane hasła nie są takie same!", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Information);
                                        return;
                                    }
                                    //else if (client.CheckIfUserExists(EditingUser.EFAccount))
                                    //{
                                    //    MessageBox.Show($"Użytkownik o takiej nazwie już istnieje!", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Information);
                                    //    return;
                                    //}
                                    if (EditingUser.EFAccount.Type == EFAccountType.Domain)
                                        EditingUser.EFAccount.Password = null;
                                    EditingUser.EFAccount.Id = client.Insert(EditingUser.EFAccount);
                                }
                                else
                                {
                                    //if (client.CheckIfUserExists(EditingUser.EFAccount))
                                    //{
                                    //    MessageBox.Show($"Użytkownik o takiej nazwie już istnieje!", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Information);
                                    //    return;
                                    //}
                                    client.Modify(EditingUser.EFAccount);
                                }
                                client.Commit();
                                TrackingGroup.Reload();
                                UsersLoading.Invoke();
                            }
                        }
                        catch (Exception exc)
                        {
                            client.Rollback();
                            MessageBox.Show("Wystąpił błąd podczas próby zapisania zmian.", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                            Log.LogException(exc);
                        }
                    }
                },
                () => TrackingGroup.HasChanges && TrackingGroup.IsValid);
            }
        }
        /// <summary>
        /// Obsługa przycisku tworzenia nowego elementu.
        /// </summary>
        public WPFTools.RelayCommand NewUserCommand
        {
            get
            {
                return new WPFTools.RelayCommand(() =>
                {
                    try
                    {
                        using (new Busy())
                        {
                            _SelectedUser = null;
                            using (IMetadataUsers client = BaseApplication.CurrentBaseApp.GetUsersContext())
                            {
                                var account = new EFAccountEdit();
                                var user = client.CreateUser();
                                account.EFAccount = user;
                                if (user.Permissions == null)
                                    user.Permissions = ByteExtensions.GetDefaultPermissions(BaseApplication.CurrentBaseApp.UserPermissionAttributes, Permission.Read);
                                account.UserPermissions = UserPermissionEdit.GetPermissionEdits(BaseApplication.CurrentBaseApp.UserPermissionAttributes, user.Permissions);
                                EditingUser = account;
                            }
                        }
                    }
                    catch (Exception exc)
                    {
                        MessageBox.Show("Wystąpił błąd podczas próby stworzenia nowego użytkownika.", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                        Log.LogException(exc);
                    }
                });
            }
        }
        /// <summary>
        /// Obsługa usuwanie elementu.
        /// </summary>
        public WPFTools.RelayCommand<EFAccountView> RemoveUserCommand
        {
            get
            {
                return new WPFTools.RelayCommand<EFAccountView>((user) =>
                {
                    try
                    {
                        using (new Busy())
                        {
                            SelectedUser = user;

                            if (user == null)
                                return;

                            using (var client = new MainServiceClient())
                            {
                                if (MessageBox.Show($"Czy na pewno chcesz trwale usunąć użytkownika {user.UserName}?", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.YesNo, MessageBoxImage.Information) != MessageBoxResult.Yes)
                                    return;
                                if (!client.CanRemoveUser(user.Id))
                                    MessageBox.Show("Konto użytkownika nie może być trwale usunięte, ponieważ jest wykorzystywane przez inne obiekty systemu.", App.CurrentApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Information);
                                else
                                {
                                    client.RemoveUser(user.Id);
                                    Users.Remove(user);
                                }
                            }
                            UsersLoading.Invoke();
                            UserLoading.Invoke();
                        }
                    }
                    catch (Exception exc)
                    {
                        MessageBox.Show("Wystąpił błąd podczas próby usunięcia użytkownika.", BaseApplication.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                        Log.LogException(exc);
                    }
                });
            }
        }
        #endregion
    }
}
