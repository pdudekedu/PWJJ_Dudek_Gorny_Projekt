using Newtonsoft.Json;
using System;
using System.Xml;
using WorkManager.Data.Models;


namespace WorkManager
{
    public class ClientHeader : System.ServiceModel.Channels.AddressHeader
    {
        public static Guid Guid { get; } = Guid.NewGuid();
        public override string Name => "ClientInfo";

        public override string Namespace => "WorkManager";

        protected override void OnWriteAddressHeaderContents(XmlDictionaryWriter writer)
        {
            writer.WriteString(JsonConvert.SerializeObject(ClientInfo.Current));
        }
    }
}
