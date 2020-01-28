using WPFTools.Models;
using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using WorkManager.Clients;
using WorkManager.Data.Enums;
using WorkManager.Data.Models;
using WorkManager.Views;

namespace WorkManager.ViewModels
{
    public class TasksViewModel : AsynchonizableViewModel
    {
        public TasksViewModel()
        {
            NewTasksLoading = BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>>
                .RegisterDescriptor(this, "Ładowanie nowych zadań", () => GetTasks(Data.Enums.TaskState.New))
                .OnCompleted((vm, res) => { vm.NewTasks = new ObservableCollection<V_Task>(res); });
            ActiveTasksLoading = BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>>
                .RegisterDescriptor(this, "Ładowanie aktywnych zadań", () => GetTasks(Data.Enums.TaskState.Active))
                .OnCompleted((vm, res) => { vm.ActiveTasks = new ObservableCollection<V_Task>(res); });
            SuspendTasksLoading = BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>>
                .RegisterDescriptor(this, "Ładowanie wstrzymanych zadań", () => GetTasks(Data.Enums.TaskState.Suspend))
                .OnCompleted((vm, res) => { vm.SuspendTasks = new ObservableCollection<V_Task>(res); });
            CompleteTasksLoading = BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>>
                .RegisterDescriptor(this, "Ładowanie zakończonych zadań", () => GetTasks(Data.Enums.TaskState.Complete))
                .OnCompleted((vm, res) => { vm.CompleteTasks = new ObservableCollection<V_Task>(res); });

            ProjectLoading = BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Project>>
              .RegisterDescriptor(this, "Ładowanie projektów", GetProjects)
              .OnCompleted((vm, res) =>
              {
                  vm.Projects = res;
                  vm.ProjectId = res.FirstOrDefault().Id;
              });

            ProjectLoading.Invoke();
        }
        public BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Project>> ProjectLoading { get; }
        public BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>> NewTasksLoading { get; }
        public BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>> ActiveTasksLoading { get; }
        public BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>> SuspendTasksLoading { get; }
        public BackgroundTaskDescriptor<TasksViewModel, IEnumerable<V_Task>> CompleteTasksLoading { get; }
        #region Properites
        /// <summary>
        /// Wybrany projekt
        /// </summary>
        public int? ProjectId
        {
            get { return _ProjectId; }
            set
            {
                _ProjectId = value;
                OnPropertyChanged();
                if (value.HasValue)
                {
                    NewTasksLoading.Invoke();
                    ActiveTasksLoading.Invoke();
                    SuspendTasksLoading.Invoke();
                    CompleteTasksLoading.Invoke();
                }
            }
        }
        private int? _ProjectId;
        /// <summary>
        /// Projekty
        /// </summary>
        public IEnumerable<V_Project> Projects
        {
            get { return _Projects; }
            set
            {
                _Projects = value;
                OnPropertyChanged();
            }
        }
        private IEnumerable<V_Project> _Projects;
        /// <summary>
        /// Kolekcja nowych zadań
        /// </summary>
        public ObservableCollection<V_Task> NewTasks
        {
            get { return _NewTasks; }
            set
            {
                _NewTasks = value;
                OnPropertyChanged();
            }
        }
        private ObservableCollection<V_Task> _NewTasks;
        /// <summary>
        /// Kolekcja aktywnych zadań
        /// </summary>
        public ObservableCollection<V_Task> ActiveTasks
        {
            get { return _ActiveTasks; }
            set
            {
                _ActiveTasks = value;
                OnPropertyChanged();
            }
        }
        private ObservableCollection<V_Task> _ActiveTasks;
        /// <summary>
        /// Kolekcja wstrzymanych zadań
        /// </summary>
        public ObservableCollection<V_Task> SuspendTasks
        {
            get { return _SuspendTasks; }
            set
            {
                _SuspendTasks = value;
                OnPropertyChanged();
            }
        }
        private ObservableCollection<V_Task> _SuspendTasks;
        /// <summary>
        /// Kolekcja zakończonych zadań
        /// </summary>
        public ObservableCollection<V_Task> CompleteTasks
        {
            get { return _CompleteTasks; }
            set
            {
                _CompleteTasks = value;
                OnPropertyChanged();
            }
        }
        private ObservableCollection<V_Task> _CompleteTasks;


