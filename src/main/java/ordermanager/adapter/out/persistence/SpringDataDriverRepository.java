package ordermanager.adapter.out.persistence;


import ordermanager.domain.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataDriverRepository extends JpaRepository<Driver, UUID> {

}
