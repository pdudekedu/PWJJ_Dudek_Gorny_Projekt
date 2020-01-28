using System;
using System.Linq;
using System.Reflection;
using System.ServiceModel.Channels;
using System.ServiceModel.Configuration;
using System.ServiceModel.Description;


namespace WorkManager
{
    public abstract class EndpointFactory
    {
        internal protected EndpointFactory() { }
        protected internal static Binding ResolveBinding(BindingsSection section, string bindingName, string name)
        {
            var bindingCollection = section.BindingCollections.FirstOrDefault(x => x.BindingName == bindingName);
            var bindingElement = bindingCollection?.ConfiguredBindings?.FirstOrDefault(x => x.Name == name);
            if (bindingElement == null)
                return null;
            var binding = (Binding)Activator.CreateInstance(bindingCollection.BindingType);
            binding.Name = bindingElement.Name;
            bindingElement.ApplyConfiguration(binding);
            return binding;
        }
        protected internal static void AddBehaviors(string behaviorConfiguration, ServiceEndpoint serviceEndpoint, BehaviorsSection section)
        {
            if (string.IsNullOrEmpty(behaviorConfiguration))
                return;
            EndpointBehaviorElement behaviorElement = section.EndpointBehaviors[behaviorConfiguration];
            for (int i = 0; i < behaviorElement.Count; i++)
            {
                BehaviorExtensionElement behaviorExtension = behaviorElement[i];
                object extension = behaviorExtension.GetType().InvokeMember("CreateBehavior", BindingFlags.InvokeMethod | BindingFlags.NonPublic | BindingFlags.Instance, null, behaviorExtension, null);
                if (extension != null)
                {
                    serviceEndpoint.Behaviors.Add((IEndpointBehavior)extension);
                }
            }
        }
    }
}
