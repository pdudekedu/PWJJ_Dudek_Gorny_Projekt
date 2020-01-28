using WPFTools.Enums;
using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WorkManager.Clients;
using WorkManager.Data.Models;

namespace WorkManager.ViewModels
{
    public class ProjectsViewModel : MetadataViewModel<V_Project, Project>
    {
        public override bool CanRemoveItem(V_Project item)
        {
            using (var client = new MainServiceClient())
                return client.CanRemoveProject(item.Id);
        }

        public override Project CreateNewItem()
        {
            using (var client = new MainServiceClient())
                return client.CreateProject();
        }

        public override bool RemoveItem(V_Project item)
        {
            using (var client = new MainServiceClient())
                client.RemoveProject(item.Id);
            return true;
        }

        protected override Project GetEditItem()
        {
            using (var client = new MainServiceClient())
                return client.GetProject(EditItemId);
        }

        protected override IEnumerable<V_Project> GetItems()
        {
            using (var client = new MainServiceClient())
                return client.GetProjects(InUseFilter);
        }

        protected override string GetLoadingEditItemText()
        {
            return "Ładowanie danych projektu";
        }

        protected override string GetLoadingItemsText()
        {
            return "Ładowanie projektów";
        }

        protected override void PrepareItemForSave()
        {
            var toDelete = EditItem.ResourceForProject.Where(x => x.TrackingState.HasFlag(TrackingState.Deleted));
            foreach (var resource in toDelete.ToList())
                EditItem.ResourceForProject.Remove(resource);
        }

        #region Commands
        /// <summary>
        /// Obsługa akcji dodania konta do zespołu.
        /// </summary>
        public WPFTools.RelayCommand<AssignableModel> AddResourceCommand
        {
            get
            {
                return new WPFTools.RelayCommand<AssignableModel>((resource) =>
                {
                    if (EditItem == null || resource == null)
                        return;
                    EditItem.ResourceForProject.Add(new ProjectResource()
                    {
                        ResourceId = resource.Id,
                        Project = EditItem,
                        Resource = resource,
                        TrackingState = WPFTools.Enums.TrackingState.Added
                    });
                    EditItem.AssignedResources.Add(resource);
                    EditItem.AvailableResources.Remove(resource);
                });
            }
        }
        /// <summary>
        /// Obsługa akcji usunięcia konta z zespołu.
        /// </summary>
        public WPFTools.RelayCommand<ProjectResource> RemoveResourceCommand
        {
            get
            {
                return new WPFTools.RelayCommand<ProjectResource>((resourceForProject) =>
                {
                    if (EditItem == null || resourceForProject == null)
                        return;
                    if (resourceForProject.TrackingState.HasFlag(TrackingState.Deleted))
                        resourceForProject.TrackingState ^= TrackingState.Deleted;
                    else
                    {
                        if (resourceForProject.TrackingState.HasFlag(TrackingState.Added))
                        {
                            EditItem.ResourceForProject.Remove(resourceForProject);
                            var resource = EditItem.AssignedResources.FirstOrDefault(x => x.Id == resourceForProject.ResourceId);
                            if (resource != null)
                            {
                                EditItem.AssignedResources.Remove(resource);
                                EditItem.AvailableResources.Add(resource);
                            }
                        }
                        else
                            resourceForProject.TrackingState |= TrackingState.Deleted;
                    }

                });
            }
        }
        #endregion
    }
}
