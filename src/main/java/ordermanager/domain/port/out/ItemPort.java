package ordermanager.domain.port.out;

import io.vavr.control.Option;
import ordermanager.domain.model.ItemDomain;

import java.util.UUID;

public interface ItemPort {

    Option<ItemDomain> findById(UUID id);

}