        #endregion
        #region Method
        public IEnumerable<V_Project> GetProjects()
        {
            using (var client = new MainServiceClient())
                return client.GetProjects(true);
        }
        public IEnumerable<V_Task> GetTasks(Data.Enums.TaskState state)
        {
            using (var client = new MainServiceClient())
                return client.GetTasks(state, ProjectId.Value);
        }
        private void ReloadTasks(TaskState state)
        {
            switch (state)
            {
                case TaskState.New:
                    NewTasksLoading.Invoke();
                    break;
                case TaskState.Active:
                    ActiveTasksLoading.Invoke();
                    break;
                case TaskState.Suspend:
                    SuspendTasksLoading.Invoke();
                    break;
                case TaskState.Complete:
                    CompleteTasksLoading.Invoke();
                    break;
            }
        }
        #endregion
        #region Command
        /// <summary>
        /// Comment
        /// </summary>
        public WPFTools.RelayCommand AddTaskCommand
        {
            get => new WPFTools.RelayCommand(() =>
            {
                var close = new NewTaskWindowView(ProjectId.Value).ShowDialog();
                NewTasksLoading.Invoke();
            });
        }
        /// <summary>
        /// Comment
        /// </summary>
        public WPFTools.RelayCommand<Tuple<V_Task, TaskState>> ChangeStateCommand
        {
            get => new WPFTools.RelayCommand<Tuple<V_Task, TaskState>>(taskInfo =>
            {
                if (taskInfo == null)
                    return;
                using (var client = new SavingServiceClient())
                {
                    var prev = taskInfo.Item1.State;
                    taskInfo.Item1.State = taskInfo.Item2;
                    switch (prev)
                    {
                        case TaskState.New:
                            NewTasks.Remove(taskInfo.Item1);
                            break;
                        case TaskState.Active:
                            ActiveTasks.Remove(taskInfo.Item1);
                            break;
                        case TaskState.Suspend:
                            SuspendTasks.Remove(taskInfo.Item1);
                            break;
                        case TaskState.Complete:
                            CompleteTasks.Remove(taskInfo.Item1);
                            break;
                    }
                    switch (taskInfo.Item2)
                    {
                        case Data.Enums.TaskState.New:
                            NewTasks.Add(taskInfo.Item1);
                            break;
                        case Data.Enums.TaskState.Active:
                            ActiveTasks.Add(taskInfo.Item1);
                            client.Insert(new TaskTime() { Time = DateTime.Now, TaskId = taskInfo.Item1.Id, Type = prev == TaskState.New ? TimeType.Start : TimeType.Resume });
                            break;
                        case Data.Enums.TaskState.Suspend:
                            SuspendTasks.Add(taskInfo.Item1);
                            client.Insert(new TaskTime() { Time = DateTime.Now, TaskId = taskInfo.Item1.Id, Type = TimeType.Suspend });
                            break;
                        case Data.Enums.TaskState.Complete:
                            CompleteTasks.Add(taskInfo.Item1);
                            client.Insert(new TaskTime() { Time = DateTime.Now, TaskId = taskInfo.Item1.Id, Type = TimeType.End });
                            break;
                    }
                    client.Commit();
                    using (var mainServiceClient = new MainServiceClient())
                        mainServiceClient.UpdateTaskState(taskInfo.Item1.Id, taskInfo.Item2);
                }
            });
        }
        /// <summary>
        /// Comment
        /// </summary>
        public WPFTools.RelayCommand<V_Task> EditCommand
        {
            get => new WPFTools.RelayCommand<V_Task>(v_task =>
            {
                using (var client = new MainServiceClient())
                {
                    var task = client.GetTask(v_task.Id);
                    new NewTaskWindowView(task).ShowDialog();
                    ReloadTasks(task.State);
                }
            });
        }
        /// <summary>
        /// Comment
        /// </summary>
        public WPFTools.RelayCommand<V_Task> RemoveCommand
        {
            get => new WPFTools.RelayCommand<V_Task>(task =>
            {
                using (var client = new MainServiceClient())
                    client.RemoveTask(task.Id);
                ReloadTasks(task.State);
            });
        }
        #endregion
    }
}
