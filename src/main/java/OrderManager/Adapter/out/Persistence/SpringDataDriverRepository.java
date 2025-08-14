package OrderManager.Adapter.out.Persistence;


import OrderManager.Domain.Model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataDriverRepository extends JpaRepository<Driver, UUID> {

}
