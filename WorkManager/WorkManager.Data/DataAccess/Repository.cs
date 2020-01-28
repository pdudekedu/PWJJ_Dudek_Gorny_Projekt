using WPFTools.DataAccess;
using WPFTools.Models;
using System.Collections.Generic;
using System.Linq;
using WorkManager.Data.Models;

namespace WorkManager.Data.DataAccess
{
    internal class Repository<TModel> : EFRepository
        where TModel : OrganizationModel
    {
        public IEnumerable<T> GetAll<T>(DataContext context, bool? inUseFilter)
            where T : EFModel<int>, IExcludableModel 
        {
            IQueryable<T> result = context.Query<T>();
            if (inUseFilter.HasValue)
                result = result.Where(x => !inUseFilter.HasValue || x.InUse == inUseFilter.Value);
            return result.ToList();
        }
        public T Get<T>(DataContext context, int id)
            where T : EFModel<int>
        {
            return context.Set<T>().FirstOrDefault(x => x.Id == id);
        }
    }
}
