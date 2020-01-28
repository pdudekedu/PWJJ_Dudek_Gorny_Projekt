using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace WorkManager.Data.Enums
{
    [DataContract]
    public enum HistoryModificationType : byte
    {
        [EnumMember]
        Insert = 0,
        [EnumMember]
        Update = 1,
        [EnumMember]
        Delete = 2
    }
}
