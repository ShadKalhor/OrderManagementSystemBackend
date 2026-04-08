package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Option;
import ordermanager.domain.model.OrderItemDomain;
import ordermanager.domain.port.out.OrderItemPersistencePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderItemRepositoryAdapter implements OrderItemPersistencePort {

    @Override
    public List<OrderItemDomain> GetOrderItemsByIds(List<UUID> orderItemIds) {
        return List.of();
    }

    @Override
    public Option<OrderItemDomain> GetById(UUID orderItemId) {
        return null;
    }
}
