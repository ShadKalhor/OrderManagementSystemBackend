package OrderManager.DTO;


import OrderManager.Entities.*;
        import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import OrderManager.Entities.Utilities;
public class OrderDTO {
    private UUID id;
    private UUID userId;
    private UUID addressId;
    private UUID driverId;
    private Utilities.Status status;
    private Utilities.DeliveryStatus deliveryStatus;
    private List<OrderItem> items;
    private double subTotal;
    private double deliveryFee;
    private double tax;
    private double totalPrice;
    private String notes;

    public OrderDTO() {
        id = UUID.randomUUID();
        addressId = UUID.randomUUID();
        status = Utilities.Status.Pending;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
        this.driverId = driverId;
    }

    public Utilities.Status getStatus() {
        return status;
    }

    public void setStatus(Utilities.Status status) {
        this.status = status;
    }

    public Utilities.DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Utilities.DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public void addItem(OrderItem item) {
        if(items == null)
            items = new ArrayList<>();
        this.items.add(item);
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
