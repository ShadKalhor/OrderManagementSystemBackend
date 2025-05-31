package OrderManager.Repository;

import OrderManager.Entities.User;
import OrderManager.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {


    Optional<UserRole> findById(UUID id);


    @Query("SELECT r FROM UserRole r WHERE r.roleName = :roleName")
    UserRole GetByRoleName(@Param("roleName") String roleName);

}
