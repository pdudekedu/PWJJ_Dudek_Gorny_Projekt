package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderSelectAll {
    private final QueryBuilder QB = new QueryBuilder().From(new DBTable("TABLE"));
    @Test
    void getQuery() {
        assertEquals("SELECT * FROM [TABLE]", QB.GetQuery());
    }
}