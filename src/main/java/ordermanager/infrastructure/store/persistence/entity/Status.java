package ordermanager.infrastructure.store.persistence.entity;

public enum Status{
    NotProccessed,
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