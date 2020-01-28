package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EFAccountView extends EFModel {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.Accounts, "ID");
    public static final DBTableColumn UserName = new DBTableColumn(DataContext.Accounts, "USERNAME");
    public static final DBTableColumn Type = new DBTableColumn(DataContext.Accounts, "TYPE");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.Accounts, "NAME");
    public static final DBTableColumn Surname = new DBTableColumn(DataContext.Accounts, "SURNAME");

    public EFAccountView()
    {

    }
    public EFAccountView(ResultSet rs) throws SQLException {
        super(rs);
        _UserName = UserName.getString(rs);
        _Type = Type.getByte(rs);
        _Name = Name.getString(rs);
        _Surname = Surname.getString(rs);
    }

    @SerializedName("UserName")
    private String _UserName;
    @SerializedName("InUse")
    private boolean _InUse;
    @SerializedName("Name")
    private String _Name;
    @SerializedName("Surname")
    private String _Surname;
    @SerializedName("Type")
    private int _Type;

    public String getUserName() {
        return _UserName;
    }

    public void setUserName(String userName) {
        _UserName = userName;
    }

    public boolean isInUse() {
        return _InUse;
    }

    public void setInUse(boolean inUse) {
        _InUse = inUse;
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

    public int getType() {
        return _Type;
    }

    public void setType(int type) {
        _Type = type;
    }
}
