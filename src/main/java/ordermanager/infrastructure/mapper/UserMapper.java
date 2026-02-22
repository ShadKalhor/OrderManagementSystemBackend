package ordermanager.infrastructure.mapper;

import ordermanager.domain.dto.user.CreateUserRequest;
import ordermanager.domain.dto.user.UpdateUserRequest;
import ordermanager.domain.dto.user.UserResponse;
import ordermanager.domain.dto.user.UserSummary;
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
