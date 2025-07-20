package OrderManager.DTO;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

public class DriverDTO {
    private UUID id;
    private String name;
    private String phone;
    private String vehicleNumber;
    private int age;

    public DriverDTO() {}

    public DriverDTO(UUID id, String name, String phone, String vehicleNumber, int age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.vehicleNumber = vehicleNumber;
        this.age = age;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return
                "id=" + id +
                        ", name='" + name + '\'' +
                        ", phone='" + phone + '\'' +
                        ", vehicleNumber=" + vehicleNumber +
                        ", age='" + age + '\n'
                ;
    }
}




