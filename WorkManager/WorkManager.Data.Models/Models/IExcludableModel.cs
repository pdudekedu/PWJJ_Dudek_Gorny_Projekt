using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace WorkManager.Data.Models
{
    public interface IExcludableModel
    {
        int Id { get; set; }
        bool InUse { get; set; }
    }
}
