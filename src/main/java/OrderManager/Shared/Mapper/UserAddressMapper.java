package OrderManager.Shared.Mapper;

import OrderManager.Domain.Model.User;
import OrderManager.Domain.Model.UserAddress;
import OrderManager.Shared.Dto.UserAddressDto;
import java.util.UUID;

public class UserAddressMapper {

    public UserAddress toDomain(UserAddressDto.CreateUserAddressRequest r) {
        if (r == null) return null;
        UserAddress a = new UserAddress();
        if (r.userId() != null) {
            User u = new User();
            u.setId(r.userId());
            a.setUser(u);
        }
        a.setName(r.name());
        a.setCity(r.city());
        a.setDescription(r.description());
        a.setType(r.type());
        a.setStreet(r.street());
        a.setResidentialNo(r.residentialNo());
        a.setPrimary(r.isPrimary());
        return a;
    }

    public void update(UserAddress a, UserAddressDto.UpdateUserAddressRequest r) {
        if (a == null || r == null) return;
        if (r.userId() != null) {
            User u = new User();
            u.setId(r.userId());
            a.setUser(u);
        }
        if (r.name() != null) a.setName(r.name());
        if (r.city() != null) a.setCity(r.city());
        if (r.description() != null) a.setDescription(r.description());
        if (r.type() != null) a.setType(r.type());
        if (r.street() != null) a.setStreet(r.street());
        if (r.residentialNo() != null) a.setResidentialNo(r.residentialNo());
        if (r.isPrimary() != null) a.setPrimary(r.isPrimary());
    }

    public UserAddressDto.UserAddressResponse toResponse(UserAddress a) {
        if (a == null) return null;
        UUID userId = a.getUser() != null ? a.getUser().getId() : null;
        return new UserAddressDto.UserAddressResponse(
            a.getId(), userId,
            a.getName(), a.getCity(), a.getDescription(), a.getType(),
            a.getStreet(), a.getResidentialNo(),
            a.isPrimary()
        );
    }
}
