package OrderManager.DTO;

import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String name;
    private String phone;
    private String password;
    private UUID roleId;
    private int genderId;

    // Default constructor
    public UserDTO() {}

    // Getters
    public UUID getId() {
        return id;
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

    public UUID getRoleId() {
        return roleId;
    }

    public int getGenderId() {
        return genderId;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
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

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }
}
