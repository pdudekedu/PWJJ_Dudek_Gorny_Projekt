using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WorkManager.Clients;
using WorkManager.Converters;
using WorkManager.Data.Models;

namespace WorkManager.ViewModels
{
    public class WorkflowViewModel : ReviewViewModel<V_ProjectStat>
    {
        protected override IEnumerable<V_ProjectStat> GetItems()
        {
            using (var client = new MainServiceClient())
                return client.GetProjectStats();
        }

        private readonly TaskStateToStringConverter _TaskStateToStringConverter = new TaskStateToStringConverter();

        internal override string GetCsvHeader()
        {
            string[] values = new string[]
            {
                "Nazwa projektu",
                "Opis",
                "Status projektu",
                "Zespół",
                "Ilość nowych zadań",
                "Ilość aktywnych zadań",
                "Ilość zawieszonych zadań",
                "Ilość zakończonych zadań",
                "Czas pracy",
                "Szacowany czas pracy",
                "Wskaźnik terminowości"
            };
            return string.Join(";", values);
        }

        internal override string GetCsvLine(V_ProjectStat item)
        {
            string[] values = new string[]
            {
                item.Name ?? "",
                item.Description ?? "",
                (_TaskStateToStringConverter.Convert(item.State, null, null, CultureInfo.InvariantCulture) as string) ?? "",
                item.Team ?? "",
                item.New.ToString() ?? "",
                item.Active.ToString() ?? "",
                item.Suspend.ToString() ?? "",
                item.Complete.ToString() ?? "",
                item.WorkTime.ToString("F1"),
                item.EstimateWorkTime.ToString("F1"),
                item.Punctuality.ToString("F2")
            };
            return string.Join(";", values);
        }
    }
}
