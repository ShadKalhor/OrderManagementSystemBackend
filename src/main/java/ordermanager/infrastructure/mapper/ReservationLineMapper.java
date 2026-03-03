package ordermanager.infrastructure.mapper;

import ordermanager.domain.model.ReservationLineDomain;
import ordermanager.infrastructure.store.persistence.entity.ReservationLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReservationLineMapper {


    ReservationLineDomain toDomain(ReservationLine entity);

    @Mapping(target = "reservation", ignore = true)
    ReservationLine toEntity(ReservationLineDomain domain);


}
