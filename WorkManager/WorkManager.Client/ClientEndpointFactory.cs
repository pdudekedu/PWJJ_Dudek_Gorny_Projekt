using WPFTools;
using WPFTools.Attributes;
using System;
using System.Configuration;
using System.Linq;
using System.Reflection;
using System.ServiceModel;
using System.ServiceModel.Configuration;
using System.ServiceModel.Description;
using System.Text.RegularExpressions;


namespace WorkManager
{
    public class ClientEndpointFactory<T> : EndpointFactory
    {
        public static ServiceEndpoint GetServiceEndpoint()
        {
            var atr = Assembly.GetEntryAssembly().GetCustomAttribute<AssemblyClientConfigurationAttribute>();
            return GetServiceEndpoint(AppDomain.CurrentDomain.GetAssemblies().FirstOrDefault(x => x.GetName().Name == atr.AssemblyName));
        }
        public static ServiceEndpoint GetServiceEndpoint(string address)
        {
            var atr = Assembly.GetEntryAssembly().GetCustomAttribute<AssemblyClientConfigurationAttribute>();
            return GetServiceEndpoint(AppDomain.CurrentDomain.GetAssemblies().FirstOrDefault(x => x.GetName().Name == atr.AssemblyName), address);
        }
        public static ServiceEndpoint GetServiceEndpoint(Assembly assembly)
        {
            var config = ConfigurationManager.OpenExeConfiguration(assembly.Location);
            var client = config.GetSection("system.serviceModel/client") as ClientSection;
            var ends = client.Endpoints.Cast<ChannelEndpointElement>().Where(x => x.Contract == typeof(T).FullName
                      && Regex.IsMatch(x.Binding, ClientConfig.ProtocolBindingName, RegexOptions.IgnoreCase));
            if (ends.Count() > 1)
                throw new ConfigurationErrorsException($"Znaleziono wiecej niż jedną konfugirację punktu końcowego dla {typeof(T).FullName}.");
            else if (!ends.Any())
                throw new ConfigurationErrorsException($"Nie znaleziono konfiguracji punktu końcowego dla {typeof(T).FullName}.");
            var end = ends.FirstOrDefault();
            var bindings = config.GetSection("system.serviceModel/bindings") as BindingsSection;
            var binding = ResolveBinding(bindings, end.Binding, end.BindingConfiguration);
            var behaviours = config.GetSection("system.serviceModel/behaviors") as BehaviorsSection;
            var contract = ContractDescription.GetContract(typeof(T));
            var endpoint = new ServiceEndpoint(contract, binding, new EndpointAddress(new Uri($"{ClientConfig.ServerAddress}{end.Address.ToString()}"),
                new System.ServiceModel.Channels.AddressHeader[] { new ClientHeader() }));
            return endpoint;
        }
        public static ServiceEndpoint GetServiceEndpoint(Assembly assembly, string address)
        {
            var config = ConfigurationManager.OpenExeConfiguration(assembly.Location);
            var client = config.GetSection("system.serviceModel/client") as ClientSection;
            var ends = client.Endpoints.Cast<ChannelEndpointElement>().Where(x => x.Contract == typeof(T).FullName);
            if (ends.Count() > 1)
                throw new ConfigurationErrorsException($"Znaleziono wiecej niż jedną konfugirację punktu końcowego dla {typeof(T).FullName}.");
            else if (!ends.Any())
                throw new ConfigurationErrorsException($"Nie znaleziono konfiguracji punktu końcowego dla {typeof(T).FullName}.");
            var end = ends.FirstOrDefault();
            var bindings = config.GetSection("system.serviceModel/bindings") as BindingsSection;
            var binding = ResolveBinding(bindings, end.Binding, end.BindingConfiguration);
            var contract = ContractDescription.GetContract(typeof(T));
            return new ServiceEndpoint(contract, binding, new EndpointAddress($"{address}{end.Address.ToString()}"));
        }
    }
}
