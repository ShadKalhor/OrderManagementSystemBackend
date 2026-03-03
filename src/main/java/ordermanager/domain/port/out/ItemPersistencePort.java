package ordermanager.domain.port.out;

import io.vavr.control.Either;
import io.vavr.control.Option;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.ItemDomain;


import java.util.List;
import java.util.UUID;

public interface ItemPersistencePort {


    Option<ItemDomain> findById(UUID id);

    Either<StructuredError, ItemDomain> save(ItemDomain item);

    List<ItemDomain> findAll();

    Either<StructuredError, Void> deleteById(UUID itemId);
}
