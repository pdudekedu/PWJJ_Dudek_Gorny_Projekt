package WorkManager.Data.Models;

import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EFExcludableModel extends EFHistoryModel{
    public EFExcludableModel() { }
    public EFExcludableModel(ResultSet rs) throws SQLException {
        super(rs);
        _InUse = rs.getBoolean("INUSE");
    }
    @SerializedName("InUse")
    protected boolean _InUse = true;

    public boolean getInUse() {
        return _InUse;
    }

    public void setInUse(boolean inUse) {
        _InUse = inUse;
    }
}
