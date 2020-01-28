package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryBuilderOrderByMultiple {
    private final DBTable T = new DBTable("TABLE");
    private final QueryBuilder QB = new QueryBuilder().From(T)
            .OrderBy(new DBTableColumn(T, "COLUMN1"))
            .OrderByDescending(new DBTableColumn(T, "COLUMN2"))
            .OrderBy(new DBTableColumn(T, "COLUMN3"));
    @Test
    void getQuery() {
        assertEquals("SELECT * FROM [TABLE] ORDER BY [TABLE].[COLUMN1] ASC, [TABLE].[COLUMN2] DESC, [TABLE].[COLUMN3] ASC", QB.GetQuery());
    }
}