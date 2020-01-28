package WorkManager.Data.QueryBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBSet {
    public DBSet(DBTableColumn column, String value)
    {
        Column = column;
        Value = value == null ? "NULL" : value;
    }

    private final SimpleDateFormat _DateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    protected DBTableColumn Column;
    protected String Value;

    @Override
    public String toString() {
        return String.format("%s = %s", Column.getColumnName(), Value);
    }
}

