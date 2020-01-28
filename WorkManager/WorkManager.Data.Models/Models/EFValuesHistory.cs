using WPFTools.Attributes;
using WPFTools.Comparers;
using WPFTools.Enums;
using WPFTools.Models;
using System;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;
using WorkManager.Data.Enums;

namespace WorkManager.Data.Models
{
    [DataContract]
    [Table("V_VALUESHISTORY", Schema = "HST")]
    public class EFValuesHistory : EFModel
    {
        [DataMember]
        public string ModUser { get; set; }
        [DataMember]
        public string ModUserDisplayName { get; set; }
        [DataMember]
        public DateTime ModDate { get; set; }
        [DataMember]
        public string ModComp { get; set; }
        [DataMember]
        public HistoryModificationType ModType { get; set; }
        [DataMember]
        public string Area { get; set; }
        [DataMember]
        public string Element { get; set; }
        [DataMember]
        public string Path { get; set; }
        [DataMember]
        public string OldValue { get; set; }
        [DataMember]
        public string NewValue { get; set; }
    }
}
