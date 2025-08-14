package OrderManager.Shared.Dto;


import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private String size;
    private double discount;
    private boolean isAvailable;
    private int quantity;
}

