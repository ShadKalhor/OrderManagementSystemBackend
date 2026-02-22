package ordermanager.domain.dto.item;

import java.math.BigDecimal;

public record ItemDto (
        BigDecimal price,
        BigDecimal discount
){
}
