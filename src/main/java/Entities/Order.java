package Entities;

import Entities.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private UUID userId;
    private UUID addressId;
    private UUID driverId;
    private Entities.Utilities.Status status;
    private Entities.Utilities.DeliveryStatus deliveryStatus;
    private List<Entities.OrderItem> items;
    private double subTotal;
    private double deliveryFee;
    private double tax;
    private double totalPrice;
    private String notes;

    public Order() {
        id = UUID.randomUUID();
        addressId = UUID.randomUUID();
        status = Entities.Utilities.Status.Pending;
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

    public Entities.Utilities.Status getStatus() {
        return status;
    }

    public void setStatus(Entities.Utilities.Status status) {
        this.status = status;
    }

    public Entities.Utilities.DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Entities.Utilities.DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public List<Entities.OrderItem> getItems() {
        return items;
    }

    public void setItems(List<Entities.OrderItem> items) {
        this.items = items;
    }
    public void addItem(Entities.OrderItem item) {
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
