package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;

import java.util.List;

public interface IUpdatable {
    int getId();
    DBSet[] getUpdateSets();
}
