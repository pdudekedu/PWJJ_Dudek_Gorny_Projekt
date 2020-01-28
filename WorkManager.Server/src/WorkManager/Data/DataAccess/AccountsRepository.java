package WorkManager.Data.DataAccess;

import WorkManager.Data.Models.DataContext;
import WorkManager.Data.Models.EFAccount;
import WorkManager.Data.Models.EFValuesHistory;
import WorkManager.Data.QueryBuilders.DBCondition;
import WorkManager.Data.QueryBuilders.DBConditionOperator;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import WorkManager.Data.QueryBuilders.QueryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AccountsRepository extends Repository {
    public EFAccount GetUser(DataContext context, String userName, String password) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(context.Accounts);
        queryBuilder.Where(new DBCondition(EFAccount.UserName, DBConditionOperator.Equal, userName));
        return executeQuery(EFAccount.class, context, queryBuilder).stream().findFirst().orElse(null);
    }
}
