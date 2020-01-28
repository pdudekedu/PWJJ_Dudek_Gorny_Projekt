package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryBuilderOrderByOneAsc {
    private final DBTable T = new DBTable("TABLE");
    private final QueryBuilder QB = new QueryBuilder().From(T).OrderBy(new DBTableColumn(T, "COLUMN"));
    @Test
    void getQuery() {
        assertEquals("SELECT * FROM [TABLE] ORDER BY [TABLE].[COLUMN] ASC", QB.GetQuery());
    }
}