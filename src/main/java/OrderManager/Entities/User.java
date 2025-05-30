package OrderManager.Entities;

import java.util.UUID;
import OrderManager.Entities.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private final UUID id;

    @ManyToOne
    @JoinColumn(name = "roleid")
    private UserRole role;

    private String name;
    private String phone;
    private String password;

    @ManyToOne
    @JoinColumn(name = "genderid") // Make sure this matches your DB column name
    private Gender gender;


    // Constructor
    public User(UUID id, UserRole roleId, String name, String phone, String password, Gender newgender) {
        this.id = id;
        this.role = role;
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

    public UserRole getRole() {
        return role;
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

    public Gender getGender() {
        return gender;
    }

    // Setters
    public void setRoleId(UserRole role) {
        this.role = role;
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

    public void setGender(Gender newgender) {
        gender = newgender;
    }

    // Optional toString method for better output
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roleId='" + role.getRoleName() + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                '}';
    }
}


