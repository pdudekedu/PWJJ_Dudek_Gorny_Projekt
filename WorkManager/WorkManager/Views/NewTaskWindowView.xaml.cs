using WPFTools.Windows;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using WorkManager.Data.Models;
using WorkManager.ViewModels;

namespace WorkManager.Views
{
    /// <summary>
    /// Interaction logic for NewTaskWindowView.xaml
    /// </summary>
    public partial class NewTaskWindowView : DialogWindow
    {
        public NewTaskWindowView(Task task)
        {
            InitializeComponent();
            Title = "Edycja zadania";
            DataContext = new NewTaskWindowViewModel(task) { Close = () => Close() };
        }
        public NewTaskWindowView(int projectId)
        {
            InitializeComponent();
            Title = "Dodanie nowego zadania";
            DataContext = new NewTaskWindowViewModel(projectId) { Close = () => Close() };
        }
    }
}
