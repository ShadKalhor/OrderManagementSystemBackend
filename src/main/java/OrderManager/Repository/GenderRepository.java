package OrderManager.Repository;

import OrderManager.Entities.Gender;
import OrderManager.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

    @Query("SELECT g FROM Gender g WHERE g.Name = :name")
    Gender GetByName(@Param("name") String Name);


}
