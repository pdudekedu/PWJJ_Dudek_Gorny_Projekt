package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderSelectOneColumn {
    private final DBTable T = new DBTable("TABLE");
    private final QueryBuilder QB = new QueryBuilder().From(T).Select(new DBTableColumn(T, "COLUMN"));
    @Test
    void getQuery() {
        assertEquals("SELECT [TABLE].[COLUMN] FROM [TABLE]", QB.GetQuery());
    }
}