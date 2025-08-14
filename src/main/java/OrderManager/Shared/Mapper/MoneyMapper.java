package OrderManager.Shared.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public class MoneyMapper {

    @Named("doubleToBigDecimal")
    public BigDecimal doubleToBigDecimal(double v) {
        return BigDecimal.valueOf(v);
    }

    @Named("doubleObjToBigDecimal")
    public BigDecimal doubleObjToBigDecimal(Double v) {
        return v == null ? null : BigDecimal.valueOf(v);
    }

    @Named("bigDecimalToDouble")
    public double bigDecimalToDouble(BigDecimal v) {
        return v == null ? 0.0 : v.doubleValue();
    }
}
