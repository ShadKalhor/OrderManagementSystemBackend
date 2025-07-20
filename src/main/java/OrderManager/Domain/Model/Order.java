package OrderManager.Entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @OneToOne
    @JoinColumn(name = "addressId")
    private UserAddress address;

    @ManyToOne
    @JoinColumn(name = "driverId")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private Utilities.Status status;

    @Enumerated(EnumType.STRING)
    private Utilities.DeliveryStatus deliveryStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    private double subTotal;
    private double deliveryFee;
    private double tax;
    private double totalPrice;
    private String notes;

    public Order() {
        status = Utilities.Status.Pending;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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
