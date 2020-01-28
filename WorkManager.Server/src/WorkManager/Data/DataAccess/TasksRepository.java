package WorkManager.Data.DataAccess;

import WorkManager.Data.Models.*;
import WorkManager.Data.QueryBuilders.DBCondition;
import WorkManager.Data.QueryBuilders.DBConditionOperator;
import WorkManager.Data.QueryBuilders.QueryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class TasksRepository extends Repository {
    public List<V_Task> getTasks(DataContext context, int state, int projectId) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(context.V_Tasks)
                .Where(new DBCondition(V_Task.State, DBConditionOperator.Equal, state))
                .Where(new DBCondition(V_Task.ProjectId, DBConditionOperator.Equal, projectId));
        return executeQuery(V_Task.class, context, queryBuilder);
    }

    public List<TaskTime> getTaskTimes(DataContext context, int taskId) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(context.TaskTimes)
                .Where(new DBCondition(TaskTime.TaskId, DBConditionOperator.Equal, taskId));
        return executeQuery(TaskTime.class, context, queryBuilder);
    }

    public List<TaskResource> getTaskResources(DataContext context, int taskId) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(context.TaskResources)
                .Where(new DBCondition(TaskResource.TaskId, DBConditionOperator.Equal, taskId));
        return executeQuery(TaskResource.class, context, queryBuilder);
    }
}
