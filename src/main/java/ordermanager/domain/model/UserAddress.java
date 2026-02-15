package ordermanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Builder
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


    public UserAddress(){
        this.id = UUID.randomUUID();
    }

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
}
