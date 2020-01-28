package WorkManager.Data.Models;

import WorkManager.Data.Enums.TaskState;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class V_Task extends EFModel {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.V_Tasks, "ID");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.V_Tasks, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.V_Tasks, "DESCRIPTION");
    public static final DBTableColumn ProjectId = new DBTableColumn(DataContext.V_Tasks, "PROJECTID");
    public static final DBTableColumn AccountId = new DBTableColumn(DataContext.V_Tasks, "ACCOUNTID");
    public static final DBTableColumn Account = new DBTableColumn(DataContext.V_Tasks, "ACCOUNT");
    public static final DBTableColumn State = new DBTableColumn(DataContext.V_Tasks, "STATE");
    public static final DBTableColumn EstimateStart = new DBTableColumn(DataContext.V_Tasks, "ESTIMATESTART");
    public static final DBTableColumn EstimateEnd = new DBTableColumn(DataContext.V_Tasks, "ESTIMATEEND");
    public V_Task()
    {

    }
    public V_Task(ResultSet rs) throws SQLException {
        super(rs);
        _Name = Name.getString(rs);
        _Description = Description.getString(rs);
        _ProjectId = ProjectId.getInt(rs);
        _AccountId = AccountId.getInt(rs);
        _Account = Account.getString(rs);
        _State = State.getInt(rs);
        _EstimateStart = EstimateStart.getDate(rs);
        _EstimateEnd = EstimateEnd.getDate(rs);
    }

    @SerializedName("Name")
    private String _Name;
    @SerializedName("Description")
    private String _Description;
    @SerializedName("ProjectId")
    private int _ProjectId;
    @SerializedName("AccountId")
    private Integer _AccountId;
    @SerializedName("Account")
    private String _Account;
    @SerializedName("State")
    private int _State;
    @SerializedName("EstimateStart")
    private Date _EstimateStart;
    @SerializedName("EstimateEnd")
    private Date _EstimateEnd;

    public String getName() {
        return _Name;
    }

    public void setName(String name) {
        _Name = name;
    }

    public String getDescription() {
        return _Description;
    }

    public void setDescription(String description) {
        _Description = description;
    }

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

    public String getAccount() {
        return _Account;
    }

    public void setAccount(String account) {
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
}
