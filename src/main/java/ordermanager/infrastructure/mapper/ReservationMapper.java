package ordermanager.infrastructure.mapper;


import ordermanager.domain.model.ReservationDomain;
import ordermanager.domain.model.ReservationLineDomain;
import ordermanager.infrastructure.store.persistence.entity.Reservation;
import ordermanager.infrastructure.store.persistence.entity.ReservationLine;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = { ReservationLineDomain.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReservationMapper {


    ReservationDomain toDomain(Reservation entity);

    Reservation toEntity(ReservationLine domain);


}
