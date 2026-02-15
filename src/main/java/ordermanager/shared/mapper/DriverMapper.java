package ordermanager.shared.mapper;

import ordermanager.shared.dto.driver.CreateDriverRequest;
import ordermanager.shared.dto.driver.DriverResponse;
import ordermanager.shared.dto.driver.UpdateDriverRequest;
import org.mapstruct.*;
import ordermanager.domain.model.Driver;
import ordermanager.domain.model.User;

@Mapper(
    componentModel = "spring",
    uses = { UserMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "accountId", target = "account.id")
    Driver toDomain(CreateDriverRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "accountId", target = "account.id")
    void update(@MappingTarget Driver entity, UpdateDriverRequest r);

    @Mapping(target = "name", source = "account.name")
    DriverResponse toResponse(Driver entity);


    /*@AfterMapping
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
    }*/

    @AfterMapping
    default void fillAccountOnCreate(CreateDriverRequest r, @MappingTarget Driver entity) {
        if (r == null) return;
        if (r.accountId() != null || notBlank(r.name())) {
            if (entity.getAccount() == null) {
                entity.setAccount(new User());
            }
        }
        if (r.accountId() != null) {
            ensureUser(entity).setId(r.accountId());
        }
        if (notBlank(r.name())) {
            ensureUser(entity).setName(r.name().trim());
        }
    }

    @AfterMapping
    default void fillAccountOnUpdate(UpdateDriverRequest r, @MappingTarget Driver entity) {
        if (r == null) return;
        if (r.accountId() != null || notBlank(r.name())) {
            if (entity.getAccount() == null) {
                entity.setAccount(new User());
            }
        }
        if (r.accountId() != null) {
            ensureUser(entity).setId(r.accountId());
        }
        if (notBlank(r.name())) {
            ensureUser(entity).setName(r.name().trim());
        }
    }

    private User ensureUser(Driver entity) {
        if (entity.getAccount() == null) {
            entity.setAccount(new User());
        }
        return entity.getAccount();
    }

    private boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

}
