using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Data;
using System.Windows.Markup;
using WorkManager.Data.Enums;
using WorkManager.Data.Models;

namespace WorkManager.Converters
{

    public class ChangeTaskStateConverter : MarkupExtension, IMultiValueConverter
    {
        public object Convert(object[] values, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
          if (values[0] is V_Task task && values[1] is TaskState state)
                return new Tuple<V_Task, TaskState>(task, state);
            return null;
        }

        public object[] ConvertBack(object value, Type[] targetTypes, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }

        public override object ProvideValue(IServiceProvider serviceProvider)
        {
            return this;
        }
    }
}
