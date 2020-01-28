package WorkManager.Data.QueryBuilders;

import WorkManager.Data.Models.IInsertable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateBuilder {

    public String GetQuery()
    {
        if(From == null)
            return null;
        StringBuilder builder = new StringBuilder(String.format("UPDATE %s SET %s",
                From,
                Sets.stream().map(Object::toString).collect(Collectors.joining(", "))));
        if(!WhereConditions.isEmpty())
            builder.append(" WHERE " + WhereConditions.stream().map(Object::toString).collect(Collectors.joining(" AND ")));
        return builder.toString();
    }

    public UpdateBuilder Where(DBCondition... conditions)
    {
        WhereConditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public UpdateBuilder Set(DBSet... sets)
    {
        Sets.addAll(Arrays.asList(sets));
        return this;
    }

    public UpdateBuilder From(DBTable table)
    {
        From = table;
        return this;
    }

    protected final List<DBCondition> WhereConditions = new ArrayList<>();
    protected final List<DBSet> Sets = new ArrayList<>();
    protected DBTable From = null;
}
