package ordermanager.infrastructure.mapper;

import ordermanager.infrastructure.web.dto.item.CreateItemRequest;
import ordermanager.infrastructure.web.dto.item.ItemResponse;
import ordermanager.infrastructure.web.dto.item.ItemSummary;
import ordermanager.infrastructure.web.dto.item.UpdateItemRequest;
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
    @Mapping(target = "reserved", ignore = true)
    Item update(UpdateItemRequest r);

    @Mapping(source = "available", target = "isAvailable")
    ItemResponse toResponse(Item entity);

    ItemSummary toSummary(Item entity);


    ordermanager.domain.model.Item toDomain(Item item);

    @Mapping(target = "isAvailable", source = "available")
    Item toInfrastructure(ordermanager.domain.model.Item item);

}
