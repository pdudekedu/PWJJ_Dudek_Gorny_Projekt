using WPFTools.Attributes;
using WPFTools.Models;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;

namespace WorkManager.Data.Models
{
    [DataContract]
    public class DependentReource : EFHistoryModel<int>
    {
        /// <summary>
        /// Referencja do zasobu
        /// </summary>
        [DataMember]
        public int ResourceId { get; set; }
        [DataMember]
        [NotMapped]
        public AssignableModel Resource { get; set; }
        /// <summary>
        /// Ilość
        /// </summary>
        [DataMember]
        [Required]
        [TrackingProperty]
        public decimal? Count
        {
            get { return _Count; }
            set
            {
                _Count = value;
                OnPropertyChanged();
            }
        }
        private decimal? _Count;

    }
}
