package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateBuilderSetOne {
    private final DBTable T = new DBTable("TABLE");
    private final UpdateBuilder UB = new UpdateBuilder().From(T).Set(new DBSet(new DBTableColumn(T, "COLUMN"), "1"));
    @Test
    void getQuery() {
        assertEquals("UPDATE [TABLE] SET COLUMN = 1", UB.GetQuery());
    }
}