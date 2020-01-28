package WorkManager.Data.Models;

import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganizationModel extends EFExcludableModel {
    public OrganizationModel() { }
    public OrganizationModel(ResultSet rs) throws SQLException {
        super(rs);
        _Name = rs.getString("NAME");
        _Description = rs.getString("DESCRIPTION");
    }
    @SerializedName("Name")
    private String _Name = null;
    @SerializedName("Description")
    private String _Description = null;

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
}
