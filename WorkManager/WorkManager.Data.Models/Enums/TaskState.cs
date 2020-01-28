using System.Runtime.Serialization;

namespace WorkManager.Data.Enums
{
    [DataContract]
    public enum TaskState
    {
        [EnumMember]
        New = 0,
        [EnumMember]
        Active = 1,
        [EnumMember]
        Suspend = 2,
        [EnumMember]
        Complete = 3,
    }
}
