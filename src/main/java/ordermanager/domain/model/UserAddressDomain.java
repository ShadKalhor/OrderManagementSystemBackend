package ordermanager.domain.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserAddressDomain {

    private UUID id;
    private UUID userId;

    private String name;
    private String city;
    private String description;
    private String type;
    private String street;
    private String residentialNo;
    private boolean isPrimary;

}
