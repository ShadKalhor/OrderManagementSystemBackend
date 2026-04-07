package ordermanager.domain.model;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
