using WPFTools.Enums;
using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WorkManager.Clients;
using WorkManager.Data.Models;

namespace WorkManager.ViewModels
{
    public class TeamsViewModel : MetadataViewModel<V_Team, Team>
    {
        public override bool CanRemoveItem(V_Team item)
        {
            using (var client = new MainServiceClient())
                return client.CanRemoveTeam(item.Id);
        }

        public override Team CreateNewItem()
        {
            using (var client = new MainServiceClient())
                return client.CreateTeam();
        }

        public override bool RemoveItem(V_Team item)
        {
            using (var client = new MainServiceClient())
                client.RemoveTeam(item.Id);
            return true;
        }

        protected override Team GetEditItem()
        {
            using (var client = new MainServiceClient())
                return client.GetTeam(EditItemId);
        }

        protected override IEnumerable<V_Team> GetItems()
        {
            using (var client = new MainServiceClient())
                return client.GetTeams(InUseFilter);
        }

        protected override string GetLoadingEditItemText()
        {
            return "Ładowanie danych zespołu";
        }

        protected override string GetLoadingItemsText()
        {
            return "Ładowanie zespołów";
        }

        #region Commands
        /// <summary>
        /// Obsługa akcji dodania konta do zespołu.
        /// </summary>
        public WPFTools.RelayCommand<AssignableModel> AddAccountCommand
        {
            get
            {
                return new WPFTools.RelayCommand<AssignableModel>((account) =>
                {
                    if (EditItem == null || account == null)
                        return;
                    EditItem.AccountTeams.Add(new TeamAccount()
                    {
                        AccountId = account.Id,
                        Team = EditItem,
                        Account = account,
                        TrackingState = WPFTools.Enums.TrackingState.Added
                    });
                    EditItem.AssignedAccounts.Add(account);
                    EditItem.AvailableAccounts.Remove(account);
                });
            }
        }
        /// <summary>
        /// Obsługa akcji usunięcia konta z zespołu.
        /// </summary>
        public WPFTools.RelayCommand<TeamAccount> RemoveAccountCommand
        {
            get
            {
                return new WPFTools.RelayCommand<TeamAccount>((accountTeam) =>
                {
                    if (EditItem == null || accountTeam == null)
                        return;
                    if (accountTeam.TrackingState.HasFlag(TrackingState.Deleted))
                        accountTeam.TrackingState ^= TrackingState.Deleted;
                    else 
                    {
                        if (accountTeam.TrackingState.HasFlag(TrackingState.Added))
                        {
                            EditItem.AccountTeams.Remove(accountTeam);
                            var account = EditItem.AssignedAccounts.FirstOrDefault(x => x.Id == accountTeam.AccountId);
                            if (account != null)
                            {
                                EditItem.AssignedAccounts.Remove(account);
                                EditItem.AvailableAccounts.Add(account);
                            }
                        }
                        else
                            accountTeam.TrackingState |= TrackingState.Deleted;
                    }
                    
                });
            }
        }
        #endregion
    }
}
