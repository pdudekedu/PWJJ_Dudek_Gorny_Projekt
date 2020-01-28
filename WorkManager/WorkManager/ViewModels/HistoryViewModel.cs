using WPFTools.Models;
using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WorkManager.Clients;
using WorkManager.Data.Models;

namespace WorkManager.ViewModels
{
    public class HistoryViewModel : AsynchonizableViewModel
    {
        public HistoryViewModel()
        {
            ItemsLoading = BackgroundTaskDescriptor<HistoryViewModel, IEnumerable<EFValuesHistory>>
                .RegisterDescriptor(this, "Ładowanie historii", GetItems)
                .OnCompleted((vm, res) => { vm.Items = res; });
        }
        #region Properties
        public BackgroundTaskDescriptor<HistoryViewModel, IEnumerable<EFValuesHistory>> ItemsLoading { get; }
        /// <summary>
        /// Elementy historii.
        /// </summary>
        public IEnumerable<EFValuesHistory> Items
        {
            get { return _Items; }
            set
            {
                _Items = value;
                OnPropertyChanged();
            }
        }
        private IEnumerable<EFValuesHistory> _Items;
        /// <summary>
        /// Zakres dat dla daty modyfikacji.
        /// </summary>
        public DateTimeRange? ModifyDateTimeRange
        {
            get { return _ModifyDateTimeRange; }
            set
            {
                _ModifyDateTimeRange = value;
                ItemsLoading.Invoke();
                OnPropertyChanged();
            }
        }
        private DateTimeRange? _ModifyDateTimeRange;
        #endregion
        #region Methods
        public IEnumerable<EFValuesHistory> GetItems()
        {
            using (var service = new MainServiceClient())
                return service.GetValuesHistory(ModifyDateTimeRange?.From, ModifyDateTimeRange?.To);
        }
        #endregion
    }
}
