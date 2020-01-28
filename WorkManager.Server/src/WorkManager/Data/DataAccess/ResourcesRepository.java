package WorkManager.Data.DataAccess;

import WorkManager.Data.Models.DataContext;
import WorkManager.Data.Models.ProjectResource;
import WorkManager.Data.Models.Task;
import WorkManager.Data.Models.TaskResource;
import WorkManager.Data.QueryBuilders.QueryBuilder;

import java.sql.SQLException;

public class ResourcesRepository extends Repository {
    public boolean CanRemoveResource(DataContext context, int id) throws SQLException {
        return !executeScalarBit(context, new QueryBuilder().GetIsUsedQuery(id, ProjectResource.ResourceId, TaskResource.ResourceId));
    }
}
