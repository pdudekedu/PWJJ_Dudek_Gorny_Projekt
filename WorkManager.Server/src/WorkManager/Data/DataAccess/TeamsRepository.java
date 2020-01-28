package WorkManager.Data.DataAccess;

import WorkManager.Data.Models.*;
import WorkManager.Data.QueryBuilders.DBCondition;
import WorkManager.Data.QueryBuilders.DBConditionOperator;
import WorkManager.Data.QueryBuilders.QueryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeamsRepository extends Repository {
    public boolean CanRemoveTeam(DataContext context, int id) throws SQLException {
        return !executeScalarBit(context, new QueryBuilder().GetIsUsedQuery(id, Project.TeamId));
    }

    public boolean CanRemoveUser(DataContext context, int id) throws SQLException {
        return !executeScalarBit(context, new QueryBuilder().GetIsUsedQuery(id, TeamAccount.AccountId, Task.AccountId));
    }

    public List<TeamAccount> getTeamAccounts(DataContext context, int teamId) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(context.TeamAccounts)
                .Where(new DBCondition(TeamAccount.TeamId, DBConditionOperator.Equal, teamId));
        return executeQuery(TeamAccount.class, context, queryBuilder);
    }

    public List<AssignableModel> getAccounts(DataContext context, List<Integer> assigned) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if(assigned.size() > 0)
            return executeQuery(AssignableModel.class, context,
                    String.format("SELECT A.ID, A.NAME + ' ' + A.SURNAME AS NAME FROM MGMNT.ACCOUNTS A " +
                            "WHERE A.ID IN (%s) OR A.INUSE = 1", assigned.stream().map(Object::toString).collect(Collectors.joining(","))));
        return executeQuery(AssignableModel.class, context,"SELECT A.ID, A.NAME + ' ' + A.SURNAME AS NAME FROM MGMNT.ACCOUNTS A WHERE A.INUSE = 1");
    }

    public List<AssignableModel> getAccounts(DataContext context, int projectId) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String query = String.format("SELECT A.ID, A.NAME + ' ' + A.SURNAME AS NAME " +
                "FROM MGMNT.ACCOUNTS A " +
                "JOIN TEAMACCOUNTS TA ON TA.ACCOUNTID = A.ID " +
                "JOIN PROJECTS P ON P.TEAMID = TA.TEAMID " +
                "WHERE P.ID = %d", projectId);
        return executeQuery(AssignableModel.class, context, query);
    }
}
