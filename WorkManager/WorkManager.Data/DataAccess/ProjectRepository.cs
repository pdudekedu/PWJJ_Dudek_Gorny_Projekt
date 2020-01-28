using WPFTools.Extensions;
using WPFTools.Models;
using WPFTools.QueryBuilders;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using WorkManager.Data.Models;

namespace WorkManager.Data.DataAccess
{
    internal class ProjectRepository : Repository<Project>
    {
        public bool CanRemoveProject(DataContext context, int id)
        {
            return !context.ExecuteScalar<bool>(System.Data.SqlDbType.Bit,
                new BasicQueryBuilder().GetIsUsedQuery(id,
                    new DBTableColumn(nameof(context.Tasks), nameof(Task.ProjectId))));
        }

        public Project GetProject(DataContext context, int id)
        {
            return context.Projects
                .Where(x => x.Id == id)
                .Include(x => x.ResourceForProject)
                .FirstOrDefault();
        }

        public IEnumerable<AssignableModel> GetResources(DataContext context, int[] assigned)
        {
            return (from r in context.Resources
                    where assigned.Contains(r.Id) || r.InUse
                    orderby r.Name
                    select new AssignableModel()
                    {
                        Id = r.Id,
                        Name = r.Name
                    }).ToList();
        }

        public IEnumerable<AssignableModel> GetTeams(DataContext context, int? assigned)
        {
            return (from t in context.Teams
                    where (assigned.HasValue && t.Id == assigned.Value) || t.InUse
                    orderby t.Name
                    select new AssignableModel()
                    {
                        Id = t.Id,
                        Name = t.Name
                    }).ToList();
        }

        public IEnumerable<AssignableModel> GetAccounts(DataContext context, int projectId)
        {
            var ids = context.Projects
                .Include(x => x.Team)
                .ThenInclude(x => x.AccountTeams).SelectMany(x => x.Team.AccountTeams.Select(y => y.AccountId));
            return context.Accounts.Where(x => ids.Contains(x.Id)).Select(x=> new AssignableModel() { Id = x.Id, Name = $"{x.Name} {x.Surname}" }).ToList();
        }

        public IEnumerable<V_ProjectStat> GetProjectStats(DataContext context)
        {
            return context.V_ProjectsStat.ToList();
        }

        public IEnumerable<V_AccountStat> GetAccountStats(DataContext context)
        {
            return context.V_AccountStat.ToList();
        }
    }
}
