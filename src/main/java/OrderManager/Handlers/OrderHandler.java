package OrderManager.Handlers;

import Controllers.OrderController;
import Entities.Order;
import Entities.OrderItem;
import Entities.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class OrderHandler implements HttpHandler {
    private final OrderController orderController = new OrderController();
    private static final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method.toUpperCase()) {
                case "POST":
                    if (path.equals("/order/create")) {
                        handleCreateOrder(exchange);
                    } else if (path.equals("/order/addItemToPendingOrder")) {
                        handleAddItemToPendingOrder(exchange);
                    } else if (path.equals("/order/placePendingOrder")) {
                        handlePlacePendingOrder(exchange);
                    } else {
                        sendResponse(exchange, 404, "Not Found");
                    }
                    break;
                case "GET":
                    if (path.equals("/order/get")) {
                        handleGetOrder(exchange);
                    } else if (path.equals("/order/printCart")) {
                        handlePrintCart(exchange);
                    } else {
                        sendResponse(exchange, 404, "Not Found");
                    }
                    break;
                case "PUT":
                    if (path.equals("/order/update")) {
                        handleUpdateOrder(exchange);
                    } else {
                        sendResponse(exchange, 404, "Not Found");
                    }
                    break;
                case "DELETE":
                    if (path.equals("/order/delete")) {
                        handleDeleteOrder(exchange);
                    } else {
                        sendResponse(exchange, 404, "Not Found");
                    }
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private void handleCreateOrder(HttpExchange exchange) throws IOException {
        // Placeholder for order creation logic
        sendResponse(exchange, 501, "Create order not implemented.");
    }

    private void handleGetOrder(HttpExchange exchange) throws IOException {
        // Placeholder for get order logic
        sendResponse(exchange, 501, "Get order not implemented.");
    }

    private void handleUpdateOrder(HttpExchange exchange) throws IOException {
        // Placeholder for update order logic
        sendResponse(exchange, 501, "Update order not implemented.");
    }

    private void handleDeleteOrder(HttpExchange exchange) throws IOException {
        // Placeholder for delete order logic
        sendResponse(exchange, 501, "Delete order not implemented.");
    }

    private void handleAddItemToPendingOrder(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        OrderRequest orderRequest = gson.fromJson(requestBody, OrderRequest.class);

        if (orderRequest != null && orderRequest.userId != null && orderRequest.items != null) {
            // Process the request
            String response = "Item added to pending order.";
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 400, "Invalid request data.");
        }
    }

    private void handlePrintCart(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("userId=")) {
            UUID userId = UUID.fromString(query.split("=")[1]);
            User user = new User(userId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);

            orderController.PrintCart(user);

            System.out.flush();
            System.setOut(old);

            String response = baos.toString();
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 400, "Invalid or missing user ID");
        }
    }

    private void handlePlacePendingOrder(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        OrderRequest orderRequest = gson.fromJson(requestBody, OrderRequest.class);

        if (orderRequest.userId != null && orderRequest.items != null) {
            User user = new User(orderRequest.userId);
            orderController.PlacePendingOrder(user.getId(), orderRequest.items);
            sendResponse(exchange, 200, "Pending order placed successfully.");
        } else {
            sendResponse(exchange, 400, "Invalid data format. Expected JSON with userId and items list.");
        }
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // Helper class to parse JSON data for userId and items list
    static class OrderRequest {
        UUID userId;
        List<OrderItem> items;
    }
}
