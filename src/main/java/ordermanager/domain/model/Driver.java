package ordermanager.domain.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }


}

