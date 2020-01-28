using WPFTools.Communication.Services;
using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using WorkManager.Data.Enums;
using WorkManager.Data.Models;

namespace WorkManager.Clients
{
    public class MainServiceClient : Client, IMetadataUsers, IDisposable
    {
        private readonly JMainService.MainServiceClient JClient = new JMainService.MainServiceClient();
        public IEnumerable<EFAccount> GetUsers(bool? inUseFilter)
            => Get<EFAccount[]>(inUseFilter);
        public EFAccount GetUser(int id)
            => Get<EFAccount>(id);
        public EFAccount UpdateUser(EFAccount user)
            => Get<EFAccount>(user);
        public bool CheckIfUserExists(EFAccount user)
            => Get<bool>(user);
        public EFAccountView InsertUser(EFAccount user)
            => Get<EFAccountView>(user);
        public bool RemoveUser(int id)
            => Get<bool>(id);
        public EFAccount CreateUser()
            => Get<EFAccount>(nameof(MainServiceClient.GetUsers));

        public Project GetProject(int id)
            => Get<Project>(id);

        public IEnumerable<V_Project> GetProjects(bool? inUse)
            => Get<V_Project[]>(inUse);
        public Task GetTask(int id)
            => Get<Task>(id);

        public IEnumerable<Task> GetTasks(bool? inUse)
            => Get<Task[]>(inUse);
        public IEnumerable<V_Task> GetTasks(TaskState state, int projectId)
            => Get<V_Task[]>((int)state, projectId);

        public Resource GetResource(int id)
            => Get<Resource>(id);

        public IEnumerable<V_Resource> GetResources(bool? inUse)
            => Get<V_Resource[]>(inUse);

        public Team GetTeam(int id) => Get<Team>(id);

        public IEnumerable<V_Team> GetTeams(bool? inUse) => Get<V_Team[]>(inUse);

        public bool CanRemoveResource(int id) => Get<bool>(id);
        public bool RemoveResource(int id) => Get<bool>(id);
        public bool CanRemoveProject(int id) => Get<bool>(id);
        public bool RemoveProject(int id) => Get<bool>(id);
        public bool CanRemoveTeam(int id) => Get<bool>(id);
        public bool RemoveTeam(int id) => Get<bool>(id);

        public Project CreateProject() => Get<Project>();

        public Team CreateTeam() => Get<Team>();

        public IEnumerable<EFValuesHistory> GetValuesHistory(DateTime? from, DateTime? to) 
            => Get<EFValuesHistory[]>(from, to);

        public bool RemoveTask(int id) => Get<bool>(id);

        public IEnumerable<AssignableModel> GetAccounts(int projectId) => Get<AssignableModel[]>(projectId);

        public void UpdateTaskState(int id, TaskState state) => Invoke(id, (int)state);

        public bool CanRemoveUser(int id) => Get<bool>(id);

        public IEnumerable<V_ProjectStat> GetProjectStats() => Get<V_ProjectStat[]>();

        public IEnumerable<V_AccountStat> GetAccountStats() => Get<V_AccountStat[]>();

        public void Dispose()
        {
            ((IDisposable)JClient).Dispose();
        }
    }
}
