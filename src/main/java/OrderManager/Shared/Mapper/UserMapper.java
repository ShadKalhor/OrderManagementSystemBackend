package OrderManager.Shared.Mapper;

import OrderManager.Shared.Dto.UserDtos.CreateUserRequest;
import OrderManager.Shared.Dto.UserDtos.UpdateUserRequest;
import OrderManager.Shared.Dto.UserDtos.UserResponse;
import OrderManager.Shared.Dto.UserDtos.UserSummary;
import org.mapstruct.*;
import OrderManager.Domain.Model.User;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toDomain(CreateUserRequest r);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget User entity, UpdateUserRequest r);

    UserResponse toResponse(User entity);
    UserSummary toSummary(User entity);
}
