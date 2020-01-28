using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;
using WorkManager.Data.Enums;

namespace WorkManager.Data.Models
{
    [DataContract]
    public class V_Task : EFModel<int>
    {
        [DataMember]
        public string Name { get; set; }
        [DataMember]
        public string Description { get; set; }
        [DataMember]
        public int ProjectId { get; set; }
        [DataMember]
        public int? AccountId { get; set; }
        [DataMember]
        public string Account { get; set; }
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
        [DataMember]
        public DateTime? EstimateStart { get; set; }
        [DataMember]
        public DateTime? EstimateEnd { get; set; }
        [NotMapped]
        public IEnumerable<NamedValue<string, TaskState>> States
        {
            get
            {
                switch (State)
                {
                    case TaskState.New:
                        yield return new NamedValue<string, TaskState>("Nowe", TaskState.New);
                        yield return new NamedValue<string, TaskState>("Aktywne", TaskState.Active);
                        yield return new NamedValue<string, TaskState>("Zakończone", TaskState.Complete);
                        break;
                    case TaskState.Active:
                        yield return new NamedValue<string, TaskState>("Aktywne", TaskState.Active);
                        yield return new NamedValue<string, TaskState>("Wstrzymane", TaskState.Suspend);
                        yield return new NamedValue<string, TaskState>("Zakończone", TaskState.Complete);
                        break;
                    case TaskState.Suspend:
                        yield return new NamedValue<string, TaskState>("Aktywne", TaskState.Active);
                        yield return new NamedValue<string, TaskState>("Wstrzymane", TaskState.Suspend);
                        yield return new NamedValue<string, TaskState>("Zakończone", TaskState.Complete);
                        break;
                    case TaskState.Complete:
                        yield return new NamedValue<string, TaskState>("Aktywne", TaskState.Active);
                        yield return new NamedValue<string, TaskState>("Zakończone", TaskState.Complete);
                        break;
                }
            }
        }
    }
}
