package OrderManager.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Data
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

}
