package OrderManager.Shared.Mapper;

import OrderManager.Domain.Model.Driver;
import OrderManager.Domain.Model.User;
import OrderManager.Shared.Dto.DriverDto.*;
import OrderManager.Shared.Dto.UserDto;

public class DriverMapper {

    public Driver toDomain(CreateDriverRequest r) {
        Driver d = new Driver();
        d.setVehicleNumber(r.vehicleNumber());
        d.setAge(r.age());
        d.setAccount(toUser(r.account()));
        return d;
    }

    public void applyPatch(Driver target, UpdateDriverRequest r) {
        if (r.vehicleNumber() != null)  target.setVehicleNumber(r.vehicleNumber());
        if (r.age() != null)            target.setAge(r.age());
        if (r.account() != null)        target.setAccount(toUser(r.account()));
    }


    public DriverResponse toResponse(Driver d) {
        return new DriverResponse(
                d.getId(),
                d.getVehicleNumber(),
                d.getAge(),
                toUserDto(d.getAccount())
        );
    }

    private User toUser(UserDto dto) {
        if (dto == null) return null;
        User u = new User();
        u.setName(dto.getName());
        u.setRole(dto.getRole());
        u.setGender(dto.getGender());
        u.setPhone(dto.getPhone());
        return u;
    }
}
