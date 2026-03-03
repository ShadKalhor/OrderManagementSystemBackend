package ordermanager.domain.port.out;

import io.vavr.control.Option;
import ordermanager.domain.model.OrderItemDomain;

import java.util.List;
import java.util.UUID;

public interface OrderItemPersistencePort {

    List<OrderItemDomain> GetOrderItemsByIds (List<UUID> orderItemIds);

    Option<OrderItemDomain> GetById(UUID orderItemId);

}
