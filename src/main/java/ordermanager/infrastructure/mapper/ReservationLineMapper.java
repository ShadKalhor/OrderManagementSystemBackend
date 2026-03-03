package ordermanager.infrastructure.mapper;

import ordermanager.domain.model.ReservationLineDomain;
import ordermanager.infrastructure.store.persistence.entity.ReservationLine;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReservationLineMapper {

    ReservationLineDomain toDomain(ReservationLine entity);

    ReservationLine toEntity(ReservationLineDomain domain);


}
