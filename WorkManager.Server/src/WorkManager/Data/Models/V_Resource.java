package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class V_Resource extends EFModel {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.V_Resources, "ID");
    public static final DBTableColumn InUse = new DBTableColumn(DataContext.V_Resources, "INUSE");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.V_Resources, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.V_Resources, "DESCRIPTION");
    public V_Resource()
    {

    }
    public V_Resource(ResultSet rs) throws SQLException {
        super(rs);
        _Name = Name.getString(rs);
        _Description = Description.getString(rs);
        _InUse = InUse.getBoolean(rs);
    }

    @SerializedName("Name")
    private String _Name;
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
