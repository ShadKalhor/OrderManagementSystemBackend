package OrderManager.Entities;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class UserAddress {
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String name;
    private String city;
    private String description;
    private String type;
    private String street;
    private String residentialNo;

    @Column(name = "isPrimary")
    private boolean isPrimary;
    public UserAddress() {}

    public UserAddress(UUID id, User user, String name, String city, String description, String type, String street, String residentialNo) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    public Boolean getIsPrimary() {return isPrimary;}

    public void setIsPrimary(boolean isPrimary) {this.isPrimary = isPrimary;}
}
