package OrderManager.Adapter.out.Persistence;

import OrderManager.Domain.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SpringDataItemRepository extends JpaRepository<Item, UUID> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Item i where i.id in :ids")
    List<Item> findAllForUpdate(@Param("ids") Collection<UUID> ids);

    @Modifying
    @Query("update Item i set i.reserved = i.reserved + :delta where i.id = :itemId")
    void bumpReserved(@Param("itemId") UUID itemId, @Param("delta") int delta);

}
