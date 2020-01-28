using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;
using System.Windows.Markup;
using System.Windows.Media;
using WorkManager.Data.Enums;

namespace WorkManager.Converters
{
    /// <summary>
    /// Comment
    /// </summary>
    public class TaskStateToBrushConverter : MarkupExtension, IValueConverter
    {
        /// <summary>
        /// Domyślny konstruktor.
        /// </summary>
        public TaskStateToBrushConverter()
        {

        }
        /// <summary>
        /// Obsługuje konwersję standardową.
        /// </summary>
        /// <param name="value">Wartość konwertowana.</param>
        /// <param name="targetType">Typ docelowy.</param>
        /// <param name="parameter">Parametr.</param>
        /// <param name="culture">Kultura.</param>
        /// <returns>Wartość skonwertowana.</returns>
        public object Convert(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            if(value is TaskState state)
            {
                switch (state)
                {
                    case TaskState.New:
                        return Brushes.LightGray;
                    case TaskState.Active:
                        return Brushes.Navy;
                    case TaskState.Suspend:
                        return Brushes.Gray;
                    case TaskState.Complete:
                        return Brushes.Green;
                }
            }
            return Brushes.Transparent;
        }
        /// <summary>
        /// Obsługuje konwersję zwrotną.
        /// </summary>
        /// <param name="value">Wartość konwertowana.</param>
        /// <param name="targetType">Typ docelowy.</param>
        /// <param name="parameter">Parametr.</param>
        /// <param name="culture">Kultura.</param>
        /// <returns>Wartość skonwertowana.</returns>
        public object ConvertBack(object value, Type targetType, object parameter, System.Globalization.CultureInfo culture)
        {
            throw new NotImplementedException();
        }
        /// <summary>
        /// Zwraca obiekt this jako wartość docelowa dla rozszerzenia znaczników (zaimplementowane przez MarkupExtension).
        /// </summary>
        /// <param name="serviceProvider">Obiekt IServiceProvider.</param>
        /// <returns>Wartość docelowa dla rozszerzenia znaczników.</returns>
        public override object ProvideValue(IServiceProvider serviceProvider)
        {
            return this;
        }
    }
}
