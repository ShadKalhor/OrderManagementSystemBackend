package ordermanager.infrastructure.mapper;

import ordermanager.infrastructure.web.dto.useraddress.CreateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UpdateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UserAddressResponse;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserAddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    UserAddress toDomain(CreateUserAddressRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "primary", ignore = true) //temp solution
    UserAddress update(UpdateUserAddressRequest r);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "primary", target = "isPrimary")
    UserAddressResponse toResponse(UserAddress entity);
}
