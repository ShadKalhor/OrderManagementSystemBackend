package ordermanager.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Setter
@Getter
public class Item {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String size;
    private BigDecimal discount;
    private boolean isAvailable;
    private int quantity;
    private int reserved;
}
