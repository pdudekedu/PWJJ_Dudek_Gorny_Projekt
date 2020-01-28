package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class V_ProjectStat {
    public static final DBTableColumn ProjectId = new DBTableColumn(DataContext.V_ProjectsStat, "PROJECTID");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.V_ProjectsStat, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.V_ProjectsStat, "DESCRIPTION");
    public static final DBTableColumn New = new DBTableColumn(DataContext.V_ProjectsStat, "NEW");
    public static final DBTableColumn Active = new DBTableColumn(DataContext.V_ProjectsStat, "ACTIVE");
    public static final DBTableColumn Suspend = new DBTableColumn(DataContext.V_ProjectsStat, "SUSPEND");
    public static final DBTableColumn Complete = new DBTableColumn(DataContext.V_ProjectsStat, "COMPLETE");
    public static final DBTableColumn State = new DBTableColumn(DataContext.V_ProjectsStat, "STATE");
    public static final DBTableColumn Team = new DBTableColumn(DataContext.V_ProjectsStat, "TEAM");
    public static final DBTableColumn WorkTime = new DBTableColumn(DataContext.V_ProjectsStat, "WORKTIME");
    public static final DBTableColumn EstimateWorkTime = new DBTableColumn(DataContext.V_ProjectsStat, "ESTIMATEWORKTIME");
    public static final DBTableColumn Punctuality = new DBTableColumn(DataContext.V_ProjectsStat, "PUNCTUALITY");
    public V_ProjectStat()
    {

    }
    public V_ProjectStat(ResultSet rs) throws SQLException {
        _ProjectId = ProjectId.getInt(rs);
        _Name = Name.getString(rs);
        _Description = Description.getString(rs);
        _New = New.getInt(rs);
        _Active = Active.getInt(rs);
        _Suspend = Suspend.getInt(rs);
        _Complete = Complete.getInt(rs);
        _State = State.getInt(rs);
        _Team = Team.getString(rs);
        _WorkTime = WorkTime.getDecimal(rs);
        _EstimateWorkTime = EstimateWorkTime.getDecimal(rs);
        _Punctuality = Punctuality.getDecimal(rs);
    }

    @SerializedName("ProjectId")
    private int _ProjectId;
    @SerializedName("Name")
    private String _Name;
    @SerializedName("Description")
    private String _Description;
    @SerializedName("New")
    private int _New;
    @SerializedName("Active")
    private int _Active;
    @SerializedName("Suspend")
    private int _Suspend;
    @SerializedName("Complete")
    private int _Complete;
    @SerializedName("State")
    private int _State;
    @SerializedName("Team")
    private String _Team;
    @SerializedName("WorkTime")
    private BigDecimal _WorkTime;
    @SerializedName("EstimateWorkTime")
    private BigDecimal _EstimateWorkTime;
    @SerializedName("Punctuality")
    private BigDecimal _Punctuality;

    public int getProjectId() {
        return _ProjectId;
    }

    public void setProjectId(int projectId) {
        _ProjectId = projectId;
    }

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

    public int getNew() {
        return _New;
    }

    public void setNew(int aNew) {
        _New = aNew;
    }

    public int getActive() {
        return _Active;
    }

    public void setActive(int active) {
        _Active = active;
    }

    public int getSuspend() {
        return _Suspend;
    }

    public void setSuspend(int suspend) {
        _Suspend = suspend;
    }

    public int getComplete() {
        return _Complete;
    }

    public void setComplete(int complete) {
        _Complete = complete;
    }

    public int getState() {
        return _State;
    }

    public void setState(int state) {
        _State = state;
    }

    public String getTeam() {
        return _Team;
    }

    public void setTeam(String team) {
        _Team = team;
    }

    public BigDecimal getWorkTime() {
        return _WorkTime;
    }

    public void setWorkTime(BigDecimal workTime) {
        _WorkTime = workTime;
    }

    public BigDecimal getEstimateWorkTime() {
        return _EstimateWorkTime;
    }

    public void setEstimateWorkTime(BigDecimal estimateWorkTime) {
        _EstimateWorkTime = estimateWorkTime;
    }

    public BigDecimal getPunctuality() {
        return _Punctuality;
    }

    public void setPunctuality(BigDecimal punctuality) {
        _Punctuality = punctuality;
    }
}
