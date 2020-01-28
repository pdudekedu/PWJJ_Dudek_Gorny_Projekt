using WPFTools;
using WPFTools.Communication.ServiceClients;
using WPFTools.Communication.Services;
using WPFTools.Enums;
using WPFTools.Languages;
using WPFTools.Models;
using WPFTools.ViewModels;
using WPFTools.ViewModels.Docking;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using WorkManager.Clients;
using WorkManager.Data.Models;

namespace WorkManager.ViewModels
{
    public abstract class MetadataViewModel<TItem, TEditItem> : AsynchonizableViewModel
        where TItem : EFModel<int>, IExcludableModel
        where TEditItem : EFExcludableModel<int>, new()
    {
        public MetadataViewModel()
        {
            ItemsLoading = BackgroundTaskDescriptor<MetadataViewModel<TItem, TEditItem>, IEnumerable<TItem>>
                .RegisterDescriptor(this, GetLoadingItemsText(), GetItems)
                .OnCompleted((vm, res) => { vm.Items = new ObservableCollection<TItem>(res); });

            EditItemLoading = BackgroundTaskDescriptor<MetadataViewModel<TItem, TEditItem>, TEditItem>
                .RegisterDescriptor(this, GetLoadingEditItemText(), GetEditItem, Await)
                .OnCompleted((vm, res) =>
                {
                    vm.EditItem = null;
                    vm.EditItem = res;
                });

            CreateItemLoading = BackgroundTaskDescriptor<MetadataViewModel<TItem, TEditItem>, TEditItem>
                .RegisterDescriptor(this, GetLoadingEditItemText(), CreateNewItem, Await)
                .OnCompleted((vm, res) =>
                {
                    vm.EditItem = null;
                    vm.EditItem = res;
                });

            ItemsLoading.Invoke();
        }
        #region Properties
        /// <summary>
        /// Przechowuje elementy do listowania.
        /// </summary>
        public BackgroundTaskDescriptor<MetadataViewModel<TItem, TEditItem>, IEnumerable<TItem>> ItemsLoading { get; }
        public BackgroundTaskDescriptor<MetadataViewModel<TItem, TEditItem>, TEditItem> EditItemLoading { get; }
        public BackgroundTaskDescriptor<MetadataViewModel<TItem, TEditItem>, TEditItem> CreateItemLoading { get; }
        /// <summary>
        /// 
        /// </summary>
        public TrackingGroup TrackingGroup { get; } = TrackingGroup.GetGroup();
        /// <summary>
        /// Kolekcja elementów.
        /// </summary>
        public ObservableCollection<TItem> Items
        {
            get { return _Items; }
            set
            {
                _Items = value;
                if (EditItemId != 0)
                    EditItemLoading.Invoke();
                else
                    EditItem = null;
                OnPropertyChanged();
            }
        }
        private ObservableCollection<TItem> _Items;
        /// <summary>
        /// Comment
        /// </summary>
        public int EditItemId
        {
            get { return _EditItemId; }
            set
            {
                _EditItemId = value;
                EditItemLoading.Invoke();
                OnPropertyChanged();
            }
        }
        private int _EditItemId;
        /// <summary>
        /// Przechowuje edytowany element.
        /// </summary>
        public TEditItem EditItem
        {
            get { return _EditItem; }
            set
            {
                if (_EditItem != null)
                    TrackingGroup.UnregisterObject(_EditItem);
                _EditItem = value;
                if (_EditItem != null)
                    TrackingGroup.RegisterObject(_EditItem);
                OnPropertyChanged();
            }
        }
        private TEditItem _EditItem;
        /// <summary>
        /// Filtr wyświetlania elementów listy.
        /// </summary>
        public bool? InUseFilter
        {
            get { return _InUseFilter; }
            set
            {
                _InUseFilter = value;
                ItemsLoading.Invoke();
                OnPropertyChanged();
            }
        }
        private bool? _InUseFilter = true;
        #endregion
        #region Methods
        protected abstract string GetLoadingItemsText();
        protected abstract string GetLoadingEditItemText();
        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        protected abstract IEnumerable<TItem> GetItems();
        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        protected abstract TEditItem GetEditItem();
        /// <summary>
        /// 
        /// </summary>
        public abstract TEditItem CreateNewItem();
        public abstract bool CanRemoveItem(TItem item);
        public abstract bool RemoveItem(TItem item);
        public virtual BackgroundTaskDescriptor[] Await() => new BackgroundTaskDescriptor[0];
        protected virtual bool SaveEditItem()
        {
            using (var saving = new SavingServiceClient())
            {
                try
                {
                    PrepareItemForSave();
                    if (EditItem.Id == 0)
                        saving.CascadeInsert(EditItem);
                    else
                    {
                        foreach (var item in TrackingGroup.GetTrackingResult<EFHistoryModel<int>>(x => x.HasFlag(TrackingState.Added)))
                        {
                            if (item.Id == 0)
                                saving.CascadeInsert(item);
                            else
                                saving.Modify(item);
                        }
                        foreach (var item in TrackingGroup.GetTrackingResult<EFHistoryModel<int>>(x => x.HasFlag(TrackingState.Deleted)))
                            if (item is EFExcludableModel<int> excludable)
                                saving.Delete(excludable);
                            else
                                saving.Remove(item);
                        foreach (var item in TrackingGroup.GetTrackingResult<EFHistoryModel<int>>(x => x.HasFlag(TrackingState.Changed) && !x.HasFlag(TrackingState.Added)))
                            saving.Modify(item);
                    }

                    saving.Commit();
                    return true;
                }
                catch (Exception ex)
                {
                    saving.Rollback();
                    WPFTools.Log.LogException(ex);
                    return false;
                }
            }
        }
        protected virtual void PrepareItemForSave() { }
        #endregion
        #region Commands
        /// <summary>
        /// Obsługuje akcję anulowania wprowadzonych zmian.
        /// </summary>
        public WPFTools.RelayCommand CancelCommand
        {
            get => new WPFTools.RelayCommand(() =>
            {
                if (EditItem.Id == 0)
                    EditItem = CreateNewItem();
                else
                    EditItemLoading.Invoke();
            }, () => TrackingGroup.HasChanges);
        }
        /// <summary>
        /// Obsługuje akcję zapisania zmian.
        /// </summary>
        public WPFTools.RelayCommand SaveCommand
        {
            get
            {
                return new WPFTools.RelayCommand(() =>
                {
                    if (EditItem == null || EditItem.TrackingState == TrackingState.None)
                        return;

                    if (SaveEditItem())
                    {
                        ItemsLoading.Invoke();
                        if (EditItem.Id != 0)
                            EditItemLoading.Invoke();
                        TrackingGroup.Reload();
                    }
                    else
                        MessageBox.Show("Wystąpił błąd podczas próby zapisania zmian.", App.CurrentApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                },
                () => TrackingGroup.HasChanges && TrackingGroup.IsValid);
            }
        }
        /// <summary>
        /// Obsługuje akcję trwałego usunięcia obiketu.
        /// </summary>
        public WPFTools.RelayCommand<TItem> RemoveItemCommand
        {
            get
            {
                return new WPFTools.RelayCommand<TItem>((item) =>
                {
                    if (item == null || MessageBox.Show("Czy na pewno chcesz usunąć obiekt?", App.CurrentApp.ProgramTitle, MessageBoxButton.YesNo, MessageBoxImage.Question) != MessageBoxResult.Yes)
                        return;
                    if (!CanRemoveItem(item))
                        MessageBox.Show("Obiekt nie może być trwale usunięty, ponieważ jest wykorzystywany przez inne obiekty systemu.", App.CurrentApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Information);
                    else
                    {
                        if (RemoveItem(item))
                            ItemsLoading.Invoke();
                    }
                });
            }
        }
        /// <summary>
        /// Obsługuje akcję edycji obiektu.
        /// </summary>
        public WPFTools.RelayCommand<TItem> EditItemCommand
        {
            get
            {
                return new WPFTools.RelayCommand<TItem>((item) =>
                {
                    if (item == null)
                        return;
                    EditItemId = item.Id;
                });
            }
        }
        /// <summary>
        /// Obsługuje akcję stworzenia nowego obiektu.
        /// </summary>
        public WPFTools.RelayCommand NewItemCommand
        {
            get
            {
                return new WPFTools.RelayCommand(() =>
                {
                    CreateItemLoading.Invoke();
                });
            }
        }
        #endregion
    }
}
