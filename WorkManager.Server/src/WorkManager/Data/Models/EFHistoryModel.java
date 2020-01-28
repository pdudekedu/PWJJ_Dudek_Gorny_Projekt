package WorkManager.Data.Models;

import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EFHistoryModel extends EFModel{
    public EFHistoryModel() { }
    public EFHistoryModel(ResultSet rs) throws SQLException {
        super(rs);
        _ModUser = rs.getString("MODUSER");
        _ModComp = rs.getString("MODCOMP");
    }
    @SerializedName("ModUser")
    protected String _ModUser = null;
    @SerializedName("ModComp")
    protected String _ModComp = null;

    public String getModUser() {
        return _ModUser;
    }

    public void setModUser(String modUser) {
        _ModUser = modUser;
    }

    public String getModComp() {
        return _ModComp;
    }

    public void setModComp(String modComp) {
        _ModComp = modComp;
    }


}
