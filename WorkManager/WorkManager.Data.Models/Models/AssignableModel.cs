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
    public class AssignableModel : EFModel<int>
    {
        [DataMember]
        public string Name { get; set; }
    }
}
