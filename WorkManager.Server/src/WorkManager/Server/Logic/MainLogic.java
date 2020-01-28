package WorkManager.Server.Logic;
import WorkManager.Data.DataAccess.*;
import WorkManager.Data.Enums.TaskState;
import WorkManager.Data.Models.*;
import WorkManager.Data.QueryBuilders.DBSet;

import javax.jws.WebService;
import javax.xml.crypto.Data;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MainLogic<OperationContract> implements IMainLogic {
    protected DataContext DataContext;
    protected final HistoryRepository _HistoryRepository = new HistoryRepository();
    protected final TeamsRepository _TeamsRepository = new TeamsRepository();
    protected final ResourcesRepository _ResourcesRepository = new ResourcesRepository();
    protected final TasksRepository _TasksRepository = new TasksRepository();
    protected final AccountsRepository _AccountsRepository = new AccountsRepository();
    protected final ProjectsRepository _ProjectsRepository = new ProjectsRepository();

    public MainLogic() throws SQLException {
        DataContext = new DataContext();
    }

    @Override
    public List<EFAccount> GetUsers() {
        try {
            return _TeamsRepository.getAll(EFAccount.class, DataContext.Accounts, DataContext, Optional.empty());
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<EFAccount> GetUsers(Boolean inUseFilter) {
        try {
            return _TeamsRepository.getAll(EFAccount.class, DataContext.Accounts, DataContext, Optional.of(inUseFilter));
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public EFAccount GetUser(int id) {
        try {
            return _TeamsRepository.get(EFAccount.class, DataContext.Accounts, DataContext, id);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EFAccount UpdateUser(EFAccount user) {
        try {
            _TeamsRepository.update(DataContext.Accounts, DataContext, user);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean CheckIfUserExists(EFAccount user) {
        return false;
    }

    @Override
    public EFAccountView InsertUser(EFAccount user) {
        try {
            int id = _TeamsRepository.insert(DataContext.Accounts, DataContext, user);
            return _TeamsRepository.get(EFAccountView.class, DataContext.Accounts, DataContext, id);
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean RemoveUser(int id) {
        try {
            return _TeamsRepository.remove(DataContext.Accounts, DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public EFAccount CreateUser() {
        return new EFAccount();
    }

    @Override
    public Boolean CanRemoveUser(int id) {
        try {
            return _TeamsRepository.CanRemoveUser(DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<V_AccountStat> GetAccountStats() {
        try {
            return _TeamsRepository.getAll(V_AccountStat.class, DataContext.V_AccountStat, DataContext);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Project GetProject(int id) {
        try {
            Project project = _ProjectsRepository.get(Project.class, DataContext.Projects, DataContext, id);
            if(project == null)
                return null;
            List<ProjectResource> projectResources = _ProjectsRepository.getProjectResources(DataContext, id);
            List<AssignableModel> resources = _ProjectsRepository.getResources(DataContext, projectResources.stream().map(x -> x.getResourceId()).collect(Collectors.toList()));
            List<AssignableModel> teams = _ProjectsRepository.getTeams(DataContext);
            project.AssignResources(projectResources, resources, teams);
            return project;
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<V_Project> GetAllProjects() {
        try {
            return _TeamsRepository.getAll(V_Project.class, DataContext.V_Projects, DataContext, Optional.empty());
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<V_Project> GetProjects(Boolean inUse) {
        try {
            return _TeamsRepository.getAll(V_Project.class, DataContext.V_Projects, DataContext, inUse);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean CanRemoveProject(int id) {
        try {
            return _ProjectsRepository.CanRemoveProject(DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean RemoveProject(int id) {
        try {
            return _TeamsRepository.remove(DataContext.Projects, DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Project CreateProject() {
        try {
            Project project = new Project();
            List<AssignableModel> resources = _ProjectsRepository.getResources(DataContext, new ArrayList<>());
            List<AssignableModel> teams = _ProjectsRepository.getTeams(DataContext);
            project.AssignResources(new ArrayList<>(), resources, teams);
            return project;
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int InsertProject(Project project) {
        try {
            int id = _TeamsRepository.insert(DataContext.Projects, DataContext, project);
            if(project.getResourceForProject() != null)
                for (ProjectResource resource : project.getResourceForProject())
                {
                    resource.setProjectId(id);
                    _TeamsRepository.insert(DataContext.ProjectResources, DataContext, resource);
                }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void UpdateProject(Project project) {
        try {
            _TeamsRepository.update(DataContext.Projects, DataContext, project);
            if(project.getResourceForProject() != null)
                for (ProjectResource resource : project.getResourceForProject())
                {
                    if(resource.getId() == 0) {
                        resource.setProjectId(project.getId());
                        _TeamsRepository.insert(DataContext.ProjectResources, DataContext, resource);
                    }
                    else
                        _TeamsRepository.update(DataContext.ProjectResources, DataContext, resource);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<V_ProjectStat> GetProjectStats() {
        try {
            return _TeamsRepository.getAll(V_ProjectStat.class, DataContext.V_ProjectsStat, DataContext);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Task GetTask(int id) {
        try {
            Task task = _TasksRepository.get(Task.class, DataContext.Tasks, DataContext, id);
            if (task == null)
                return null;
            task.setTaskTimes(_TasksRepository.getTaskTimes(DataContext, task.getId()));
            task.setAccount(_TasksRepository.get(EFAccount.class, DataContext.Accounts, DataContext, task.getAccountId()));
            task.setResourceForTask(_TasksRepository.getTaskResources(DataContext, task.getId()));
            return task;
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<V_Task> GetTasks(int state, int projectId) {
        try {
            return _TasksRepository.getTasks(DataContext, state, projectId);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean RemoveTask(int id) {
        try {
            return _TeamsRepository.remove(DataContext.Tasks, DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int InsertTask(Task task) {
        try {
            int id = _TeamsRepository.insert(DataContext.Tasks, DataContext, task);
            if(task.getResourceForTask() != null)
                for (TaskResource resource : task.getResourceForTask())
                {
                    resource.setTaskId(id);
                    _TeamsRepository.insert(DataContext.TaskResources, DataContext, resource);
                }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void UpdateTask(Task task) {
        try {
            _TeamsRepository.update(DataContext.Tasks, DataContext, task);
            if(task.getResourceForTask() != null)
                for (TaskResource resource : task.getResourceForTask())
                {
                    if(resource.getId() == 0) {
                        resource.setTaskId(task.getId());
                        _TeamsRepository.insert(DataContext.TaskResources, DataContext, resource);
                    }
                    else
                        _TeamsRepository.update(DataContext.TaskResources, DataContext, resource);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource GetResource(int id) {
        try {
            return _ResourcesRepository.get(Resource.class, DataContext.Resources, DataContext, id);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<V_Resource> GetResources() {
        try {
            return _TeamsRepository.getAll(V_Resource.class, DataContext.V_Resources, DataContext, Optional.empty());
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<V_Resource> GetResources(Boolean inUse) {
        try {
            return _TeamsRepository.getAll(V_Resource.class, DataContext.V_Resources, DataContext, Optional.of(inUse));
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean CanRemoveResource(int id) {
        try {
            return _ResourcesRepository.CanRemoveResource(DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean RemoveResource(int id) {
        try {
            return _TeamsRepository.remove(DataContext.Resources, DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int InsertResource(Resource resource) {
        try {
            return _TeamsRepository.insert(DataContext.Resources, DataContext, resource);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void UpdateResource(Resource resource) {
        try {
            _TeamsRepository.update(DataContext.Resources, DataContext, resource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Team GetTeam(int id) {
        try {
            Team team = _TeamsRepository.get(Team.class, DataContext.Teams, DataContext, id);
            if(team == null)
                return null;
            List<TeamAccount> teamAccounts = _TeamsRepository.getTeamAccounts(DataContext, team.getId());
            List<AssignableModel> accounts = _TeamsRepository.getAccounts(DataContext, teamAccounts.stream().map(x -> x.getAccountId()).collect(Collectors.toList()));
            team.AssignAccountTeams(teamAccounts, accounts);
            return team;
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<V_Team> GetTeams() {
        try {
            return _TeamsRepository.getAll(V_Team.class, DataContext.V_Teams, DataContext, Optional.empty());
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<V_Team> GetTeams(Boolean inUse) {
        try {
            return _TeamsRepository.getAll(V_Team.class, DataContext.V_Teams, DataContext, Optional.of(inUse));
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean CanRemoveTeam(int id) {
        try {
            return _TeamsRepository.CanRemoveTeam(DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean RemoveTeam(int id) {
        try {
            return _TeamsRepository.remove(DataContext.Teams, DataContext, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Team CreateTeam() {
        try {
            Team team = new Team();
            List<AssignableModel> accounts = _TeamsRepository.getAccounts(DataContext, new ArrayList<Integer>());
            team.AssignAccountTeams(new ArrayList<TeamAccount>(), accounts);
            return team;
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int InsertTeam(Team team) {
        try {
            int id = _TeamsRepository.insert(DataContext.Teams, DataContext, team);
            if(team.getAccountTeams()  != null)
                for (TeamAccount account : team.getAccountTeams())
                {
                    account.setTeamId(id);
                    _TeamsRepository.insert(DataContext.TeamAccounts, DataContext, account);
                }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void UpdateTeam(Team team) {
        try {
            _TeamsRepository.update(DataContext.Teams, DataContext, team);
            if(team.getAccountTeams() != null)
                for (TeamAccount account : team.getAccountTeams())
                {
                    if(account.getId() == 0) {
                        account.setTeamId(team.getId());
                        _TeamsRepository.insert(DataContext.TeamAccounts, DataContext, account);
                    }
                    else
                        _TeamsRepository.update(DataContext.TeamAccounts, DataContext, account);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EFValuesHistory> GetValuesHistory(Date from, Date to) {
        try {
            return _HistoryRepository.GetValuesHistory(DataContext, from, to);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<AssignableModel> GetAccounts(int projectId) {
        try {
            return _TeamsRepository.getAccounts(DataContext, projectId);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void UpdateTaskState(int id, int state) {
        try {
            _TasksRepository.update(DataContext.Tasks, DataContext, id, new DBSet(Task.State, Task.State.of(state)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EFAccount Login(String userName, String password) {
        try {
           return _AccountsRepository.GetUser(DataContext, userName, password);
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void InsertTaskTime(TaskTime time){
        try {
            _TeamsRepository.insert(DataContext.TaskTimes, DataContext, time);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
