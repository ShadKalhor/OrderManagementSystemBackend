package OrderManager.Shared.Dto;

import OrderManager.Domain.Model.Utilities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private UserRoles role;
    private String name;
    private String phone;
    private String password; // consider omitting in responses
    private Genders gender;
}
