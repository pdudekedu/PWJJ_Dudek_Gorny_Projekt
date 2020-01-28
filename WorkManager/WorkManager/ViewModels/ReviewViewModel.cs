using WPFTools;
using WPFTools.Extensions;
using WPFTools.Models;
using WPFTools.ViewModels;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace WorkManager.ViewModels
{
    public abstract class ReviewViewModel<T> : AsynchonizableViewModel
        where T : EFModel
    {
        public ReviewViewModel()
        {
            ItemsLoading = BackgroundTaskDescriptor<ReviewViewModel<T>, IEnumerable<T>>
                .RegisterDescriptor(this, "Ładowanie danych", GetItems)
                .OnCompleted((vm, res) => { vm.Items = res; });
            ItemsLoading.Invoke();
        }
        #region Properties
        public BackgroundTaskDescriptor<ReviewViewModel<T>, IEnumerable<T>> ItemsLoading { get; }
        /// <summary>
        /// Elementy historii.
        /// </summary>
        public IEnumerable<T> Items
        {
            get { return _Items; }
            set
            {
                _Items = value;
                OnPropertyChanged();
            }
        }
        private IEnumerable<T> _Items;
        #endregion
        #region Methods
        protected abstract IEnumerable<T> GetItems();
        internal abstract string GetCsvHeader();
        internal abstract string GetCsvLine(T item);
        #endregion
        #region Command
        /// <summary>
        /// Obsługuje akcję eksportu do pliku CSV.
        /// </summary>
        public WPFTools.RelayCommand ExportCommand
        {
            get
            {
                return new WPFTools.RelayCommand(() =>
                {
                    SaveFileDialog saveFileDialog = new SaveFileDialog
                    {
                        Filter = "Pliki CSV|*.csv",
                        FileName = $"dane_{DateTime.Now.ToString("dd-MM-yyyy_HHmmss")}"
                    };

                    if (saveFileDialog.ShowDialog() == true)
                    {
                        if (saveFileDialog.FileName.IsFileLocked())
                        {
                            MessageBox.Show("Plik jest otwarty w innym programie.", App.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Error);
                            return;
                        }

                        using (new Busy())
                        using (var fileStream = new StreamWriter(saveFileDialog.FileName, false, Encoding.UTF8))
                        {
                            fileStream.WriteLine(GetCsvHeader());
                            foreach (var measurement in Items)
                                fileStream.WriteLine(GetCsvLine(measurement));
                        }

                        MessageBox.Show("Pomiary zostały wyeksporotowane prawidłowo.", App.CurrentBaseApp.ProgramTitle, MessageBoxButton.OK, MessageBoxImage.Information);
                    }
                });
            }
        }
        #endregion
    }
}
