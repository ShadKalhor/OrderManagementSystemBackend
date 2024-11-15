package OrderManager.Handlers;

import Controllers.UserController;
import Entities.User;
import Entities.Utilities;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class UserHandler implements HttpHandler {
    private final UserController userController = new UserController();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        switch (method.toUpperCase()) {
            case "POST":
                if (path.equals("/user/create")) {
                    handleCreateUser(exchange);
                } else if (path.equals("/user/validateLogin")) {
                    handleValidateLogin(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;

            case "GET":
                if (path.equals("/user/get")) {
                    handleGetUser(exchange);
                } else if (path.equals("/user/printUsers")) {
                    handlePrintUsers(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;

            case "PUT":
                if (path.equals("/user/update")) {
                    handleUpdateUser(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;

            case "DELETE":
                if (path.equals("/user/delete")) {
                    handleDeleteUser(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;

            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                break;
        }
    }

    private void handleCreateUser(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Expected format: roleId,name,phone,password,gender
        String[] parts = requestBody.toString().split(",");
        if (parts.length == 5) {
            UUID roleId = UUID.fromString(parts[0]);
            String name = parts[1];
            String phone = parts[2];
            String password = parts[3];
            Utilities.Genders gender = Utilities.Genders.valueOf(parts[4]);

            User user = new User(UUID.randomUUID(), roleId, name, phone, password, gender);
            userController.CreateUser(user);

            String response = "User created successfully: " + user.toString();
            exchange.sendResponseHeaders(201, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Invalid data format. Expected format: roleId,name,phone,password,gender";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleGetUser(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            if (query.startsWith("id=")) {
                UUID userId = UUID.fromString(query.split("=")[1]);
                User user = userController.GetUserById(userId);
                sendResponse(exchange, user);

            } else if (query.startsWith("phone=")) {
                String phone = query.split("=")[1];
                User user = userController.GetUserByPhoneNumber(phone);
                sendResponse(exchange, user);

            } else if (query.equals("all")) {
                List<User> users = userController.LoadUsers();
                String response = new Gson().toJson(users);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Invalid or missing ID/phone parameter";
                exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Missing query parameter";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleUpdateUser(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Expected format: id,roleId,name,phone,password,gender
        String[] parts = requestBody.toString().split(",");
        if (parts.length == 6) {
            UUID userId = UUID.fromString(parts[0]);
            UUID roleId = UUID.fromString(parts[1]);
            String name = parts[2];
            String phone = parts[3];
            String password = parts[4];
            Utilities.Genders gender = Utilities.Genders.valueOf(parts[5]);

            User updatedUser = new User(userId, roleId, name, phone, password, gender);
            userController.UpdateUser(updatedUser);

            String response = "User updated successfully: " + updatedUser.toString();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Invalid data format. Expected format: id,roleId,name,phone,password,gender";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleDeleteUser(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID userId = UUID.fromString(query.split("=")[1]);

            userController.DeleteUser(userId);

            String response = "User deleted successfully.";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Invalid or missing ID parameter";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    private void handleValidateLogin(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Expected format: phone,password
        String[] parts = requestBody.toString().split(",");
        if (parts.length == 2) {
            String phoneNumber = parts[0];
            String password = parts[1];

            boolean isValid = userController.IsValidLogin(phoneNumber, password);

            String response;
            if (isValid) {
                response = "Login successful";
                exchange.sendResponseHeaders(200, response.getBytes().length); // HTTP OK
            } else {
                response = "Invalid login credentials";
                exchange.sendResponseHeaders(401, response.getBytes().length); // HTTP Unauthorized
            }

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Invalid data format. Expected format: phone,password";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handlePrintUsers(HttpExchange exchange) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        userController.PrintUsers();

        System.out.flush();
        System.setOut(old);

        String response = baos.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private void sendResponse(HttpExchange exchange, User user) throws IOException {
        if (user != null) {
            Gson gson = new Gson();
            String response = gson.toJson(user);  // Convert user object to JSON
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "User not found";
            exchange.sendResponseHeaders(404, response.getBytes().length); // Not Found
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
