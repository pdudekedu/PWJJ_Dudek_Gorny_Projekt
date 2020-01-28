using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;

namespace WorkManager.Data.Models
{
    [DataContract]
    [Table("TASKRESOURCES")]
    public class TaskResource : DependentReource
    {
        /// <summary>
        /// Referencja do zadania
        /// </summary>
        [DataMember]
        public int TaskId { get; set; }
        [DataMember]
        public Task Task { get; set; }
    }
}
