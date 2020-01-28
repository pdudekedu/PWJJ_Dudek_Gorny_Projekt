package WorkManager.Data.DataAccess;

import WorkManager.Data.Models.*;
import WorkManager.Data.QueryBuilders.DBCondition;
import WorkManager.Data.QueryBuilders.DBConditionOperator;
import WorkManager.Data.QueryBuilders.QueryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectsRepository extends Repository {
    public boolean CanRemoveProject(DataContext context, int id) throws SQLException {
        return !executeScalarBit(context, new QueryBuilder().GetIsUsedQuery(id, Task.ProjectId));
    }

    public List<ProjectResource> getProjectResources(DataContext context, int projectId) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(context.ProjectResources)
                .Where(new DBCondition(ProjectResource.ProjectId, DBConditionOperator.Equal, projectId));
        return executeQuery(ProjectResource.class, context, queryBuilder);
    }

    public List<AssignableModel> getResources(DataContext context, List<Integer> assigned) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if(assigned.size() > 0)
            return executeQuery(AssignableModel.class, context,
                    String.format("SELECT R.ID, R.NAME FROM RESOURCES R " +
                            "WHERE R.ID IN (%s) OR R.INUSE = 1", assigned.stream().map(Object::toString).collect(Collectors.joining(","))));
        return executeQuery(AssignableModel.class, context,"SELECT R.ID, R.NAME FROM RESOURCES R WHERE R.INUSE = 1");
    }

    public List<AssignableModel> getTeams(DataContext context) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return executeQuery(AssignableModel.class, context,"SELECT T.ID, T.NAME FROM TEAMS T WHERE T.INUSE = 1");
    }

    public List<AssignableModel> getAccounts(DataContext context, int projectId) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return executeQuery(AssignableModel.class, context,String.format("SELECT A.ID, A.NAME + ' ' + A.SURNAME AS NAME " +
                "FROM MGMNT.ACCOUNTS A " +
                "JOIN TEAMACCOUNTS TA ON TA.ACCOUNTID = A.ID" +
                "JOIN PROJECTS P ON P.TEAMID = TA.TEAMID" +
                "WHERE P.ID = %d", projectId));
    }
}
