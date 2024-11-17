package OrderManager.Handlers;

import Controllers.DriverController;
import Entities.Driver;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class DriverHandler implements HttpHandler {
    private final DriverController driverController = new DriverController();
    private static final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        switch (method.toUpperCase()) {
            case "POST":
                handleCreateDriver(exchange);
                break;
            case "GET":
                if (path.equals("/driver/list")) {
                handleListDrivers(exchange); // New request for listing drivers
            } else{
                handleGetDriver(exchange);
            }
                handleGetDriver(exchange);
                break;
            case "PUT":
                handleUpdateDriver(exchange);
                break;
            case "DELETE":
                handleDeleteDriver(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                break;
        }
    }

    private void handleListDrivers(HttpExchange exchange) throws IOException {
        try {
            List<Driver> drivers = driverController.GetDrivers(); // Fetch drivers from the controller
            String response = gson.toJson(drivers);
            exchange.sendResponseHeaders(200, response.getBytes().length); // HTTP 200 OK
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (Exception e) {
            exchange.sendResponseHeaders(500, -1); // HTTP 500 Internal Server Error
            e.printStackTrace();
        }
    }

    private void handleCreateDriver(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        String[] parts = requestBody.toString().split(",");
        if (parts.length == 4) {
            String name = parts[0];
            String phone = parts[1];
            String vehicleNumber = parts[2];
            int age = Integer.parseInt(parts[3]);

            Driver driver = new Driver(UUID.randomUUID(), name, phone, vehicleNumber, age);
            driverController.createDriver(driver);

            String response = "Driver created successfully: " + driver.toString();
            exchange.sendResponseHeaders(201, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Invalid data format. Expected format: name,phone,vehicleNumber,age";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleGetDriver(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID driverId = UUID.fromString(query.split("=")[1]);

            Driver driver = driverController.readDriver(driverId);

            if (driver != null) {
                String response = "Driver found: " + driver.toString();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Driver not found";
                exchange.sendResponseHeaders(404, response.getBytes().length); // Not Found
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Missing or invalid driver ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleUpdateDriver(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID driverId = UUID.fromString(query.split("=")[1]);

            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            String[] parts = requestBody.toString().split(",");
            if (parts.length == 4) {
                String name = parts[0];
                String phone = parts[1];
                String vehicleNumber = parts[2];
                int age = Integer.parseInt(parts[3]);

                driverController.updateDriver(driverId, name, phone, vehicleNumber, age);

                String response = "Driver updated successfully.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Invalid data format. Expected format: name,phone,vehicleNumber,age";
                exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Missing or invalid driver ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleDeleteDriver(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID driverId = UUID.fromString(query.split("=")[1]);

            driverController.deleteDriver(driverId);

            String response = "Driver deleted successfully.";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Missing or invalid driver ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
