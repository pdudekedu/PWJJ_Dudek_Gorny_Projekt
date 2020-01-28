using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WorkManager.Models;

namespace WorkManager.ViewModels
{
    public class MainWindowViewModel : BaseViewModel
    {
        public MainWindowViewModel()
        {
            LoadMenuItems();
        }

        public void LoadMenuItems()
        {
            var menuItems = new List<MenuItem>();
            if (App.CurrentClientApp.LoggedUser["TSK"].CanRead)
                menuItems.Add(new MenuItem<TasksViewModel>("task", "Zadania"));
            if (App.CurrentClientApp.LoggedUser["WKF"].CanRead)
                menuItems.Add(new MenuItem<WorkflowViewModel>("workflow", "Stan projektów"));
            if (App.CurrentClientApp.LoggedUser["WKT"].CanRead)
                menuItems.Add(new MenuItem<WorktimeViewModel>("worktime", "Czas pracy"));
            if (App.CurrentClientApp.LoggedUser["HST"].CanRead)
                menuItems.Add(new MenuItem<HistoryViewModel>("history", "Historia"));
            if (App.CurrentClientApp.LoggedUser["PRJ"].CanRead)
                menuItems.Add(new MenuItem<ProjectsViewModel>("project", "Projekty"));
            if (App.CurrentClientApp.LoggedUser["RSC"].CanRead)
                menuItems.Add(new MenuItem<ResourcesViewModel>("resource", "Zasoby"));
            if (App.CurrentClientApp.LoggedUser["USR"].CanRead)
                menuItems.Add(new MenuItem<UsersViewModel>("users", "Użytkownicy"));
            if (App.CurrentClientApp.LoggedUser["TMS"].CanRead)
                menuItems.Add(new MenuItem<TeamsViewModel>("team", "Zespoły"));
            MenuItems = menuItems;
            SelectedMenuItem = null;
            SelectedMenuItem = MenuItems.FirstOrDefault();
        }

        #region Properties
        /// <summary>
        /// Opcje menu głownego.
        /// </summary>
        public IEnumerable<MenuItem> MenuItems
        {
            get { return _MenuItems; }
            set
            {
                _MenuItems = value;
                OnPropertyChanged();
            }
        }
        private IEnumerable<MenuItem> _MenuItems;
        /// <summary>
        /// Wybrana opcja z menu aplikacji.
        /// </summary>
        public MenuItem SelectedMenuItem
        {
            get { return _SelectedMenuItem; }
            set
            {
                _SelectedMenuItem = value;
                SelectedViewModel = _SelectedMenuItem?.GetViewModel();
                OnPropertyChanged();
            }
        }
        private MenuItem _SelectedMenuItem;
        /// <summary>
        /// View model dla wybranej z menu opcji.
        /// </summary>
        public AsynchonizableViewModel SelectedViewModel
        {
            get { return _SelectedViewModel; }
            set
            {
                _SelectedViewModel = value;
                OnPropertyChanged();
            }
        }
        private AsynchonizableViewModel _SelectedViewModel;
        #endregion
    }
}
