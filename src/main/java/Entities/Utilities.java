package Entities;

public class Utilities {
    public static enum Genders{
        Male,
        Female,
    }
    public enum Status{
        Pending,
        Confirmed,
        Reviewing,
        Approved,
        OutForDelivery,
        Canceled,
        OnHold,
        Returned,
        Refunded,
    }
    public enum DeliveryStatus{
        Pending,
        onWay,
        Delivered,
    }
}
