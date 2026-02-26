package ordermanager.infrastructure.mapper;

import ordermanager.domain.model.UserAddressDomain;
import ordermanager.infrastructure.store.persistence.entity.OrderItem;
import ordermanager.infrastructure.store.persistence.entity.User;
import ordermanager.infrastructure.web.dto.useraddress.CreateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UpdateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UserAddressResponse;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;

import java.util.UUID;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserAddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    UserAddress create(CreateUserAddressRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    UserAddress update(UpdateUserAddressRequest r);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "primary", target = "isPrimary")
    UserAddressResponse toResponse(UserAddress entity);


    @Mapping(source = "user", target = "userId")
    @Mapping(source = "primary", target = "isPrimary")
    UserAddressDomain toDomain(UserAddress entity);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "primary", target = "isPrimary")
    UserAddress toEntity(UserAddressDomain domain);


    default UUID map(User user)    {
        return user == null ? null : user.getId();
    }

    default User map(UUID id) {
        if (id == null) return null;

        User user = new User();
        user.setId(id);
        return user;
    }





}
