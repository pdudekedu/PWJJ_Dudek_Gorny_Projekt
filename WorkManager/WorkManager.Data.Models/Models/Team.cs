using WPFTools.Attributes;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;

namespace WorkManager.Data.Models
{
    [DataContract]
    public class Team : OrganizationModel
    {
        [DataMember]
        [DeepTrackingCollection]
        public ObservableCollection<TeamAccount> AccountTeams { get; set; }
        [DataMember]
        [NotMapped]
        public ObservableCollection<AssignableModel> AssignedAccounts { get; set; }
        [DataMember]
        [NotMapped]
        public ObservableCollection<AssignableModel> AvailableAccounts { get; set; }
        public void AssignAccountTeams(IEnumerable<AssignableModel> accounts)
        {
            foreach (var account in AccountTeams)
                account.Team = this;
            AssignedAccounts = new ObservableCollection<AssignableModel>(accounts.Where(x => AccountTeams.Any(z => z.AccountId == x.Id)));
            foreach (var account in AssignedAccounts)
                AccountTeams.First(x => x.AccountId == account.Id).Account = account;
            AvailableAccounts = new ObservableCollection<AssignableModel>(accounts.Except(AssignedAccounts));
        }
    }
}
