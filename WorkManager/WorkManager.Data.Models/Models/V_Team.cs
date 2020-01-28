using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace WorkManager.Data.Models
{
    [DataContract]
    public class V_Team : EFModel<int>, IExcludableModel
    {
        [DataMember]
        public string Name { get; set; }
        [DataMember]
        public string Description { get; set; }
        [DataMember]
        public bool InUse { get; set; }
    }
}
