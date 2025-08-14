package OrderManager.Shared.Mapper;

import OrderManager.Domain.Model.User;
import OrderManager.Shared.Dto.UserDto;
import OrderManager.Domain.Model.Utilities.*;

public class UserMapper {

    public User toDomain(UserDto.CreateUserRequest r) {
        if (r == null) return null;
        User u = new User();
        u.setName(r.name());
        u.setPhone(r.phone());
        u.setPassword(r.password());
        if (r.gender() != null) u.setGender(r.gender());
        if (r.role() != null) u.setRole(r.role());
        return u;
    }

    public void update(User u, UserDto.UpdateUserRequest r) {
        if (u == null || r == null) return;
        if (r.name() != null) u.setName(r.name());
        if (r.phone() != null) u.setPhone(r.phone());
        if (r.password() != null) u.setPassword(r.password());
        if (r.gender() != null) u.setGender(r.gender());
        if (r.role() != null) u.setRole(r.role());
    }

    public UserDto.UserResponse toResponse(User u) {
        if (u == null) return null;
        return new UserDto.UserResponse(u.getId(), u.getName(), u.getPhone(), u.getRole(), u.getGender());
    }

    public UserDto.UserSummary toSummary(User u) {
        if (u == null) return null;
        return new UserDto.UserSummary(u.getId(), u.getName(), u.getPhone(), u.getRole(), u.getGender());
    }
}
