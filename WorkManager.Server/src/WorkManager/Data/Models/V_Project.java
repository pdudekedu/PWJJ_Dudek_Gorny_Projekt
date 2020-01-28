package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class V_Project extends EFModel {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.V_Projects, "ID");
    public static final DBTableColumn InUse = new DBTableColumn(DataContext.V_Projects, "INUSE");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.V_Projects, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.V_Projects, "DESCRIPTION");
    public static final DBTableColumn Team = new DBTableColumn(DataContext.V_Projects, "TEAM");
    public V_Project()
    {

    }
    public V_Project(ResultSet rs) throws SQLException {
        super(rs);
        _Name = Name.getString(rs);
        _Description = Description.getString(rs);
        _Team = Team.getString(rs);
        _InUse = InUse.getBoolean(rs);
    }

    @SerializedName("Name")
    private String _Name;
    @SerializedName("Team")
    private String _Team;
    @SerializedName("Description")
    private String _Description;
    @SerializedName("InUse")
    private boolean _InUse;

    public String getName() {
        return _Name;
    }

    public void setName(String name) {
        _Name = name;
    }

    public String getTeam() {
        return _Team;
    }

    public void setTeam(String team) {
        _Team = team;
    }

    public String getDescription() {
        return _Description;
    }

    public void setDescription(String description) {
        _Description = description;
    }

    public boolean isInUse() {
        return _InUse;
    }

    public void setInUse(boolean inUse) {
        _InUse = inUse;
    }
}
