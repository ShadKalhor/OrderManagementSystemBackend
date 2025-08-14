package OrderManager.Shared.Mapper;

import OrderManager.Shared.Dto.ItemDtos.CreateItemRequest;
import OrderManager.Shared.Dto.ItemDtos.ItemResponse;
import OrderManager.Shared.Dto.ItemDtos.ItemSummary;
import OrderManager.Shared.Dto.ItemDtos.UpdateItemRequest;
import org.mapstruct.*;
import OrderManager.Domain.Model.Item;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    Item toDomain(CreateItemRequest r);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Item entity, UpdateItemRequest r);

    @Mapping(source = "available", target = "isAvailable")
    ItemResponse toResponse(Item entity);

    ItemSummary toSummary(Item entity);
}
