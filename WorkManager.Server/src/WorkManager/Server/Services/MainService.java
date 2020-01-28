package WorkManager.Server.Services;

import WorkManager.Server.Logic.MainLogic;
import WorkManager.Server.ServiceContracts.IMainService;
import com.google.gson.*;

import javax.jws.WebService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

@WebService(endpointInterface= "WorkManager.Server.ServiceContracts.IMainService", serviceName="MainService", targetNamespace = "WorkManager/main/")
public class MainService<OperationContract> implements IMainService {
    MainLogic logic = new MainLogic();

    public MainService() throws SQLException {
    }

    @Override
    public String Get(String operation, String parameters) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        String finalOperation = operation;
        Optional<Method> optional = Arrays.stream(MainLogic.class.getMethods()).filter(x->x.getName().equalsIgnoreCase(finalOperation)).findFirst();
        if(optional.isPresent()) {
            Method method = optional.get();
            JsonArray array = gson.fromJson(parameters, JsonArray.class);
            Object[] resolvedArgs = new Object[method.getParameterCount()];
            for(int i = 0; i< method.getParameterCount(); i++){
                resolvedArgs[i] = gson.fromJson(array.get(i), method.getParameterTypes()[i]);
            }
            String response = null;
            try {
                response = gson.toJson(method.invoke(logic, resolvedArgs));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return response;
        }
        return null;
    }

    @Override
    public void Invoke(String operation, String parameters) throws InvocationTargetException, IllegalAccessException {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        String finalOperation = operation;
        Optional<Method> optional = Arrays.stream(MainLogic.class.getMethods()).filter(x->x.getName().equalsIgnoreCase(finalOperation)).findFirst();
        if(optional.isPresent()) {
            Method method = optional.get();
            JsonArray array = gson.fromJson(parameters, JsonArray.class);
            Object[] resolvedArgs = new Object[method.getParameterCount()];
            for(int i = 0; i< method.getParameterCount(); i++){
                resolvedArgs[i] = gson.fromJson(array.get(i), method.getParameterTypes()[i]);
            }
            method.invoke(logic, resolvedArgs);
        }
    }
}
