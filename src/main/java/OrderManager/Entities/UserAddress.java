package OrderManager.Entities;

import java.util.UUID;

public class UserAddress {
    private UUID id;
    private UUID userId;
    private String name;
    private String city;
    private String description;
    private String type;
    private String street;
    private String residentialNo;

    public UserAddress() {}

    public UserAddress(UUID id, UUID userId, String name, String city, String description, String type, String street, String residentialNo) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.city = city;
        this.description = description;
        this.type = type;
        this.street = street;
        this.residentialNo = residentialNo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getResidentialNo() {
        return residentialNo;
    }

    public void setResidentialNo(String residentialNo) {
        this.residentialNo = residentialNo;
    }
}
