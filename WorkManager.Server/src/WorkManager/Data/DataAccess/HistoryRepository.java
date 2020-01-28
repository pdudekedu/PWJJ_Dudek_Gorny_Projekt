package WorkManager.Data.DataAccess;

import WorkManager.Data.Models.DataContext;
import WorkManager.Data.Models.EFValuesHistory;
import WorkManager.Data.QueryBuilders.DBCondition;
import WorkManager.Data.QueryBuilders.DBConditionOperator;
import WorkManager.Data.QueryBuilders.QueryBuilder;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryRepository extends Repository {
    public List<EFValuesHistory> GetValuesHistory(DataContext context, Date from, Date to) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(context.ValuesHistory).OrderByDescending(EFValuesHistory.ModDate);
        if (from != null)
            queryBuilder.Where(new DBCondition(EFValuesHistory.ModDate, DBConditionOperator.GreaterOrEqual, from));
        if (to != null)
            queryBuilder.Where(new DBCondition(EFValuesHistory.ModDate, DBConditionOperator.LessOrEqual, to));
        return executeQuery(EFValuesHistory.class, context, queryBuilder);
    }
}

