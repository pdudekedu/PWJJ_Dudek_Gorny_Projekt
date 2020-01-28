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
    public class WorktimeViewModel : ReviewViewModel<V_AccountStat>
    {
        protected override IEnumerable<V_AccountStat> GetItems()
        {
            using (var client = new MainServiceClient())
                return client.GetAccountStats();
        }

        internal override string GetCsvHeader()
        {
            string[] values = new string[]
            {
                "Użytkownik",
                "Ilość zadań",
                "Ilość projektów",
                "Czas pracy",
                "Wskaźnik terminowości"
            };
            return string.Join(";", values);
        }

        internal override string GetCsvLine(V_AccountStat item)
        {
            string[] values = new string[]
            {
                item.DisplayName ?? "",
                item.TaskCount?.ToString() ?? "",
                item.ProjectCount?.ToString() ?? "",
                item.WorkTime?.ToString("F1") ?? "",
                item.Punctuality?.ToString("F2") ?? ""
            };
            return string.Join(";", values);
        }
    }
}
