using WPFTools.Attributes;
using WPFTools.Communication.Services;
using WPFTools.Enums;
using WPFTools.Models;
using WPFTools.Windows;
using System;
using System.Collections.Generic;
using WorkManager.Clients;
using WorkManager.Data.Models;
using WorkManager.Models;
using WorkManager.ViewModels;

namespace WorkManager
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    [UserPermission("TSK", "Zadania", 0)]
    [UserPermission("WKF", "Stan projektów", 2)]
    [UserPermission("WKT", "Czas pracy", 4)]
    [UserPermission("HST", "Historia", 6)]
    [UserPermission("PRJ", "Projekty", 8)]
    [UserPermission("RSC", "Zasoby", 10)]
    [UserPermission("USR", "Użytkownicy", 12)]
    [UserPermission("TMS", "Zespoły", 14)]
    public partial class App : ClientApplication
    {
        public App()
        {
            IsLoginEnabled = true;
        }

        public static App CurrentApp => Current as App;

        public override IMetadataUsers GetUsersContext()
        {
            return new MainServiceClient();
        }

        protected override WPFTools.Models.IStartupContext GetStartupContext()
        {
            return new StartupServiceClient();
        }

        protected override string GetUserType(EFAccount account)
        {
            return UserType.GetUserType(account?.Permissions)?.Name;
        }
        public override void Login(StartupMode mode = StartupMode.Login)
        {
            base.Login(mode);
            ClientInfo.Current.ModUser = CurrentBaseApp.LoggedUser.HistoryName;
            ClientInfo.Current.ModComp = Environment.MachineName;
            ClientInfo.Current.LanguageId = 0;
            if (mode == StartupMode.SwitchUser)
                (Current.MainWindow.DataContext as MainWindowViewModel)?.LoadMenuItems();
        }
    }
}
