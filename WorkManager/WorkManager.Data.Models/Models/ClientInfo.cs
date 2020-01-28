using System;

namespace WorkManager.Data.Models
{
    public class ClientInfo
    {
        public static ClientInfo Current { get; private set; } = new ClientInfo(Guid.NewGuid());
        public ClientInfo() { }
        private ClientInfo(Guid guid) => Guid = guid;

        public Guid Guid { get; set; }
        public string ModUser { get; set; }
        public string ModComp { get; set; }
        public int LanguageId { get; set; }
    }
}
