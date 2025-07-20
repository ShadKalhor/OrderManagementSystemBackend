package OrderManager.Entities;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="UserRole")
public class UserRole {

    @Id/*
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")*/
    @Type(type = "uuid-char")
    private UUID id;

    private String roleName;

    public UserRole() {
    }

    public UserRole(UUID id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
