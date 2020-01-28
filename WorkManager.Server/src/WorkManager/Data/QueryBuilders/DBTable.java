package WorkManager.Data.QueryBuilders;

public class DBTable {
    public DBTable(String tableName)
    {
        TableName = tableName;
    }
    public DBTable(String schemaName, String tableName)
    {
        this(tableName);
        SchemaName = schemaName;
    }
    protected String SchemaName = null;
    protected String TableName;

    public String getFullTableName()
    {
        if(SchemaName == null)
            return TableName;
        return String.format("%s.%s", SchemaName, TableName);
    }

    @Override
    public String toString()
    {
        if(SchemaName == null)
            return String.format("[%s]", TableName);
        return String.format("[%s].[%s]", SchemaName, TableName);
    }
}
