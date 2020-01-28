package WorkManager.Server.ServiceContracts;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.lang.reflect.InvocationTargetException;

@WebService
public interface IMainService {
    @WebMethod
    String Get(String operation, String parameters) throws InvocationTargetException, IllegalAccessException;
    @WebMethod
    void Invoke(String operation, String parameters) throws InvocationTargetException, IllegalAccessException;
}
