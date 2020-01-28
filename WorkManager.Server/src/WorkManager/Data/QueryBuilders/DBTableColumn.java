package WorkManager.Data.QueryBuilders;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBTableColumn {
    public DBTableColumn(DBTable table, String columnName)
    {
        Table = table;
        ColumnName = columnName;
    }

    private final SimpleDateFormat _DateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    private DBTable Table;
    private String ColumnName;

    public String getTableName() { return Table.toString(); }
    public String getColumnName() { return ColumnName; }

    public String getString(ResultSet rs) throws SQLException { return rs.getString(ColumnName); }
    public int getInt(ResultSet rs) throws SQLException { return rs.getInt(ColumnName); }
    public BigDecimal getDecimal(ResultSet rs) throws SQLException { return rs.getBigDecimal(ColumnName); }
    public byte getByte(ResultSet rs) throws SQLException { return rs.getByte(ColumnName); }
    public int[] getInts(ResultSet rs) throws SQLException
    {
        byte[] bytes = rs.getBytes(ColumnName);
        int[] result = new int[bytes.length];
        for (int i = 0; i < bytes.length; ++i)
            result[i] = bytes[i] & 0xFF;
        return result;
    }
    public boolean getBoolean(ResultSet rs) throws SQLException { return rs.getBoolean(ColumnName); }
    public Date getDate(ResultSet rs) throws SQLException
    {
        Timestamp ts = rs.getTimestamp(ColumnName);
        return ts != null ? new Date(ts.getTime()) : null;
    }

    public String of(String value)
    {
        return value != null ? String.format("N'%s'", value) : "NULL";
    }
    public String of(Integer value)
    {
        return value != null ? String.valueOf(value) : "NULL";
    }
    public String of(BigDecimal value)
    {
        return value != null ? String.valueOf(value).replace(',','.') : "NULL";
    }
    public String of(Byte value)
    {
        return value != null ? String.valueOf(value & 0xFF) : "NULL";
    }
    public String of(Boolean value)
    {
        return value != null ? (value ? "1" : "0") : "NULL";
    }
    public String of(Date value)
    {
        return value != null ? String.format("'%s'", _DateTimeFormat.format(value)) : "NULL";
    }
    public String of(int[] value)
    {
        if(value == null || value.length == 0)
            return "NULL";
        StringBuilder builder = new StringBuilder("0x");
        for (int i = 0; i < value.length; ++i)
            builder.append(String.format("%02X", value[i]));
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("%s.[%s]", Table, ColumnName);
    }
}
