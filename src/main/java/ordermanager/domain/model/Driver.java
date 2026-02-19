package ordermanager.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;


@Setter
@Getter
public class Driver {

    private UUID id;
    private User account;
    private String vehicleNumber;
    private int age;

}
