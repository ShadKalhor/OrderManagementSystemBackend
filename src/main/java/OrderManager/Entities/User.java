package OrderManager.Entities;

import java.util.UUID;
import OrderManager.Entities.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    private final UUID id;
    private UUID roleId;
    private String name;
    private String phone;
    private String password;
    private Utilities.Genders gender;

    // Constructor
    public User(UUID id, UUID roleId, String name, String phone, String password, Utilities.Genders newgender) {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        gender = newgender;
    }


    // Constructor
    public User() {
        this.id = UUID.randomUUID();
    }
    public User(UUID Id){
        this.id = Id;
    }
    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public Utilities.Genders getGender() {
        return gender;
    }

    // Setters
    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(Utilities.Genders newgender) {
        gender = newgender;
    }

    // Optional toString method for better output
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roleId='" + roleId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                '}';
    }
}


