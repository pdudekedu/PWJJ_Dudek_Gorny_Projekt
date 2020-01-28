package WorkManager.Data.DataAccess;

import WorkManager.Data.Models.DataContext;
import WorkManager.Data.Models.EFValuesHistory;
import WorkManager.Data.Models.IInsertable;
import WorkManager.Data.Models.IUpdatable;
import WorkManager.Data.QueryBuilders.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository {
    public boolean executeScalarBit(DataContext context, String query) throws SQLException {
        ResultSet rs = context.getStatement().executeQuery(query);
        if (rs.next()) {
            return  rs.getBoolean(1);
        }
        return true;
    }

    public <T> List<T> executeQuery(Class<T> entity, DataContext context, QueryBuilder queryBuilder) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return executeQuery(entity, context, queryBuilder.GetQuery());
    }

    public <T> List<T> executeQuery(Class<T> entity, DataContext context, String query) throws SQLException, NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        System.out.println(query);
        ArrayList<T> result = new ArrayList<>();
        ResultSet rs = context.getStatement().executeQuery(query);
        Constructor<T> constructor = entity.getDeclaredConstructor(ResultSet.class);

        while (rs.next()) {
                result.add(constructor.newInstance(rs));
        }
        rs.close();
        return result;
    }

    public <T> List<T> getAll(Class<T> entity, DBTable table, DataContext context) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return executeQuery(entity, context, new QueryBuilder().Select().From(table));
    }

    public <T> List<T> getAll(Class<T> entity, DBTable table, DataContext context, Optional<Boolean> inUse) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(table);
        inUse.ifPresent(b -> queryBuilder.Where(new DBCondition(new DBTableColumn(table, "INUSE"), DBConditionOperator.Equal, b)));
        return executeQuery(entity, context, queryBuilder);
    }
    public <T> List<T> getAll(Class<T> entity, DBTable table, DataContext context, Boolean inUse) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(table);
        if(inUse != null)
            queryBuilder.Where(new DBCondition(new DBTableColumn(table, "INUSE"), DBConditionOperator.Equal, inUse));
        return executeQuery(entity, context, queryBuilder);
    }
    public <T> T get(Class<T> entity, DBTable table, DataContext context, Integer id) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        QueryBuilder queryBuilder = new QueryBuilder().Select().From(table);
        queryBuilder.Where(new DBCondition(new DBTableColumn(table, "ID"), DBConditionOperator.Equal, id));
        return executeQuery(entity, context, queryBuilder).stream().findFirst().orElse(null);
    }

    public boolean remove(DBTable table, DataContext context, Integer id) throws SQLException {
        return context.getStatement().executeUpdate(String.format("DELETE FROM %s WHERE ID = %d", table, id)) > 0;
    }

    public int insert(DBTable table, DataContext context, IInsertable entity) throws SQLException {
        if(entity == null)
            return 0;
        context.getStatement().execute(new InsertBuilder().Into(table).GetQuery(entity));
        ResultSet rs = context.getStatement().executeQuery(String.format("SELECT IDENT_CURRENT('%s')", table.getFullTableName()));
        if(rs.next())
            return rs.getInt(1);
        return 0;
    }

    public int update(DBTable table, DataContext context, IUpdatable entity) throws SQLException {
        if(entity == null)
            return 0;
        String query = new UpdateBuilder()
                .From(table)
                .Set(entity.getUpdateSets())
                .Where(new DBCondition(new DBTableColumn(table, "ID"), DBConditionOperator.Equal, entity.getId())).GetQuery();
        System.out.println(query);
        return context.getStatement().executeUpdate(query);
    }

    public int update(DBTable table, DataContext context, int id, DBSet... sets) throws SQLException {
        return context.getStatement().executeUpdate(
                new UpdateBuilder()
                        .From(table)
                        .Set(sets)
                        .Where(new DBCondition(new DBTableColumn(table, "ID"), DBConditionOperator.Equal, id)).GetQuery());
    }
}
