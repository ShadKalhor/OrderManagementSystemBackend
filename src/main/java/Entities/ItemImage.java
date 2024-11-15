package OrderManager.Entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class ItemImage {
    private UUID id;
    private UUID itemId;
    private String imageURL;
    private Boolean isPrimary;
    private LocalDateTime uploadedAt;

    public ItemImage() {}

    public ItemImage(UUID id, UUID itemId, String imageURL, Boolean isPrimary, LocalDateTime uploadedAt) {
        this.id = id;
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.isPrimary = isPrimary;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
