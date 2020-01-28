using WPFTools.Models;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;

namespace WorkManager.Data.Models
{
    [DataContract]
    [Table("TEAMACCOUNTS")]
    public class TeamAccount : EFHistoryModel<int>
    {
        [DataMember]
        public int AccountId { get; set; }
        [DataMember]
        public int TeamId { get; set; }
        [DataMember]
        public Team Team { get; set; }
        [DataMember]
        [NotMapped]
        public AssignableModel Account { get; set; }
    }
}
