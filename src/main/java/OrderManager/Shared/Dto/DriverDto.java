package OrderManager.Shared.Dto;


import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDto {
    private UUID id;
    private UserDto account;
    private String vehicleNumber;
    private int age;
}