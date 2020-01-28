using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using WorkManager.Data.Enums;

namespace WorkManager.Data.Models
{
    [DataContract]
    [Table("V_PROJECTSTAT")]
    public class V_ProjectStat : EFModel
    {
        [DataMember]
        public int ProjectId { get; set; }
        [DataMember]
        public string Name { get; set; }
        [DataMember]
        public string Description { get; set; }
        [DataMember]
        public int New { get; set; }
        [DataMember]
        public int Active { get; set; }
        [DataMember]
        public int Suspend { get; set; }
        [DataMember]
        public int Complete { get; set; }
        [DataMember]
        public TaskState State { get; set; }
        [DataMember]
        public string Team { get; set; }
        [DataMember]
        public decimal WorkTime { get; set; }
        [DataMember]
        public decimal EstimateWorkTime { get; set; }
        [DataMember]
        public decimal Punctuality { get; set; }
    }
}
