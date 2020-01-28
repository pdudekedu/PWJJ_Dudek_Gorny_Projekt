using WPFTools.Communication.ServiceClients;
using WPFTools.Models;
using WorkManager.Data.Models;

namespace WorkManager.Clients
{
    public class SavingServiceClient : Client
    {
        public SavingServiceClient() : base() { }

        public void Commit() { }

        public void Rollback() { }

        public void CascadeInsert(EFModel model)
        {
            switch (model)
            {
                case Resource resource:
                    InsertResource(resource);
                    break;
                case Task task:
                    InsertTask(task);
                    break;
                case Project project:
                    InsertProject(project);
                    break;
                case Team team:
                    InsertTeam(team);
                    break;
            }
        }

        public void Delete(EFExcludableModel<int> model) => Remove(model);

        public int Insert(EFHistoryModel<int> model)
        {
            switch (model)
            {
                case Resource resource:
                    return InsertResource(resource);
                case Task task:
                    return InsertTask(task);
                case Project project:
                    return InsertProject(project);
                case Team team:
                    return InsertTeam(team);
                case EFAccount account:
                     InsertUser(account);
                    break;
            }
            return 0;
        }

        public void Modify(EFHistoryModel<int> model)
        {
            switch (model)
            {
                case Resource resource:
                    UpdateResource(resource);
                    break;
                case Task task:
                    task.Account = null;
                    if (task.AccountId == 0)
                        task.AccountId = null;
                    UpdateTask(task);
                    break;
                case Project project:
                    UpdateProject(project);
                    break;
                case Team team:
                    UpdateTeam(team);
                    break;
                case EFAccount account:
                    UpdateUser(account);
                    break;
            }
        }


        public void Remove(EFHistoryModel<int> model)
        {
            switch (model)
            {
                case Resource resource:
                    RemoveResource(resource);
                    break;
                case Task task:
                    RemoveTask(task);
                    break;
                case Project project:
                    RemoveProject(project);
                    break;
                case Team team:
                    RemoveTeam(team);
                    break;
            }
        }

        protected int InsertResource(Resource resource) => Get<int>(resource);
        protected void UpdateResource(Resource resource) => Invoke(resource);
        protected bool RemoveResource(Resource resource) => Get<bool>(resource);

        protected int InsertTask(Task Task) => Get<int>(Task);
        protected void UpdateTask(Task Task) => Invoke(Task);
        protected bool RemoveTask(Task Task) => Get<bool>(Task);

        protected int InsertProject(Project Project) => Get<int>(Project);
        protected void UpdateProject(Project Project) => Invoke(Project);
        protected bool RemoveProject(Project Project) => Get<bool>(Project);

        protected int InsertTeam(Team Team) => Get<int>(Team);
        protected void UpdateTeam(Team Team) => Invoke(Team);
        protected bool RemoveTeam(Team Team) => Get<bool>(Team);

        public EFAccount UpdateUser(EFAccount user) => Get<EFAccount>(user);
        public EFAccountView InsertUser(EFAccount user) => Get<EFAccountView>(user);
    }
}
