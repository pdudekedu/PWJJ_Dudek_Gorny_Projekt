package WorkManager.Data.Models;

import WorkManager.Data.Enums.HistoryModificationType;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

public class EFValuesHistory {
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.ValuesHistory, "MODUSER");
    public static final DBTableColumn ModUserDisplayName = new DBTableColumn(DataContext.ValuesHistory, "MODUSERDISPLAYNAME");
    public static final DBTableColumn ModDate = new DBTableColumn(DataContext.ValuesHistory, "MODDATE");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.ValuesHistory, "MODCOMP");
    public static final DBTableColumn ModType = new DBTableColumn(DataContext.ValuesHistory, "MODTYPE");
    public static final DBTableColumn Area = new DBTableColumn(DataContext.ValuesHistory, "AREA");
    public static final DBTableColumn Element = new DBTableColumn(DataContext.ValuesHistory, "ELEMENT");
    public static final DBTableColumn Path = new DBTableColumn(DataContext.ValuesHistory, "PATH");
    public static final DBTableColumn OldValue = new DBTableColumn(DataContext.ValuesHistory, "OLDVALUE");
    public static final DBTableColumn NewValue = new DBTableColumn(DataContext.ValuesHistory, "NEWVALUE");

    public EFValuesHistory()
    {

    }
    public EFValuesHistory(ResultSet rs) throws SQLException {
        _ModUser = ModUser.getString(rs);
        _ModUserDisplayName = ModUserDisplayName.getString(rs);
        _ModDate = ModDate.getDate(rs);
        _ModComp = ModComp.getString(rs);
        _ModType = HistoryModificationType.valueOf(ModType.getByte(rs));
        _Area = Area.getString(rs);
        _Element = Element.getString(rs);
        _Path = Path.getString(rs);
        _OldValue = OldValue.getString(rs);
        _NewValue = NewValue.getString(rs);
    }

    @SerializedName("ModUser")
    private String _ModUser;
    @SerializedName("ModUserDisplayName")
    private String _ModUserDisplayName;
    @SerializedName("ModDate")
    private Date _ModDate;
    @SerializedName("ModComp")
    private String _ModComp;
    @SerializedName("ModType")
    private HistoryModificationType _ModType;
    @SerializedName("Area")
    private String _Area;
    @SerializedName("Element")
    private String _Element;
    @SerializedName("Path")
    private String _Path;
    @SerializedName("OldValue")
    private String _OldValue;
    @SerializedName("NewValue")
    private String _NewValue;

    public String getModUser() {
        return _ModUser;
    }

    public void setModUser(String modUser) {
        _ModUser = modUser;
    }

    public String getModUserDisplayName() {
        return _ModUserDisplayName;
    }

    public void setModUserDisplayName(String modUserDisplayName) {
        _ModUserDisplayName = modUserDisplayName;
    }

    public Date getModDate() {
        return _ModDate;
    }

    public void setModDate(Date modDate) {
        _ModDate = modDate;
    }

    public String getModComp() {
        return _ModComp;
    }

    public void setModComp(String modComp) {
        _ModComp = modComp;
    }

    public HistoryModificationType getModType() {
        return _ModType;
    }

    public void setModType(HistoryModificationType modType) {
        _ModType = modType;
    }

    public String getArea() {
        return _Area;
    }

    public void setArea(String area) {
        _Area = area;
    }

    public String getElement() {
        return _Element;
    }

    public void setElement(String element) {
        _Element = element;
    }

    public String getPath() {
        return _Path;
    }

    public void setPath(String path) {
        _Path = path;
    }

    public String getOldValue() {
        return _OldValue;
    }

    public void setOldValue(String oldValue) {
        _OldValue = oldValue;
    }

    public String getNewValue() {
        return _NewValue;
    }

    public void setNewValue(String newValue) {
        _NewValue = newValue;
    }
}
