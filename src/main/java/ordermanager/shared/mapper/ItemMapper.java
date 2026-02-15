package ordermanager.shared.mapper;

import ordermanager.shared.dto.item.CreateItemRequest;
import ordermanager.shared.dto.item.ItemResponse;
import ordermanager.shared.dto.item.ItemSummary;
import ordermanager.shared.dto.item.UpdateItemRequest;
import org.mapstruct.*;
import ordermanager.domain.model.Item;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    Item toDomain(CreateItemRequest r);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    @Mapping(source = "isAvailable", target = "available")
    void update(@MappingTarget Item entity, UpdateItemRequest r);

    @Mapping(source = "available", target = "isAvailable")
    ItemResponse toResponse(Item entity);

    ItemSummary toSummary(Item entity);
}
