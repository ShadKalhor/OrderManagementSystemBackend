package ordermanager.domain.model;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Driver {
    @Id
    @Type(type = "uuid-char")
    private UUID id;
    @OneToOne
    @JoinColumn(name = "userId")
    private User account;
    private String vehicleNumber;
    private int age;

}
