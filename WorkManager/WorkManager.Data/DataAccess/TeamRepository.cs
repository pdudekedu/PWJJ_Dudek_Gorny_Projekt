using WPFTools.Extensions;
using WPFTools.Models;
using WPFTools.QueryBuilders;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using WorkManager.Data.Models;

namespace WorkManager.Data.DataAccess
{
    internal class TeamRepository : Repository<Team>
    {
        public bool CanRemoveTeam(DataContext context, int id)
        {
            return !context.ExecuteScalar<bool>(System.Data.SqlDbType.Bit,
                new BasicQueryBuilder().GetIsUsedQuery(id,
                    new DBTableColumn(nameof(context.Projects), nameof(Project.TeamId))));
        }
        
        public bool CanRemoveUser(DataContext context, int id)
        {
            var query = new BasicQueryBuilder().GetIsUsedQuery(id,
                    new DBTableColumn(nameof(context.TeamAccounts), nameof(TeamAccount.AccountId)),
                    new DBTableColumn(nameof(context.Tasks), nameof(Task.AccountId)));
            return !context.ExecuteScalar<bool>(System.Data.SqlDbType.Bit, query);
        }

        public Team GetTeam(DataContext context, int id)
        {
            return context.Teams
                .Where(x => x.Id == id)
                .Include(x => x.AccountTeams)
                .FirstOrDefault();
        }

        public IEnumerable<AssignableModel> GetAccounts(DataContext context, int[] assigned)
        {
            return (from a in context.Accounts
                    where assigned.Contains(a.Id) || a.InUse
                    orderby $"{a.Name} {a.Surname}"
                    select new AssignableModel()
                    {
                        Id = a.Id,
                        Name = $"{a.Name} {a.Surname}"
                    }).ToList();
        }
    }
}
