package WorkManager.Data.QueryBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryBuilder {
    public String GetIsUsedQuery(int id, DBTableColumn... tables)
    {
        List<String> selects = new ArrayList<>();
        for (DBTableColumn table : tables)
            selects.add(String.format("SELECT 1 FROM %1$s WHERE %2$s = %3$d", table.getTableName(), table, id));

        return String.format("SELECT CASE\n" +
                "WHEN EXISTS\n" +
                "(\n" +
                "   %s\n" +
                ")\n" +
                "THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT)\n" +
                "END", String.join("\nUNION ALL\n", selects));
    }

    public String GetQuery()
    {
        if(From == null)
            return null;
        StringBuilder builder = new StringBuilder(
                String.format(
                        "SELECT%s %s FROM %s",
                        Top.map(integer -> " TOP " + integer.toString()).orElse(""),
                        SelectColumns.isEmpty() ? "*" : SelectColumns.stream().map(Object::toString).collect(Collectors.joining(", ")),
                        From));
        if(!WhereConditions.isEmpty())
            builder.append(" WHERE " + WhereConditions.stream().map(Object::toString).collect(Collectors.joining(" AND ")));
        if(!OrderStatements.isEmpty())
            builder.append(" ORDER BY " + OrderStatements.stream().map(Object::toString).collect(Collectors.joining(", ")));
        return builder.toString();
    }

    public QueryBuilder Select(DBTableColumn... columns)
    {
        SelectColumns.addAll(Arrays.asList(columns));
        return this;
    }

    public QueryBuilder Top(int rows)
    {
        Top = Optional.of(rows);
        return this;
    }

    public QueryBuilder From(DBTable table)
    {
        From = table;
        return this;
    }

    public QueryBuilder Where(DBCondition... conditions)
    {
        WhereConditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public QueryBuilder OrderBy(DBTableColumn column)
    {
        OrderStatements.add(new DBOrderStatement(column, DBOrder.Asc));
        return this;
    }
    public QueryBuilder OrderByDescending(DBTableColumn column)
    {
        OrderStatements.add(new DBOrderStatement(column, DBOrder.Desc));
        return this;
    }

    protected final List<DBTableColumn> SelectColumns = new ArrayList<>();
    protected final List<DBCondition> WhereConditions = new ArrayList<>();
    protected final List<DBOrderStatement> OrderStatements = new ArrayList<>();
    protected DBTable From = null;
    protected Optional<Integer> Top = Optional.empty();
}
