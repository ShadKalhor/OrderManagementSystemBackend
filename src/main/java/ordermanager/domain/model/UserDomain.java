package ordermanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UserDomain {

    private UUID id;

    @Builder.Default
    private UserRoles role = UserRoles.Member;

    private String name;
    private String phone;
    private String password;
    @Builder.Default
    private Genders gender = Genders.Other;


}
