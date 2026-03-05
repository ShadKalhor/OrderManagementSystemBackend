package ordermanager.infrastructure.mapper;

import ordermanager.domain.model.ReservationDomain;
import ordermanager.domain.model.ReservationLineDomain;
import ordermanager.infrastructure.store.persistence.entity.Reservation;
import ordermanager.infrastructure.store.persistence.entity.ReservationLine;
import ordermanager.infrastructure.store.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReserveMapper {

    @Mapping(source = "lines", target = "linesIds")
    ReservationDomain toDomain(Reservation entity);

    @Mapping(source = "linesIds", target = "lines")
    Reservation toEntity(ReservationDomain domain);

    ReservationLineDomain lineToDomain(ReservationLine lineEntity);

    @Mapping(target = "reservation", ignore = true)
    ReservationLine lineToEntity(ReservationLineDomain lineDomain);


    default UUID map(ReservationLine reservationLine)    {
        return reservationLine == null ? null : reservationLine.getId();
    }

    default ReservationLine map(UUID id) {
        if (id == null) return null;

        ReservationLine reservationLine = new ReservationLine();
        reservationLine.setId(id);
        return reservationLine;
    }



}
