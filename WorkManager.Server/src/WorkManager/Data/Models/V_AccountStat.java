package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class V_AccountStat {
    public static final DBTableColumn DisplayName = new DBTableColumn(DataContext.V_AccountStat, "DISPLAYNAME");
    public static final DBTableColumn TaskCount = new DBTableColumn(DataContext.V_AccountStat, "TASKCOUNT");
    public static final DBTableColumn ProjectCount = new DBTableColumn(DataContext.V_AccountStat, "PROJECTCOUNT");
    public static final DBTableColumn WorkTime = new DBTableColumn(DataContext.V_AccountStat, "WORKTIME");
    public static final DBTableColumn EstimateWorkTime = new DBTableColumn(DataContext.V_AccountStat, "ESTIMATEWORKTIME");
    public static final DBTableColumn Punctuality = new DBTableColumn(DataContext.V_AccountStat, "PUNCTUALITY");
    public V_AccountStat()
    {

    }
    public V_AccountStat(ResultSet rs) throws SQLException {
        _DisplayName = DisplayName.getString(rs);
        _TaskCount = TaskCount.getInt(rs);
        _ProjectCount = ProjectCount.getInt(rs);
        _WorkTime = WorkTime.getDecimal(rs);
        _EstimateWorkTime = EstimateWorkTime.getDecimal(rs);
        _Punctuality = Punctuality.getDecimal(rs);
    }

    @SerializedName("DisplayName")
    private String _DisplayName;
    @SerializedName("TaskCount")
    private Integer _TaskCount;
    @SerializedName("ProjectCount")
    private Integer _ProjectCount;
    @SerializedName("WorkTime")
    private BigDecimal _WorkTime;
    @SerializedName("EstimateWorkTime")
    private BigDecimal _EstimateWorkTime;
    @SerializedName("Punctuality")
    private BigDecimal _Punctuality;

    public String getDisplayName() {
        return _DisplayName;
    }

    public void setDisplayName(String displayName) {
        _DisplayName = displayName;
    }

    public Integer getTaskCount() {
        return _TaskCount;
    }

    public void setTaskCount(Integer taskCount) {
        _TaskCount = taskCount;
    }

    public Integer getProjectCount() {
        return _ProjectCount;
    }

    public void setProjectCount(Integer projectCount) {
        _ProjectCount = projectCount;
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
