using System.Runtime.Serialization;

namespace WorkManager.Data.Enums
{
    [DataContract]
    public enum TimeType
    {
        [EnumMember]
        Start = 0,
        [EnumMember]
        Suspend = 1,
        [EnumMember]
        Resume = 2,
        [EnumMember]
        End = 3,
    }
}
