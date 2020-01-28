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
    [Table("V_ACCOUNTSTAT")]
    public class V_AccountStat : EFModel
    {
        [DataMember]
        public string DisplayName { get; set; }
        [DataMember]
        public int? TaskCount { get; set; }
        [DataMember]
        public int? ProjectCount { get; set; }
        [DataMember]
        public decimal? WorkTime { get; set; }
        [DataMember]
        public decimal? EstimateWorkTime { get; set; }
        [DataMember]
        public decimal? Punctuality { get; set; }
    }
}
