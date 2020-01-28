package WorkManager.Server.Logic;

import WorkManager.Data.Enums.TaskState;
import WorkManager.Data.Models.*;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMainLogic {
  List<EFAccount> GetUsers();
  List<EFAccount> GetUsers(Boolean inUseFilter);
  EFAccount GetUser(int id);
  EFAccount UpdateUser(EFAccount user);
  Boolean CheckIfUserExists(EFAccount user);
  EFAccountView InsertUser(EFAccount user);
  Boolean RemoveUser(int id);
  EFAccount CreateUser();
  Boolean CanRemoveUser(int id);
  List<V_AccountStat> GetAccountStats();

  Project GetProject(int id);
  List<V_Project> GetAllProjects();
  List<V_Project> GetProjects(Boolean inUse);
  Boolean CanRemoveProject(int id);
  Boolean RemoveProject(int id);
  Project CreateProject();
  int InsertProject(Project project);
  void UpdateProject(Project project);
  List<V_ProjectStat> GetProjectStats();

  Task GetTask(int id);
  List<V_Task> GetTasks(int state, int projectId);
  Boolean RemoveTask(int id);
  int InsertTask(Task task);
  void UpdateTask(Task task);

  Resource GetResource(int id);
  List<V_Resource> GetResources();
  List<V_Resource> GetResources(Boolean inUse);
  Boolean CanRemoveResource(int id);
  Boolean RemoveResource(int id);
  int InsertResource(Resource resource);
  void UpdateResource(Resource resource);

  Team GetTeam(int id);
  List<V_Team> GetTeams();
  List<V_Team> GetTeams(Boolean inUse);
  Boolean CanRemoveTeam(int id);
  Boolean RemoveTeam(int id);
  Team CreateTeam();
  int InsertTeam(Team team);
  void UpdateTeam(Team team);

  List<EFValuesHistory> GetValuesHistory(Date from, Date to);
  List<AssignableModel> GetAccounts(int projectId);
  void UpdateTaskState(int id, int state);

  EFAccount Login(String userName, String password);
}
