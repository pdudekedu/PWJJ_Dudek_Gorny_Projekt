package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Team extends OrganizationModel implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.Teams, "ID");
    public static final DBTableColumn InUse = new DBTableColumn(DataContext.Teams, "INUSE");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.Teams, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.Teams, "MODCOMP");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.Teams, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.Teams, "DESCRIPTION");

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

    public Team() { }
    public Team(ResultSet rs) throws SQLException {
        super(rs);
    }
    @SerializedName("AccountTeams")
    private List<TeamAccount> AccountTeams = new ArrayList<>();
    @SerializedName("AssignedAccounts")
    private List<AssignableModel> AssignedAccounts = new ArrayList<>();
    @SerializedName("AvailableAccounts")
    private List<AssignableModel> AvailableAccounts = new ArrayList<>();

    public List<TeamAccount> getAccountTeams() {
        return AccountTeams;
    }

    public void setAccountTeams(List<TeamAccount> accountTeams) {
        AccountTeams = accountTeams;
    }

    public List<AssignableModel> getAssignedAccounts() {
        return AssignedAccounts;
    }

    public void setAssignedAccounts(List<AssignableModel> assignedAccounts) {
        AssignedAccounts = assignedAccounts;
    }

    public List<AssignableModel> getAvailableAccounts() {
        return AvailableAccounts;
    }

    public void setAvailableAccounts(List<AssignableModel> availableAccounts) {
        AvailableAccounts = availableAccounts;
    }

    public void AssignAccountTeams(List<TeamAccount> teamAccounts, List<AssignableModel> accounts)
    {
        AccountTeams = teamAccounts;
        AssignedAccounts = accounts.stream().filter(x -> AccountTeams.stream().anyMatch(z -> z.getAccountId() == x.getId())).collect(Collectors.toList());
        for (AssignableModel account : AssignedAccounts)
            AccountTeams.stream().filter(x -> x.getAccountId() == account.getId()).findFirst().get().setAccount(account);
        AvailableAccounts = accounts.stream().filter(x -> !AssignedAccounts.contains(x)).collect(Collectors.toList());
    }
}
