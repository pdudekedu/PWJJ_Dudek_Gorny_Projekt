package WorkManager.Data.Models;

public interface IInsertable {
    String[] getInsertColumns();
    String[] getInsertValues();
}
