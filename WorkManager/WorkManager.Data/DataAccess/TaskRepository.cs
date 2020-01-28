using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;
using WorkManager.Data.Enums;
using WorkManager.Data.Models;

namespace WorkManager.Data.DataAccess
{
    internal class TaskRepository : Repository<Task>
    {
        public IEnumerable<V_Task> GetTasks(DataContext context, TaskState state, int projectId)
        {
            return context.V_Tasks.Where(x => x.State == state && x.ProjectId == projectId).ToList();
        }
        public Task GetTask(DataContext context, int id)
        {
            return context.Tasks
                  .Include(x => x.TaskTimes)
                  .Include(x => x.Account)
                  .Include(x=>x.ResourceForTask)
                  .FirstOrDefault(x => x.Id == id);
        }
        public IEnumerable<AssignableModel> GetResources(DataContext context, int[] assigned)
        {
            return (from r in context.Resources
                    where assigned.Contains(r.Id) || r.InUse
                    orderby r.Name
                    select new AssignableModel()
                    {
                        Id = r.Id,
                        Name = r.Name
                    }).ToList();
        }
        public void UpdateTaskState(DataContext context, int id, TaskState state, string modUser, string modComp)
        {
            var task = new Task()
            {
                Id = id,
                State = state,
                ModUser = modUser,
                ModComp = modComp
            };
            context.Entry(task).Property(x => x.ModComp).IsModified = true;
            context.Entry(task).Property(x => x.ModUser).IsModified = true;
            context.Entry(task).Property(x => x.State).IsModified = true;
            context.SaveChanges();
        }
    }
}
