package OrderManager.Handlers;

import Controllers.AddressController;
import Entities.UserAddress;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class AddressHandler implements HttpHandler {
    private final AddressController addressController = new AddressController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        switch (method.toUpperCase()) {
            case "POST":
                if (path.equals("/address/create")) {
                    handleCreateAddress(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            case "GET":
                if (path.equals("/address/get")) {
                    handleGetAddress(exchange);
                } else if (path.equals("/address/list")) {
                    handleListAddressesByUserId(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            case "PUT":
                if (path.equals("/address/update")) {
                    handleUpdateAddress(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            case "DELETE":
                if (path.equals("/address/delete")) {
                    handleDeleteAddress(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                break;
        }
    }

    private void handleCreateAddress(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        UserAddress address = new Gson().fromJson(requestBody.toString(), UserAddress.class);
        addressController.createAddress(address);

        String response = "Address created successfully.";
        exchange.sendResponseHeaders(201, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleGetAddress(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID id = UUID.fromString(query.split("=")[1]);
            UserAddress address = addressController.getAddressById(id);

            if (address != null) {
                String response = new Gson().toJson(address);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Address not found";
                exchange.sendResponseHeaders(404, response.getBytes().length); // Not Found
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Invalid or missing address ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleListAddressesByUserId(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("userId=")) {
            UUID userId = UUID.fromString(query.split("=")[1]);
            List<UserAddress> addresses = addressController.listAddressesByUserId(userId);

            if (!addresses.isEmpty()) {
                String response = new Gson().toJson(addresses);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "No addresses found for the specified user ID";
                exchange.sendResponseHeaders(404, response.getBytes().length); // Not Found
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Invalid or missing user ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleUpdateAddress(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        UserAddress address = new Gson().fromJson(requestBody.toString(), UserAddress.class);
        addressController.updateAddress(address);

        String response = "Address updated successfully.";
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleDeleteAddress(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID id = UUID.fromString(query.split("=")[1]);

            addressController.deleteAddress(id);

            String response = "Address deleted successfully.";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Invalid or missing address ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
