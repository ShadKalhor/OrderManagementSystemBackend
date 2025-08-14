package OrderManager.Shared.Dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddressDto {
    private UUID id;

    @Valid // 👈 enables validation of the nested object's annotations
    @NotNull(message = "User is required")
    private UserDto user;

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String description;

    @NotBlank
    private String type;

    @NotBlank
    private String street;

    @NotBlank
    private String residentialNo;

    private boolean isPrimary;


    public record CreateUserAddressRequest(
            @NotNull UUID userId,
            @NotBlank String name,
            @NotBlank String city,
            @Size(max=255) String description,
            @NotBlank String type,
            @NotBlank String street,
            @NotBlank String residentialNo,
            boolean isPrimary
    ) {}

    public record UpdateUserAddressRequest(
            UUID userId,
            String name,
            String city,
            String description,
            String type,
            String street,
            String residentialNo,
            Boolean isPrimary
    ) {}

    public record UserAddressResponse(
            UUID id,
            UUID userId,
            String name, String city, String description, String type, String street, String residentialNo,
            boolean isPrimary
    ) {}
}