package OrderManager.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
/*
    private static final String URL = "jdbc:sqlserver://Zain\\LOCALDB#13B4325F;databaseName=OrderManagementSystem;trustServerCertificate=true;";
    private static final String USER = "ShadJ"; // Replace with your SQL Server username
    private static final String PASSWORD = "123$qweR"; // Replace with your SQL Server password
*/

    private static final String URL = "jdbc:sqlserver://ZAIN\\SQLEXPRESS;databaseName=OrderManagementSystem;integratedSecurity=true;trustServerCertificate=true;";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
