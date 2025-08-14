package OrderManager.Shared.Mapper;

import OrderManager.Shared.Dto.UserAddressDtos.CreateUserAddressRequest;
import OrderManager.Shared.Dto.UserAddressDtos.UpdateUserAddressRequest;
import OrderManager.Shared.Dto.UserAddressDtos.UserAddressResponse;
import org.mapstruct.*;
import OrderManager.Domain.Model.UserAddress;

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
    void update(@MappingTarget UserAddress entity, UpdateUserAddressRequest r);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "primary", target = "isPrimary")
    UserAddressResponse toResponse(UserAddress entity);
}
