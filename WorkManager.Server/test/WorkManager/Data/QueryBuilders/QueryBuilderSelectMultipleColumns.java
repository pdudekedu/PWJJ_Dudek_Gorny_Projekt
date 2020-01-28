package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryBuilderSelectMultipleColumns {
    private final DBTable T = new DBTable("TABLE");
    private final DBTableColumn C1 = new DBTableColumn(T, "COLUMN1");
    private final DBTableColumn C2 = new DBTableColumn(T, "COLUMN2");
    private final DBTableColumn C3 = new DBTableColumn(T, "COLUMN3");
    private final QueryBuilder QB = new QueryBuilder().From(T).Select(C1, C2, C3);
    @Test
    void getQuery() {
        assertEquals("SELECT [TABLE].[COLUMN1], [TABLE].[COLUMN2], [TABLE].[COLUMN3] FROM [TABLE]", QB.GetQuery());
    }
}