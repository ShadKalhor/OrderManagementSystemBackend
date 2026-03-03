package ordermanager.infrastructure.mapper;


import ordermanager.domain.model.ReservationDomain;
import ordermanager.domain.model.ReservationLineDomain;
import ordermanager.infrastructure.store.persistence.entity.Reservation;
import ordermanager.infrastructure.store.persistence.entity.ReservationLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        uses = { ReservationLineDomain.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReservationMapper {

    @Mapping(source="lines", target = "linesIds")
    ReservationDomain toDomain(Reservation entity);

    @Mapping(source="linesIds",target = "lines")
    Reservation toEntity(ReservationDomain domain);


    default UUID map(ReservationLine line){
        return line == null ? null : line.getId();
    }

    default ReservationLine map(UUID lineId){
        if (lineId == null)
            return null;

        ReservationLine line = new ReservationLine();
        line.setId(lineId);
        return line;

    }


}
