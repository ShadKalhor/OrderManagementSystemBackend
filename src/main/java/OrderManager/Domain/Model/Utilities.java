package OrderManager.Domain.Model;

public class Utilities {
    public enum Genders{
        Male,
        Female,
        Other,
    }
    public enum UserRoles{
        Member,
        Driver,
        Admin,
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
