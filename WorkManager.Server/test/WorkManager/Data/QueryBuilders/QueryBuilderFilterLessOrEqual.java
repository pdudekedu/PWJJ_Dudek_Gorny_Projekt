package WorkManager.Data.QueryBuilders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryBuilderFilterLessOrEqual {
    private final DBTable T = new DBTable("TABLE");
    private final QueryBuilder QB = new QueryBuilder().From(T).Where(new DBCondition(new DBTableColumn(T, "COLUMN"), DBConditionOperator.LessOrEqual, 0));
    @Test
    void getQuery() {
        assertEquals("SELECT * FROM [TABLE] WHERE [TABLE].[COLUMN] <= 0", QB.GetQuery());
    }
}