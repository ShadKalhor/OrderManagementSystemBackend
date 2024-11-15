package OrderManager.Handlers;

import Controllers.ItemController;
import Entities.Item;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class ItemHandler implements HttpHandler {
    private final ItemController itemController = new ItemController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        switch (method.toUpperCase()) {
            case "POST":
                if (path.equals("/item/create")) {
                    handleCreateItem(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            case "GET":
                if (path.equals("/item/get")) {
                    handleGetItem(exchange);
                } else if (path.equals("/item/list")) {
                    handleListItems(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            case "PUT":
                if (path.equals("/item/update")) {
                    handleUpdateItem(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            case "DELETE":
                if (path.equals("/item/delete")) {
                    handleDeleteItem(exchange);
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not Found
                }
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                break;
        }
    }

    private void handleCreateItem(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        String[] parts = requestBody.toString().split(",");
        if (parts.length == 7) {
            String name = parts[0];
            String description = parts[1];
            double price = Double.parseDouble(parts[2]);
            String size = parts[3];
            double discount = Double.parseDouble(parts[4]);
            boolean isAvailable = Boolean.parseBoolean(parts[5]);
            int quantity = Integer.parseInt(parts[6]);

            Item item = new Item(UUID.randomUUID(), name, description, price, size, discount, isAvailable, quantity);
            itemController.CreateItem(item);

            String response = "Item created successfully: " + item.toString();
            exchange.sendResponseHeaders(201, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Invalid data format. Expected format: name,description,price,size,discount,isAvailable,quantity";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleGetItem(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID itemId = UUID.fromString(query.split("=")[1]);
            System.out.println(itemId);
            Item item = itemController.GetItemById(itemId);

            if (item != null) {
                String response = new Gson().toJson(item);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Item not found";
                exchange.sendResponseHeaders(404, response.getBytes().length); // Not Found
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Invalid or missing item ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleUpdateItem(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID itemId = UUID.fromString(query.split("=")[1]);

            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            String[] parts = requestBody.toString().split(",");
            if (parts.length == 7) {
                String name = parts[0];
                String description = parts[1];
                double price = Double.parseDouble(parts[2]);
                String size = parts[3];
                double discount = Double.parseDouble(parts[4]);
                boolean isAvailable = Boolean.parseBoolean(parts[5]);
                int quantity = Integer.parseInt(parts[6]);

                Item item = new Item(itemId, name, description, price, size, discount, isAvailable, quantity);
                itemController.UpdateItem(itemId, item);

                String response = "Item updated successfully.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Invalid data format. Expected format: name,description,price,size,discount,isAvailable,quantity";
                exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            String response = "Missing or invalid item ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleDeleteItem(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            UUID itemId = UUID.fromString(query.split("=")[1]);

            itemController.DeleteItem(itemId);

            String response = "Item deleted successfully.";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Missing or invalid item ID";
            exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleListItems(HttpExchange exchange) throws IOException {
        List<Item> items = itemController.ListItems();
        if (!items.isEmpty()) {
            String response = new Gson().toJson(items);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "No items found";
            exchange.sendResponseHeaders(404, response.getBytes().length); // Not Found
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
