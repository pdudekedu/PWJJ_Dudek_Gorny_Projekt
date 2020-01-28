using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;

namespace WorkManager.Data.Models
{
    [DataContract]
    [Table("PROJECTRESOURCES")]
    public class ProjectResource : DependentReource
    {
        /// <summary>
        /// Referencja do projektu
        /// </summary>
        [DataMember]
        public int ProjectId { get; set; }
        [DataMember]
        public Project Project { get; set; }
    }
}
