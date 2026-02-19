package ordermanager.infrastructure.mapper;

import ordermanager.infrastructure.web.dto.user.CreateUserRequest;
import ordermanager.infrastructure.web.dto.user.UpdateUserRequest;
import ordermanager.infrastructure.web.dto.user.UserResponse;
import ordermanager.infrastructure.web.dto.user.UserSummary;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.User;

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
