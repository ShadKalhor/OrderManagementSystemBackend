package OrderManager.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    private static final String URL = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
