package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EFAccount extends EFExcludableModel implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.Accounts, "ID");
    public static final DBTableColumn InUse = new DBTableColumn(DataContext.Accounts, "INUSE");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.Accounts, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.Accounts, "MODCOMP");
    public static final DBTableColumn UserName = new DBTableColumn(DataContext.Accounts, "USERNAME");
    public static final DBTableColumn Password = new DBTableColumn(DataContext.Accounts, "PASSWORD");
    public static final DBTableColumn Permissions = new DBTableColumn(DataContext.Accounts, "PERMISSIONS");
    public static final DBTableColumn Type = new DBTableColumn(DataContext.Accounts, "TYPE");
    public static final DBTableColumn Title = new DBTableColumn(DataContext.Accounts, "TITLE");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.Accounts, "NAME");
    public static final DBTableColumn Surname = new DBTableColumn(DataContext.Accounts, "SURNAME");
    public static final DBTableColumn IsLocked = new DBTableColumn(DataContext.Accounts, "ISLOCKED");

    @Override
    public String[] getInsertColumns() {
        return new String[] { UserName.getColumnName(), Password.getColumnName(), Permissions.getColumnName(), Type.getColumnName(), Title.getColumnName(), Name.getColumnName(), Surname.getColumnName(), IsLocked.getColumnName(), InUse.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { UserName.of(getUserName()), Password.of(getPassword()), Permissions.of(getPermissions()), Type.of(getType()), Title.of(getTitle()), Name.of(getName()), Surname.of(getSurname()), IsLocked.of(_IsLocked), InUse.of(getInUse()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
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

    public EFAccount()
    {

    }
    public EFAccount(ResultSet rs) throws SQLException {
        super(rs);
        _UserName = UserName.getString(rs);
        _Password = Password.getString(rs);
        _Permissions = Permissions.getInts(rs);
        _Type = Type.getByte(rs);
        _Title = Title.getString(rs);
        _Name = Name.getString(rs);
        _Surname = Surname.getString(rs);
        _IsLocked = IsLocked.getBoolean(rs);
    }

    @SerializedName("UserName")
    private String _UserName;
    @SerializedName("Password")
    private String _Password;
    @SerializedName("Permissions")
    private int[] _Permissions;
    @SerializedName("Type")
    private byte _Type;
    @SerializedName("Title")
    private String _Title;
    @SerializedName("Name")
    private String _Name;
    @SerializedName("Surname")
    private String _Surname;
    @SerializedName("IsLocked")
    private boolean _IsLocked;

    public String getUserName() {
        return _UserName;
    }

    public void setUserName(String userName) {
        _UserName = userName;
    }

    public String getPassword() {
        return _Password;
    }

    public void setPassword(String password) {
        _Password = password;
    }

    public int[] getPermissions() {
        return _Permissions;
    }

    public void setPermissions(int[] permissions) {
        _Permissions = permissions;
    }

    public byte getType() {
        return _Type;
    }

    public void setType(byte type) {
        _Type = type;
    }

    public String getTitle() {
        return _Title;
    }

    public void setTitle(String title) {
        _Title = title;
    }

    public String getName() {
        return _Name;
    }

    public void setName(String name) {
        _Name = name;
    }

    public String getSurname() {
        return _Surname;
    }

    public void setSurname(String surname) {
        _Surname = surname;
    }

    public boolean isLocked() {
        return _IsLocked;
    }

    public void setLocked(boolean locked) {
        _IsLocked = locked;
    }
}
