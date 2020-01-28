package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamAccount extends EFHistoryModel implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.TeamAccounts, "ID");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.TeamAccounts, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.TeamAccounts, "MODCOMP");
    public static final DBTableColumn AccountId = new DBTableColumn(DataContext.TeamAccounts, "ACCOUNTID");
    public static final DBTableColumn TeamId = new DBTableColumn(DataContext.TeamAccounts, "TEAMID");

    @Override
    public String[] getInsertColumns() {
        return new String[] { AccountId.getColumnName(), TeamId.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { AccountId.of(getAccountId()), TeamId.of(getTeamId()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
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

    public TeamAccount() { }
    public TeamAccount(ResultSet rs) throws SQLException {
        super(rs);
        _AccountId = AccountId.getInt(rs);
        _TeamId = TeamId.getInt(rs);
    }
    @SerializedName("AccountId")
    private int _AccountId;
    @SerializedName("TeamId")
    private int _TeamId;
    @SerializedName("Account")
    private AssignableModel Account;

    public int getAccountId() {
        return _AccountId;
    }

    public void setAccountId(int accountId) {
        _AccountId = accountId;
    }

    public int getTeamId() {
        return _TeamId;
    }

    public void setTeamId(int teamId) {
        _TeamId = teamId;
    }

    public AssignableModel getAccount() {
        return Account;
    }

    public void setAccount(AssignableModel account) {
        Account = account;
    }
}
