using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Data;
using System.Windows.Markup;
using WorkManager.Data.Enums;

namespace WorkManager.Converters
{
    /// <summary>
    /// Comment
    /// </summary>
    public class HistoryModificationTypeToDisplayNameConverter : MarkupExtension, IValueConverter
    {
        /// <summary>
        /// Domyślny konstruktor.
        /// </summary>
        public HistoryModificationTypeToDisplayNameConverter()
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
            if(value is HistoryModificationType modType)
            {
                switch (modType)
                {
                    case HistoryModificationType.Insert:
                        return "Wprowadzenie";
                    case HistoryModificationType.Update:
                        return "Modyfikacja";
                    case HistoryModificationType.Delete:
                        return "Usunięcie";
                }
            }
            return null;
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
