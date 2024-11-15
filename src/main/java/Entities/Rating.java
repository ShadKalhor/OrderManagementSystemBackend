package Entities;

import java.util.UUID;

public class Rating {
    private UUID id;
    private UUID userId;
    private UUID itemId;
    private double rating;

    public Rating() {}

    public Rating(UUID id, UUID userId, UUID itemId, double rating) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
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

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
