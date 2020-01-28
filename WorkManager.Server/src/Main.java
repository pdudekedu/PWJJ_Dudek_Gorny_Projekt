import WorkManager.Data.DataAccess.HistoryRepository;
import WorkManager.Data.DataAccess.ProjectsRepository;
import WorkManager.Data.Models.*;
import WorkManager.Server.Logic.MainLogic;
import WorkManager.Server.Services.MainService;

import javax.xml.ws.Endpoint;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] argv) throws SQLException {
        Object implementor = new MainService();
        String address = "http://localhost:9000/main";
        Endpoint.publish(address, implementor);

//        try {
//            DataContext context = new DataContext();
//            ProjectsRepository repository = new ProjectsRepository();
//
//            Resource resource = new Resource();
//            resource.setName("Testowy zasób");
//            resource.setDescription("OOOpis");
//            //resource.setId(repository.insert(DataContext.Resources, context, resource));
//            int id = resource.getId();
//            //List<V_ProjectStat> valuesHistoryList = historyRepository.getAll(V_ProjectStat.class, DataContext.V_ProjectsStat, context);
//            //valuesHistoryList.isEmpty();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
