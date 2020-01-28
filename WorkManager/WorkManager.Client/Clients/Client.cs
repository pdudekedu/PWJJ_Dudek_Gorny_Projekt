using WPFTools;
using WPFTools.Attributes;
using System;
using System.Configuration;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.ServiceModel.Configuration;
using System.ServiceModel.Description;
using System.Text.RegularExpressions;
using System.Xml;
using WorkManager.Data.Models;

namespace WorkManager.Clients
{
    public class Client : IDisposable
    {
        public void Dispose() { }

        public T Get<T>(params object[] args)
        {
            string operationName = GetCallingMethod();
            var param = Newtonsoft.Json.JsonConvert.SerializeObject(args);
            string result;
            lock (typeof(Client))
                result = new JMainService.MainServiceClient().Get(operationName, param);
            var r = Newtonsoft.Json.JsonConvert.DeserializeObject<T>(result);
            return r;
        }
        public void Invoke(params object[] args)
        {
            string operationName = GetCallingMethod();
            var param = Newtonsoft.Json.JsonConvert.SerializeObject(args);
            new JMainService.MainServiceClient().Invoke(operationName, param);
        }
        private string GetCallingMethod()
        {
            StackTrace stackTrace = new StackTrace();
            StackFrame[] stackFrames = stackTrace.GetFrames();

            StackFrame callingFrame = stackFrames[2];
            MethodBase method = callingFrame.GetMethod();
            return method.Name;
        }
    }


}
