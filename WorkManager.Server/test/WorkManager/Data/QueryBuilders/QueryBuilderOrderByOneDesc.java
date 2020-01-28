package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryBuilderOrderByOneDesc {
    private final DBTable T = new DBTable("TABLE");
    private final QueryBuilder QB = new QueryBuilder().From(T).OrderByDescending(new DBTableColumn(T, "COLUMN"));
    @Test
    void getQuery() {
        assertEquals("SELECT * FROM [TABLE] ORDER BY [TABLE].[COLUMN] DESC", QB.GetQuery());
    }
}