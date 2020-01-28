package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task extends OrganizationModel implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.Tasks, "ID");
    public static final DBTableColumn InUse = new DBTableColumn(DataContext.Tasks, "INUSE");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.Tasks, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.Tasks, "MODCOMP");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.Tasks, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.Tasks, "DESCRIPTION");
    public static final DBTableColumn ProjectId = new DBTableColumn(DataContext.Tasks, "PROJECTID");
    public static final DBTableColumn AccountId = new DBTableColumn(DataContext.Tasks, "ACCOUNTID");
    public static final DBTableColumn State = new DBTableColumn(DataContext.Tasks, "STATE");
    public static final DBTableColumn EstimateStart = new DBTableColumn(DataContext.Tasks, "ESTIMATESTART");
    public static final DBTableColumn EstimateEnd = new DBTableColumn(DataContext.Tasks, "ESTIMATEEND");

    @Override
    public String[] getInsertColumns() {
        return new String[] { ProjectId.getColumnName(), AccountId.getColumnName(), State.getColumnName(), EstimateStart.getColumnName(), EstimateEnd.getColumnName(), Name.getColumnName(), Description.getColumnName(), InUse.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { ProjectId.of(getProjectId()), AccountId.of(getAccountId()), State.of(getState()), EstimateStart.of(getEstimateStart()), EstimateEnd.of(getEstimateEnd()), Name.of(getName()), Description.of(getDescription()), InUse.of(getInUse()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
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

    public Task() { }
    public Task(ResultSet rs) throws SQLException {
        super(rs);
        _ProjectId = ProjectId.getInt(rs);
        _AccountId = AccountId.getInt(rs);
        _State = State.getInt(rs);
        _EstimateStart = EstimateStart.getDate(rs);
        _EstimateEnd = EstimateEnd.getDate(rs);
    }

    @SerializedName("ProjectId")
    private int _ProjectId;
    @SerializedName("AccountId")
    private Integer _AccountId = null;
    @SerializedName("Account")
    private EFAccount _Account;
    @SerializedName("State")
    private int _State;
    @SerializedName("EstimateStart")
    private Date _EstimateStart;
    @SerializedName("EstimateEnd")
    private Date _EstimateEnd;
    @SerializedName("TaskTimes")
    private List<TaskTime> TaskTimes = new ArrayList<>();
    @SerializedName("ResourceForTask")
    private List<TaskResource> ResourceForTask= new ArrayList<>();
    @SerializedName("AssignedResources")
    private List<AssignableModel> AssignedResources= new ArrayList<>();
    @SerializedName("AvailableResources")
    private List<AssignableModel> AvailableResources= new ArrayList<>();

    public int getProjectId() {
        return _ProjectId;
    }

    public void setProjectId(int projectId) {
        _ProjectId = projectId;
    }

    public Integer getAccountId() {
        return _AccountId;
    }

    public void setAccountId(Integer accountId) {
        _AccountId = accountId;
    }

    public EFAccount getAccount() {
        return _Account;
    }

    public void setAccount(EFAccount account) {
        _Account = account;
    }

    public int getState() {
        return _State;
    }

    public void setState(int state) {
        _State = state;
    }

    public Date getEstimateStart() {
        return _EstimateStart;
    }

    public void setEstimateStart(Date estimateStart) {
        _EstimateStart = estimateStart;
    }

    public Date getEstimateEnd() {
        return _EstimateEnd;
    }

    public void setEstimateEnd(Date estimateEnd) {
        _EstimateEnd = estimateEnd;
    }

    public List<TaskTime> getTaskTimes() {
        return TaskTimes;
    }

    public void setTaskTimes(List<TaskTime> taskTimes) {
        TaskTimes = taskTimes;
    }

    public List<TaskResource> getResourceForTask() {
        return ResourceForTask;
    }

    public void setResourceForTask(List<TaskResource> resourceForTask) {
        ResourceForTask = resourceForTask;
    }

    public List<AssignableModel> getAssignedResources() {
        return AssignedResources;
    }

    public void setAssignedResources(List<AssignableModel> assignedResources) {
        AssignedResources = assignedResources;
    }

    public List<AssignableModel> getAvailableResources() {
        return AvailableResources;
    }

    public void setAvailableResources(List<AssignableModel> availableResources) {
        AvailableResources = availableResources;
    }
}
