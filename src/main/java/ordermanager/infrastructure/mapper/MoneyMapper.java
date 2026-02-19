package ordermanager.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public class MoneyMapper {

    @Named("doubleToBigDecimal")
    public BigDecimal doubleToBigDecimal(double v) {
        return BigDecimal.valueOf(v);
    }

}
