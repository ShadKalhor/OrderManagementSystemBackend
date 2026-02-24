package ordermanager.infrastructure.web.dto.item;

import java.math.BigDecimal;

public record ItemDto (
        BigDecimal price,
        BigDecimal discount
){
}
