using WPFTools.Extensions;
using WPFTools.Models;
using WPFTools.QueryBuilders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WorkManager.Data.Models;

namespace WorkManager.Data.DataAccess
{
    internal class ResourceRepository : Repository<Resource>
    {
        public bool CanRemoveResource(DataContext context, int id)
        {
            return !context.ExecuteScalar<bool>(System.Data.SqlDbType.Bit,
                new BasicQueryBuilder().GetIsUsedQuery(id,
                    new DBTableColumn(nameof(context.ProjectResources), nameof(ProjectResource.ResourceId)),
                    new DBTableColumn(nameof(context.TaskResources), nameof(TaskResource.ResourceId))));
        }
    }
}
