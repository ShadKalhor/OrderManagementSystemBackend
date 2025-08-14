package OrderManager.Shared.Dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddressDto {
    private UUID id;
    private UserDto user;
    private String name;
    private String city;
    private String description;
    private String type;
    private String street;
    private String residentialNo;
    private boolean isPrimary;
}