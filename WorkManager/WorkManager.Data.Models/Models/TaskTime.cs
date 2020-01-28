using WPFTools.Attributes;
using WPFTools.Models;
using System;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;
using WorkManager.Data.Enums;

namespace WorkManager.Data.Models
{
    [DataContract]
    public class TaskTime : EFHistoryModel<int>
    {
        [DataMember]
        [TrackingProperty]
        public DateTime? Time
        {
            get { return _Time; }
            set
            {
                _Time = value;
                OnPropertyChanged();
            }
        }
        private DateTime? _Time;

        [DataMember]
        public TimeType Type { get; set; }
        [DataMember]
        public int TaskId { get; set; }
        [DataMember]
        [ForeignKey(nameof(TaskId))]
        public Task Task { get; set; }
    }
}
