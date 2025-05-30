package OrderManager.Repository;

import OrderManager.Entities.Gender;
import OrderManager.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {



}
