package Entities;
import java.util.Arrays;
import java.util.UUID;

public class Item {
    private final UUID id;         // Make this final if you want it to be immutable
    private String name;
    private String description;
    private double price;
    private String size;
    private double discount;
    private boolean isAvailable;
    private int quantity;

    // Constructor
    public Item(UUID id, String name, String description, double price, String size, double discount, boolean isAvailable, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.discount = discount;
        this.isAvailable = isAvailable;
        this.quantity = quantity;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    // Override toString() for meaningful output
    @Override
    public String toString() {
        return
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", isAvailable=" + isAvailable + '\n'
                ;
    }
}
