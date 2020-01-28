package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskResource extends DependentResource implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.TaskResources, "ID");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.TaskResources, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.TaskResources, "MODCOMP");
    public static final DBTableColumn ResourceId = new DBTableColumn(DataContext.TaskResources, "RESOURCEID");
    public static final DBTableColumn Count = new DBTableColumn(DataContext.TaskResources, "COUNT");
    public static final DBTableColumn TaskId = new DBTableColumn(DataContext.TaskResources, "TASKID");

    @Override
    public String[] getInsertColumns() {
        return new String[] { ResourceId.getColumnName(), Count.getColumnName(), TaskId.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { ResourceId.of(getResourceId()), Count.of(getCount()), TaskId.of(getTaskId()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
    }

    @Override
    public DBSet[] getUpdateSets() {
        String[] columns = getInsertColumns();
        String[] values = getInsertValues();
        DBSet[] result = new DBSet[columns.length];
        for(int i = 0; i < columns.length; ++i)
            result[i] = new DBSet(new DBTableColumn(null, columns[i]), values[i]);
        return result;
    }

    public TaskResource() { }
    public TaskResource(ResultSet rs) throws SQLException {
        super(rs);
        _TaskId = TaskId.getInt(rs);
    }
    @SerializedName("TaskId")
    private int _TaskId;
    @SerializedName("Task")
    private Task _Task;

    public int getTaskId() {
        return _TaskId;
    }

    public void setTaskId(int taskId) {
        _TaskId = taskId;
    }

    public WorkManager.Data.Models.Task getTask() {
        return _Task;
    }

    public void setTask(WorkManager.Data.Models.Task task) {
        _Task = task;
    }
}
