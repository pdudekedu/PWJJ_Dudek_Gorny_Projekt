using WPFTools.Enums;
using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using WorkManager.Clients;
using WorkManager.Data.Models;

namespace WorkManager.ViewModels
{
    public class NewTaskWindowViewModel : INotifyPropertyChanged
    {
        public NewTaskWindowViewModel(int projectId)
        {
            Task = new Task() { ProjectId = projectId, State = Data.Enums.TaskState.New };
            TrackingGroup.RegisterObject(Task);
            using(var client = new MainServiceClient())
                Accounts = client.GetAccounts(projectId);
        }
        public NewTaskWindowViewModel(Task task)
        {
            Task = task;
            TrackingGroup.RegisterObject(Task);
            using (var client = new MainServiceClient())
                Accounts = client.GetAccounts(task.ProjectId);
        }
        public Action Close { get; set; }
        public Task Task { get; }
        public TrackingGroup TrackingGroup { get; } = TrackingGroup.GetGroup();
        #region Properties
        /// <summary>
        /// Comment
        /// </summary>
        public IEnumerable<AssignableModel> Accounts
        {
            get { return _Accounts; }
            set
            {
                _Accounts = value;
                OnPropertyChanged();
            }
        }
        private IEnumerable<AssignableModel> _Accounts;

        #endregion
        #region Command
        /// <summary>
        /// Procedura zapisu
        /// </summary>
        public WPFTools.RelayCommand SaveCommand
        {
            get => new WPFTools.RelayCommand(() =>
            {
                using (SavingServiceClient saving = new SavingServiceClient())
                {
                    if (Task.Id == 0)
                        saving.CascadeInsert(Task);
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
                }
                Close?.Invoke();
            }, () => TrackingGroup.HasChanges && TrackingGroup.IsValid);
        }
        /// <summary>
        /// Procedura anulowania zmian
        /// </summary>
        public WPFTools.RelayCommand CancelCommand
        {
            get => new WPFTools.RelayCommand(() =>
            {
                TrackingGroup.Reset();
            }, () => TrackingGroup.HasChanges);
        }
        /// <summary>
        /// Obsługa akcji dodania konta do zespołu.
        /// </summary>
        public WPFTools.RelayCommand<AssignableModel> AddResourceCommand
        {
            get
            {
                return new WPFTools.RelayCommand<AssignableModel>((resource) =>
                {
                    if (Task == null || resource == null)
                        return;
                    Task.ResourceForTask.Add(new TaskResource()
                    {
                        ResourceId = resource.Id,
                        Task = Task,
                        Resource = resource,
                        TrackingState = WPFTools.Enums.TrackingState.Added
                    });
                    Task.AssignedResources.Add(resource);
                    Task.AvailableResources.Remove(resource);
                });
            }
        }
        /// <summary>
        /// Obsługa akcji usunięcia konta z zespołu.
        /// </summary>
        public WPFTools.RelayCommand<TaskResource> RemoveResourceCommand
        {
            get
            {
                return new WPFTools.RelayCommand<TaskResource>((resourceForTask) =>
                {
                    if (Task == null || resourceForTask == null)
                        return;
                    if (resourceForTask.TrackingState.HasFlag(TrackingState.Deleted))
                        resourceForTask.TrackingState ^= TrackingState.Deleted;
                    else
                    {
                        if (resourceForTask.TrackingState.HasFlag(TrackingState.Added))
                        {
                            Task.ResourceForTask.Remove(resourceForTask);
                            var resource = Task.AssignedResources.FirstOrDefault(x => x.Id == resourceForTask.ResourceId);
                            if (resource != null)
                            {
                                Task.AssignedResources.Remove(resource);
                                Task.AvailableResources.Add(resource);
                            }
                        }
                        else
                            resourceForTask.TrackingState |= TrackingState.Deleted;
                    }

                });
            }
        }
        #endregion
        #region PropertyChanged
        /// <summary>
        /// Zdarzenie obsługujące zmianę wartości właściwości (implementowane przez INotifyPropertyChanged).
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;
        /// <summary>
        /// Podnosi zdarzenie PropertyChanged dla konkretnej wałaściwości.
        /// </summary>
        /// <param name="name">Nazwa właściwości.</param>
        protected void OnPropertyChanged([CallerMemberName]string name = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
        #endregion
    }
}
