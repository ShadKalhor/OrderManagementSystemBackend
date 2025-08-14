package OrderManager.Shared.Mapper;

import OrderManager.Shared.Dto.DriverDtos.CreateDriverRequest;
import OrderManager.Shared.Dto.DriverDtos.DriverResponse;
import OrderManager.Shared.Dto.DriverDtos.UpdateDriverRequest;
import org.mapstruct.*;
import OrderManager.Domain.Model.Driver;
import OrderManager.Domain.Model.User;

@Mapper(
    componentModel = "spring",
    uses = { UserMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    
    Driver toDomain(CreateDriverRequest r);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Driver entity, UpdateDriverRequest r);

    // Expose name from nested account
    @Mapping(source = "account.name", target = "name")
    DriverResponse toResponse(Driver entity);

    // Set nested account.name safely
    @AfterMapping
    default void fillAccountOnCreate(CreateDriverRequest r, @MappingTarget Driver entity) {
        if (r == null) return;
        if (r.name() != null) {
            if (entity.getAccount() == null) entity.setAccount(new User());
            entity.getAccount().setName(r.name());
        }
    }

    @AfterMapping
    default void fillAccountOnUpdate(UpdateDriverRequest r, @MappingTarget Driver entity) {
        if (r == null) return;
        if (r.name() != null) {
            if (entity.getAccount() == null) entity.setAccount(new User());
            entity.getAccount().setName(r.name());
        }
    }
}
