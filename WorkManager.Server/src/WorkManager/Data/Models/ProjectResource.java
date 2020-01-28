package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectResource extends DependentResource implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.ProjectResources, "ID");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.ProjectResources, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.ProjectResources, "MODCOMP");
    public static final DBTableColumn ResourceId = new DBTableColumn(DataContext.ProjectResources, "RESOURCEID");
    public static final DBTableColumn Count = new DBTableColumn(DataContext.ProjectResources, "COUNT");
    public static final DBTableColumn ProjectId = new DBTableColumn(DataContext.ProjectResources, "PROJECTID");

    @Override
    public String[] getInsertColumns() {
        return new String[] { ResourceId.getColumnName(), Count.getColumnName(), ProjectId.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { ResourceId.of(getResourceId()), Count.of(getCount()), ProjectId.of(getProjectId()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
    }

    @Override
    public DBSet[] getUpdateSets() {
        String[] columns = getInsertColumns();
        String[] values = getInsertValues();
        DBSet[] result = new DBSet[columns.length];
        for(int i = 0; i < columns.length; ++i)
            result[i] = new DBSet(new DBTableColumn(null, columns[i]), values[i]);
        return result;
    }

    public ProjectResource() { }
    public ProjectResource(ResultSet rs) throws SQLException {
        super(rs);
        _ProjectId = ProjectId.getInt(rs);
    }
    @SerializedName("ProjectId")
    private int _ProjectId;

    public int getProjectId() {
        return _ProjectId;
    }

    public void setProjectId(int projectId) {
        _ProjectId = projectId;
    }
}
