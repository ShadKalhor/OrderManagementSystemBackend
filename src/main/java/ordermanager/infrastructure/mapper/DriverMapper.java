package ordermanager.infrastructure.mapper;

import ordermanager.domain.model.DriverDomain;
import ordermanager.infrastructure.web.dto.driver.CreateDriverRequest;
import ordermanager.infrastructure.web.dto.driver.DriverResponse;
import ordermanager.infrastructure.web.dto.driver.UpdateDriverRequest;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.Driver;
import ordermanager.infrastructure.store.persistence.entity.User;

import java.util.UUID;

@Mapper(
    componentModel = "spring",
    uses = { UserMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "accountId", target = "account")
    Driver create(CreateDriverRequest r);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "accountId", target = "account")
    Driver update(UpdateDriverRequest r);

    @Mapping(target = "name", source = "account.name")
    DriverResponse toResponse(Driver entity);

    @Mapping(source = "account", target = "accountId")
    DriverDomain toDomain(Driver entity);

    @Mapping(source = "accountId", target = "account")
    Driver toEntity(DriverDomain domain);



    default UUID map(User account)    {
        return account == null ? null : account.getId();
    }

    default User map(UUID id) {
        if (id == null) return null;

        User account = new User();
        account.setId(id);
        return account;
    }


    default String nameMap(User account)    {
        return account == null ? null : account.getName();
    }



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
