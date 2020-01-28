package WorkManager.Data.QueryBuilders;

public class DBOrderStatement {
    public DBOrderStatement(DBTableColumn column, DBOrder order)
    {
        Column = column;
        Order = order;
    }
    private DBTableColumn Column;
    private DBOrder Order;

    @Override
    public String toString() {
        switch (Order)
        {
            case Asc:
                return String.format("%s ASC", Column);
            case Desc:
                return String.format("%s DESC", Column);
        }
        return null;
    }
}
