using WPFTools.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using WorkManager.Clients;
using WorkManager.Data.Models;

namespace WorkManager.ViewModels
{
    public class ResourcesViewModel : MetadataViewModel<V_Resource, Resource>
    {
        public override bool CanRemoveItem(V_Resource item)
        {
            using (var client = new MainServiceClient())
                return client.CanRemoveResource(item.Id);
        }

        public override Resource CreateNewItem()
        {
            return new Resource()
            {
                TrackingState = WPFTools.Enums.TrackingState.Added
            };
        }

        public override bool RemoveItem(V_Resource item)
        {
            using (var client = new MainServiceClient())
                client.RemoveResource(item.Id);
            return true;
        }

        protected override Resource GetEditItem()
        {
            using (var client = new MainServiceClient())
                return client.GetResource(EditItemId);
        }

        protected override IEnumerable<V_Resource> GetItems()
        {
            using (var client = new MainServiceClient())
                return client.GetResources(InUseFilter);
        }

        protected override string GetLoadingEditItemText()
        {
            return "Ładowanie danych zasobu";
        }

        protected override string GetLoadingItemsText()
        {
            return "Ładowanie zasobów";
        }
    }
}
