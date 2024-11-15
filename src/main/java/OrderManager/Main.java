package OrderManager;

import Controllers.*;
import Entities.User;
import Entities.Utilities;
import OrderManager.Database.DatabaseConnection;
import OrderManager.Handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class Main {
    private static final int PORT = 8081;

    public static void main(String[] args) {/*
        UserController userController = new UserController();
        UUID roleId = UUID.fromString("45c4cad3-d78d-4e76-93d2-7da1c3900969");
        User user = new User(UUID.randomUUID(), roleId, "Kayha", "07508361067","Ss123$Dd", Utilities.Genders.Male);
        userController.createUser(user);
        userController.PrintUsers();*/

        try {
            // Initialize controllers
            DriverController driverController = new DriverController();
            ItemController itemController = new ItemController();
            OrderController orderController = new OrderController();
            UserController userController = new UserController();
            AddressController addressController = new AddressController();

            // Set up the HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            System.out.println("Server started on port " + PORT);

            // Define contexts for each controller endpoint
            server.createContext("/driver", new DriverHandler());
            server.createContext("/item", new ItemHandler());
            server.createContext("/order", new OrderHandler());
            server.createContext("/user", new UserHandler());
            server.createContext("/address", new AddressHandler());


            // Start the server
            server.setExecutor(null); // Creates a default executor
            server.start();
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
  }
}
