using WPFTools.Attributes;
using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;
using WorkManager.Data.Enums;

namespace WorkManager.Data.Models
{
    [DataContract]
    public class Task : OrganizationModel
    {
        [DataMember]
        public int ProjectId { get; set; }
        [DataMember]
        [TrackingProperty]
        public int? AccountId
        {
            get { return _AccountId; }
            set
            {
                _AccountId = value;
                OnPropertyChanged();
            }
        }
        private int? _AccountId;
        [DataMember]
        public EFAccount Account { get; set; }
        [DataMember]
        public TaskState State
        {
            get { return _State; }
            set
            {
                _State = value;
                OnPropertyChanged();
            }
        }
        private TaskState _State;
        /// <summary>
        /// Comment
        /// </summary>
        [DataMember]
        [TrackingProperty]
        public DateTime? EstimateStart
        {
            get { return _EstimateStart; }
            set
            {
                _EstimateStart = value;
                OnPropertyChanged();
            }
        }
        private DateTime? _EstimateStart;
        /// <summary>
        /// Comment
        /// </summary>
        [DataMember]
        [TrackingProperty]
        public DateTime? EstimateEnd
        {
            get { return _EstimateEnd; }
            set
            {
                _EstimateEnd = value;
                OnPropertyChanged();
            }
        }
        private DateTime? _EstimateEnd;

        [DataMember]
        public ICollection<TaskTime> TaskTimes { get; set; }
        [DataMember]
        [DeepTrackingCollection]
        public ObservableCollection<TaskResource> ResourceForTask { get; set; }
        [DataMember]
        [NotMapped]
        public ObservableCollection<AssignableModel> AssignedResources { get; set; }
        [DataMember]
        [NotMapped]
        public ObservableCollection<AssignableModel> AvailableResources { get; set; }
        public void AssignResources(IEnumerable<AssignableModel> resources)
        {
            foreach (var taskResource in ResourceForTask)
                taskResource.Task = this;
            AssignedResources = new ObservableCollection<AssignableModel>(resources.Where(x => ResourceForTask.Any(z => z.ResourceId == x.Id)));
            foreach (var resource in AssignedResources)
                ResourceForTask.First(x => x.ResourceId == resource.Id).Resource = resource;
            AvailableResources = new ObservableCollection<AssignableModel>(resources.Except(AssignedResources));
        }
    }
}
