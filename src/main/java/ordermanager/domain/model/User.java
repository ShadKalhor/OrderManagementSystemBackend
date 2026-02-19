package ordermanager.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Setter
@Getter
public class User {

    private UUID id;
    private Utilities.UserRoles role = Utilities.UserRoles.Member;
    private String name;
    private String phone;
    private String password;
    private Utilities.Genders gender = Utilities.Genders.Other;

}
