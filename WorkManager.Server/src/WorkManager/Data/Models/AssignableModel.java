package WorkManager.Data.Models;

import WorkManager.Data.Enums.HistoryModificationType;
import WorkManager.Data.Enums.TaskState;
import WorkManager.Data.Enums.TimeType;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AssignableModel extends EFModel {
    public AssignableModel() { }
    public AssignableModel(ResultSet rs) throws SQLException {
        super(rs);
        _Name = rs.getString("NAME");
    }
    @SerializedName("Name")
    private String _Name;

    public String getName() {
        return _Name;
    }

    public void setName(String name) {
        _Name = name;
    }
}

