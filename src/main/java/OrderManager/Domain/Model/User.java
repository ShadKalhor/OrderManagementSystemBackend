package OrderManager.Domain.Model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import OrderManager.Domain.Model.Utilities.*;

import javax.persistence.*;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Builder.Default
    private UserRoles role = UserRoles.Member;

    private String name;
    private String phone;
    private String password;
    @Builder.Default
    private Genders gender = Genders.Other;


}


