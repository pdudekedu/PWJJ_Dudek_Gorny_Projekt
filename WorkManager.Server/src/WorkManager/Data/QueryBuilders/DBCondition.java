package WorkManager.Data.QueryBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBCondition {
    public DBCondition(DBTableColumn column, DBConditionOperator operator)
    {
        Column = column;
        Operator = operator;
    }
    public DBCondition(DBTableColumn column, DBConditionOperator operator, Date value)
    {
        this(column, operator);
        Value = String.format("'%s'", _DateTimeFormat.format(value));
    }
    public DBCondition(DBTableColumn column, DBConditionOperator operator, boolean value)
    {
        this(column, operator);
        Value = value ? "1" : "0";
    }
    public DBCondition(DBTableColumn column, DBConditionOperator operator, int value)
    {
        this(column, operator);
        Value = String.valueOf(value);
    }
    public DBCondition(DBTableColumn column, DBConditionOperator operator, String value)
    {
        this(column, operator);
        Value = "N'" + value + "'";
    }

    private final SimpleDateFormat _DateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    protected DBTableColumn Column;
    protected DBConditionOperator Operator;
    protected String Value;

    protected String GetValue()
    {
        switch (Operator)
        {
            case Equal:
                return String.format("= %s", Value);
            case Greater:
                return String.format("> %s", Value);
            case GreaterOrEqual:
                return String.format(">= %s", Value);
            case In:
                return String.format("IN (%s)", Value);
            case IsNotNull:
                return "IS NOT NULL";
            case IsNull:
                return "IS NULL";
            case Less:
                return String.format("< %s", Value);
            case LessOrEqual:
                return String.format("<= %s", Value);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s %s", Column, GetValue());
    }
}

