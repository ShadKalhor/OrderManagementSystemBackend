package ordermanager.shared.mapper;

import ordermanager.shared.dto.user.CreateUserRequest;
import ordermanager.shared.dto.user.UpdateUserRequest;
import ordermanager.shared.dto.user.UserResponse;
import ordermanager.shared.dto.user.UserSummary;
import org.mapstruct.*;
import ordermanager.domain.model.User;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toDomain(CreateUserRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true) //temp solution
    void update(@MappingTarget User entity, UpdateUserRequest r);

    UserResponse toResponse(User entity);
    UserSummary toSummary(User entity);
}
