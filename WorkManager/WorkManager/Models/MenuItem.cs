using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WorkManager.Models
{
    public abstract class MenuItem
    {
        public MenuItem(string iconName, string name)
        {
            Icon = new Uri($"pack://application:,,,/Resources/Images/{iconName}_32_2C3E50.png");
            HoverIcon = new Uri($"pack://application:,,,/Resources/Images/{iconName}_32_FFFFFF.png");
            Name = name;
        }
        public Uri Icon { get; }
        public Uri HoverIcon { get; }
        public string Name { get; }
        public abstract AsynchonizableViewModel GetViewModel();
    }

    public class MenuItem<T> : MenuItem
        where T : AsynchonizableViewModel, new()
    {
        public MenuItem(string iconName, string name) : base(iconName, name) { }

        public override AsynchonizableViewModel GetViewModel()
        {
            return new T();
        }
    }
}
