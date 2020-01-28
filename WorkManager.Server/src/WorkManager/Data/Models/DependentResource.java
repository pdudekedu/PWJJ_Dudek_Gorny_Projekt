package WorkManager.Data.Models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DependentResource extends EFHistoryModel
{
    public DependentResource() { }
    public DependentResource(ResultSet rs) throws SQLException {
        super(rs);
        _ResourceId = rs.getInt("RESOURCEID");
        _Count = rs.getBigDecimal("COUNT");
    }
    @SerializedName("ResourceId")
    private int _ResourceId;
    @SerializedName("Resource")
    private AssignableModel _Resource;
    @SerializedName("Count")
    private BigDecimal _Count;

    public int getResourceId() {
        return _ResourceId;
    }

    public void setResourceId(int resourceId) {
        _ResourceId = resourceId;
    }

    public AssignableModel getResource() {
        return _Resource;
    }

    public void setResource(AssignableModel resource) {
        _Resource = resource;
    }

    public BigDecimal getCount() {
        return _Count;
    }

    public void setCount(BigDecimal count) {
        _Count = count;
    }
}
