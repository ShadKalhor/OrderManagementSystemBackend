package OrderManager.Domain.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @Type(type = "uuid-char")
    private UUID id;
    private String name;
    private String description;
    private double price;
    private String size;
    private double discount;
    private boolean isAvailable;
    private int quantity;

}
