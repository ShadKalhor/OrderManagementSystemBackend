package ordermanager.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
public class UserAddress {

    private UUID id;
    private User user;
    private String name;
    private String city;
    private String description;
    private String type;
    private String street;
    private String residentialNo;
    private boolean isPrimary;

    //TODO: check y aw constructor a bkawa lo lajyati isprimary id haya la signiture.
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
