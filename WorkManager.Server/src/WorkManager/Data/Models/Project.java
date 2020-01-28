package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBSet;
import WorkManager.Data.QueryBuilders.DBTableColumn;
import com.google.gson.annotations.SerializedName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Project extends OrganizationModel implements IInsertable, IUpdatable {
    public static final DBTableColumn Id = new DBTableColumn(DataContext.Projects, "ID");
    public static final DBTableColumn InUse = new DBTableColumn(DataContext.Projects, "INUSE");
    public static final DBTableColumn ModUser = new DBTableColumn(DataContext.Projects, "MODUSER");
    public static final DBTableColumn ModComp = new DBTableColumn(DataContext.Projects, "MODCOMP");
    public static final DBTableColumn Name = new DBTableColumn(DataContext.Projects, "NAME");
    public static final DBTableColumn Description = new DBTableColumn(DataContext.Projects, "DESCRIPTION");
    public static final DBTableColumn TeamId = new DBTableColumn(DataContext.Projects, "TEAMID");

    @Override
    public String[] getInsertColumns() {
        return new String[] { TeamId.getColumnName(), Name.getColumnName(), Description.getColumnName(), InUse.getColumnName(), ModUser.getColumnName(), ModComp.getColumnName() };
    }

    @Override
    public String[] getInsertValues() {
        return new String[] { TeamId.of(getTeamId()), Name.of(getName()), Description.of(getDescription()), InUse.of(getInUse()), ModUser.of(getModUser()), ModComp.of(getModComp()) };
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

    public Project() { }
    public Project(ResultSet rs) throws SQLException {
        super(rs);
        _TeamId = TeamId.getInt(rs);
    }

    @SerializedName("TeamId")
    private int _TeamId;
    @SerializedName("Team")
    private Team Team;
    @SerializedName("ResourceForProject")
    private List<ProjectResource> ResourceForProject = new ArrayList<>();
    @SerializedName("AssignedResources")
    private List<AssignableModel> AssignedResources = new ArrayList<>();
    @SerializedName("AvailableResources")
    private List<AssignableModel> AvailableResources = new ArrayList<>();
    @SerializedName("AvailableTeams")
    private List<AssignableModel> AvailableTeams = new ArrayList<>();

    public int getTeamId() {
        return _TeamId;
    }

    public void setTeamId(int teamId) {
        _TeamId = teamId;
    }

    public WorkManager.Data.Models.Team getTeam() {
        return Team;
    }

    public void setTeam(WorkManager.Data.Models.Team team) {
        Team = team;
    }

    public List<ProjectResource> getResourceForProject() {
        return ResourceForProject;
    }

    public void setResourceForProject(List<ProjectResource> resourceForProject) {
        ResourceForProject = resourceForProject;
    }

    public List<AssignableModel> getAssignedResources() {
        return AssignedResources;
    }

    public void setAssignedResources(List<AssignableModel> assignedResources) {
        AssignedResources = assignedResources;
    }

    public List<AssignableModel> getAvailableResources() {
        return AvailableResources;
    }

    public void setAvailableResources(List<AssignableModel> availableResources) {
        AvailableResources = availableResources;
    }

    public List<AssignableModel> getAvailableTeams() {
        return AvailableTeams;
    }

    public void setAvailableTeams(List<AssignableModel> availableTeams) {
        AvailableTeams = availableTeams;
    }

    public void AssignResources(List<ProjectResource> projectResources, List<AssignableModel> resources, List<AssignableModel> teams)
    {
        ResourceForProject = projectResources;
        AssignedResources = resources.stream().filter(x -> ResourceForProject.stream().anyMatch(z -> z.getResourceId() == x.getId())).collect(Collectors.toList());
        for (AssignableModel resource : AssignedResources)
            ResourceForProject.stream().filter(x -> x.getResourceId() == resource.getId()).findFirst().get().setResource(resource);
        AvailableResources = resources.stream().filter(x -> !AssignedResources.contains(x)).collect(Collectors.toList());
        AvailableTeams = teams;
    }
}
