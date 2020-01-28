package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBuilderEmpty {
    private final UpdateBuilder UB = new UpdateBuilder();
    @Test
    void getQuery() {
        assertEquals(null, UB.GetQuery());
    }
}