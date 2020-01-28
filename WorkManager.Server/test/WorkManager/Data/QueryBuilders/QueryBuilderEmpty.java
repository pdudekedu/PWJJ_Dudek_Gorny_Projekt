package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderEmpty {
    private final QueryBuilder QB = new QueryBuilder();
    @Test
    void getQuery() {
        assertEquals(null, QB.GetQuery());
    }
}