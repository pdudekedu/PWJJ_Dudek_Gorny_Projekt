using WPFTools.Communication.ServiceClients;
using WPFTools.Communication.Services;
using WPFTools.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;

namespace WorkManager.Clients
{
    public class StartupServiceClient : Client, IStartupService, IStartupContext
    {
        public DomainMetadata GetDomainMetadata() => new DomainMetadata();
        public bool IsTestVersion() => false;
        public LoginResult Login(string userName, string password, LoginMetadata metadata)
        {
            var account = Get<EFAccount>(userName, password);
            return new LoginResult(WPFTools.Enums.LoginResult.Success, account);
        }            
        public LogoutResult Logout(LogoutMetadata metadata) => new LogoutResult();
        public ComponentsValidationResult ValidateComponents(ComponentsValidationMetadata metadata) 
            => new ComponentsValidationResult(WPFTools.Enums.ComponentsValidationResult.Success, WPFTools.Enums.LicenseValidationResult.Success);

        public bool ValidateConnection()
        {
            return true;
        }
    }
}
