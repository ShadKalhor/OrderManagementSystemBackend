package ordermanager.domain.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    //TODO:Seperate Item types with different fields.
    @Id
    @Type(type = "uuid-char")
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
