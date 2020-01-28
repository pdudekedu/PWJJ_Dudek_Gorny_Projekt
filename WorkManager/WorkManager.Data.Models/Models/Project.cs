using WPFTools.Attributes;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;

namespace WorkManager.Data.Models
{
    [DataContract(IsReference = true)]
    public class Project : OrganizationModel
    {
        /// <summary>
        /// Przypisany zespół.
        /// </summary>
        [DataMember]
        [TrackingProperty]
        [Int32Required(0)]
        public int TeamId
        {
            get { return _TeamId; }
            set
            {
                _TeamId = value;
                OnPropertyChanged();
            }
        }
        private int _TeamId;
        [DataMember]
        public Team Team { get; set; }
        [DataMember]
        [DeepTrackingCollection]
        public ObservableCollection<ProjectResource> ResourceForProject { get; set; }
        [DataMember]
        [NotMapped]
        public ObservableCollection<AssignableModel> AssignedResources { get; set; }
        [DataMember]
        [NotMapped]
        public ObservableCollection<AssignableModel> AvailableResources { get; set; }
        [DataMember]
        [NotMapped]
        public IEnumerable<AssignableModel> AvailableTeams { get; set; }
        public void AssignResources(IEnumerable<AssignableModel> resources, IEnumerable<AssignableModel> teams)
        {
            foreach (var account in ResourceForProject)
                account.Project = this;
            AssignedResources = new ObservableCollection<AssignableModel>(resources.Where(x => ResourceForProject.Any(z => z.ResourceId == x.Id)));
            foreach (var resource in AssignedResources)
                ResourceForProject.First(x => x.ResourceId == resource.Id).Resource = resource;
            AvailableResources = new ObservableCollection<AssignableModel>(resources.Except(AssignedResources));
            AvailableTeams = teams;
        }
    }
}
