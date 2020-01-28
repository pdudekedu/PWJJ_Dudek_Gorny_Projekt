package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Resource extends OrganizationModel implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.Projects, "ID");
    public static final DBTableColumn InUse = new DBTableColumn(DataContext.Projects, "INUSE");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.Projects, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.Projects, "MODCOMP");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.Projects, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.Projects, "DESCRIPTION");

    public Resource() { }
    public Resource(ResultSet rs) throws SQLException {
        super(rs);
    }

    @Override
    public String[] getInsertColumns() {
        return new String[] { Name.getColumnName(), Description.getColumnName(), InUse.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { Name.of(getName()), Description.of(getDescription()), InUse.of(getInUse()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
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
}
