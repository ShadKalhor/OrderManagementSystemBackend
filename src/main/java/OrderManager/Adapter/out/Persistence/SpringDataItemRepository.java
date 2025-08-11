package OrderManager.Adapter.out.Persistence;

import OrderManager.Domain.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataItemRepository extends JpaRepository<Item, UUID> {

}
