package ordermanager.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class DriverDomain {

    private UUID id;

    private UUID accountId;
    private String vehicleNumber;
    private int age;

}
