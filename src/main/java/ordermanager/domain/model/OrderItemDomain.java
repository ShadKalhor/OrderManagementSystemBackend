package ordermanager.domain.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
public class OrderItemDomain {

    private UUID id;

    private UUID itemId;
    private int quantity;
    private BigDecimal totalPrice;

}
