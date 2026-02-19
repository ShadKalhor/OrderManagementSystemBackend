package ordermanager.shared.mapper;

import ordermanager.shared.dto.useraddress.CreateUserAddressRequest;
import ordermanager.shared.dto.useraddress.UpdateUserAddressRequest;
import ordermanager.shared.dto.useraddress.UserAddressResponse;
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
    void update(@MappingTarget UserAddress entity, UpdateUserAddressRequest r);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "primary", target = "isPrimary")
    UserAddressResponse toResponse(UserAddress entity);
}
