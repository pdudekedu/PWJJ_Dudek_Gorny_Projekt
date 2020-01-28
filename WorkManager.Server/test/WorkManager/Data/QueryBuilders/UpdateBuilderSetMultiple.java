package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateBuilderSetMultiple {
    private final DBTable T = new DBTable("TABLE");
    private final UpdateBuilder UB = new UpdateBuilder().From(T)
            .Set(new DBSet(new DBTableColumn(T, "COLUMN1"), "1"),
                    new DBSet(new DBTableColumn(T, "COLUMN2"), "2"),
                    new DBSet(new DBTableColumn(T, "COLUMN3"), "3"));
    @Test
    void getQuery() {
        assertEquals("UPDATE [TABLE] SET COLUMN1 = 1, COLUMN2 = 2, COLUMN3 = 3", UB.GetQuery());
    }
}