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
    User create(CreateUserRequest r);

    @Mapping(target = "id", ignore = true)
    User update(UpdateUserRequest r);

    UserResponse toResponse(User entity);
    UserSummary toSummary(User entity);



}
