package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class TaskTime extends EFHistoryModel implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.TaskTimes, "ID");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.TaskTimes, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.TaskTimes, "MODCOMP");
    public static final DBTableColumn Time = new DBTableColumn(DataContext.TaskTimes, "TIME");
    public static final DBTableColumn Type = new DBTableColumn(DataContext.TaskTimes, "TYPE");
    public static final DBTableColumn TaskId = new DBTableColumn(DataContext.TaskTimes, "TASKID");

    @Override
    public String[] getInsertColumns() {
        return new String[] { Time.getColumnName(), Type.getColumnName(), TaskId.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { Time.of(getTime()), Type.of(getType()), TaskId.of(getTaskId()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
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

    public TaskTime() { }
    public TaskTime(ResultSet rs) throws SQLException {
        super(rs);
        _Time = Time.getDate(rs);
        _Type = Type.getInt(rs);
        _TaskId = TaskId.getInt(rs);
    }
    @SerializedName("Time")
    private Date _Time;
    @SerializedName("Type")
    private int _Type;
    @SerializedName("TaskId")
    private int _TaskId;
    @SerializedName("Task")
    private Task _Task;

    public Date getTime() {
        return _Time;
    }

    public void setTime(Date time) {
        _Time = time;
    }

    public int getType() {
        return _Type;
    }

    public void setType(int type) {
        _Type = type;
    }

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
