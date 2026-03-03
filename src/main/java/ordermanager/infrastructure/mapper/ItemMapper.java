package ordermanager.infrastructure.mapper;

import liquibase.pro.packaged.M;
import ordermanager.domain.model.ItemDomain;
import ordermanager.infrastructure.web.dto.driver.CreateDriverRequest;
import ordermanager.infrastructure.web.dto.item.*;
import org.mapstruct.*;
import ordermanager.infrastructure.store.persistence.entity.Item;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE

)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    Item create(CreateItemRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(source="isAvailable", target = "available")
    @Mapping(target = "reserved", ignore = true)
    ItemDomain createDomain(CreateItemRequest r);


    @Mapping(target = "id", ignore = true)
    @Mapping(source="isAvailable", target = "available")
    @Mapping(target = "reserved", ignore = true)
    ItemDomain updateDomain(UpdateItemRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    @Mapping(source="isAvailable", target = "isAvailable")
    Item update(UpdateItemRequest r);

    @Mapping(source = "available", target = "isAvailable")
    ItemResponse toResponse(Item entity);

    @Mapping(source="available", target = "isAvailable")
    ItemResponse domainToResponse(ItemDomain domain);

    ItemSummary toSummary(Item entity);



    ItemDomain toDomain(Item entity);

    @Mapping(source="available", target = "isAvailable")
    Item toEntity(ItemDomain domain);


}
