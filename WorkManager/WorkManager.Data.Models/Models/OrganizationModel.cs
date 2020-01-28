using WPFTools.Attributes;
using WPFTools.Models;
using System;
using System.Runtime.Serialization;

namespace WorkManager.Data.Models
{
    [DataContract(IsReference = true)]
    public class OrganizationModel : EFExcludableModel<int>, IExcludableModel
    {
        /// <summary>
        /// Nazwa elementu
        /// </summary>
        [DataMember]
        [TrackingProperty]
        [StringRequired]
        public string Name
        {
            get { return _Name; }
            set
            {
                _Name = value;
                OnPropertyChanged();
            }
        }
        private string _Name;
        /// <summary>
        /// Opis elementu
        /// </summary>
        [DataMember]
        [TrackingProperty]
        public string Description
        {
            get { return _Description; }
            set
            {
                _Description = value;
                OnPropertyChanged();
            }
        }
        private string _Description;

    }
}
