using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WorkManager.Data.Models;

namespace WorkManager.Data.DataAccess
{
    public class HistoryRepository
    {
        public IEnumerable<EFValuesHistory> GetValuesHistory(DataContext context, DateTime? from, DateTime? to)
        {
            return context.EFValuesHistory
                .Where(x => (!from.HasValue || x.ModDate >= from.Value) && (!to.HasValue || x.ModDate >= to.Value))
                .OrderByDescending(x => x.ModDate)
                .ToList();
        }
    }
}
