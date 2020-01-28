using WPFTools.Comparers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WorkManager.Models
{
    public class UserType
    {
        private UserType(string name, byte[] permissions, bool showInWindow = true)
        {
            Name = name;
            Permissions = permissions;
            ShowInWindow = showInWindow;
        }
        public static UserType[] UserTypes { get; } = new UserType[]
        {
            new UserType("Menadżer", new byte[] { 255, 255 }, true),
            new UserType("Pracownik", new byte[] { 7, 13 }, true),
            new UserType("Interesant", new byte[] { 85, 85 }, true),
            new UserType("Niestandardowy", null, false),
        };
        public string Name { get; }
        public byte[] Permissions { get; }
        public bool ShowInWindow { get; }
        public static UserType GetUserType(byte[] permissions)
        {
            var comparer = new ByteArrayPermissionsComparer();
            var result = UserTypes.FirstOrDefault(x => comparer.Compare(x.Permissions, permissions) == 0);
            return result ?? UserTypes.ElementAt(3);
        }
    }
}
