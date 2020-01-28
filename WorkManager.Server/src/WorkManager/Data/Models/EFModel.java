package WorkManager.Data.Models;

import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EFModel {
    public EFModel() { }
    public EFModel(ResultSet rs) throws SQLException {
        _Id = rs.getInt("ID");
    }
    public int getId() {
        return _Id;
    }

    public void setId(int id) {
        _Id = id;
    }

    @SerializedName("Id")
    protected int _Id = 0;
}
