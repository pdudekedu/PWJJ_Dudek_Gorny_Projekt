package WorkManager.Data.Models;

import WorkManager.Data.QueryBuilders.DBTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataContext {
    public DataContext() throws SQLException {
        //String connectionUrl = "jdbc:sqlserver://DELL-02\\SQLSRV2017:1433;databaseName=WM;user=sa;password=sa;";
        String connectionUrl = "jdbc:sqlserver://PCV2\\SQLEXPRESS17:1433;databaseName=WM;user=sa;password=sa;";
        Connection connection = DriverManager.getConnection(connectionUrl);
        statement = connection.createStatement();
    }

    public static final DBTable Accounts = new DBTable("MGMNT","ACCOUNTS");
    public static final DBTable Projects = new DBTable("PROJECTS");
    public static final DBTable Tasks = new DBTable("TASKS");
    public static final DBTable TaskTimes = new DBTable("TASKTIMES");
    public static final DBTable Resources = new DBTable("RESOURCES");
    public static final DBTable ProjectResources = new DBTable("PROJECTRESOURCES");
    public static final DBTable TaskResources = new DBTable("TASKRESOURCES");
    public static final DBTable Teams = new DBTable("TEAMS");
    public static final DBTable TeamAccounts = new DBTable("TEAMACCOUNTS");

    public static final DBTable V_Resources = new DBTable("V_RESOURCES");
    public static final DBTable V_Teams = new DBTable("V_TEAMS");
    public static final DBTable V_Projects = new DBTable("V_PROJECTS");
    public static final DBTable V_Tasks = new DBTable("V_TASKS");
    public static final DBTable V_ProjectsStat = new DBTable("V_PROJECTSTAT");
    public static final DBTable V_AccountStat = new DBTable("V_ACCOUNTSTAT");
    public static final DBTable ValuesHistory = new DBTable("HST", "V_VALUESHISTORY");

    private Connection connection;
    private Statement statement;

    public Statement getStatement() { return statement; }
}
