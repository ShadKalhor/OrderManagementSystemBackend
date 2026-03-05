package ordermanager.infrastructure.store.persistence.adapter;

import io.vavr.control.Either;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.OrderItemDomain;
import ordermanager.domain.model.ReservationDomain;
import ordermanager.domain.model.ReservationStatus;
import ordermanager.domain.port.out.InventoryReservationPort;
import ordermanager.domain.model.ReservationResult;
import ordermanager.infrastructure.mapper.ReserveMapper;
import ordermanager.infrastructure.store.persistence.entity.*;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataItemRepository;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataReservationLineRepository;
import ordermanager.infrastructure.store.persistence.jpa.SpringDataReservationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InventoryReservationAdapter implements InventoryReservationPort {

    private final SpringDataItemRepository itemRepo;
    private final SpringDataReservationRepository resRepo;
    private final SpringDataReservationLineRepository lineRepo;
    private final ReserveMapper reserveMapper;

    public InventoryReservationAdapter(SpringDataItemRepository itemRepo,
                                       SpringDataReservationRepository resRepo,
                                       SpringDataReservationLineRepository lineRepo,
                                       ReserveMapper reserveMapper) {
        this.itemRepo = itemRepo;
        this.resRepo = resRepo;
        this.lineRepo = lineRepo;
        this.reserveMapper = reserveMapper;
    }

    @Override
    @Transactional
    public ReservationResult reserveItems(List<OrderItemDomain> items) {


        if (items == null || items.isEmpty()) {
            return new ReservationResult.Failure(List.of());
        }

        var itemIds = items.stream().map(OrderItemDomain::getItemId).toList();

        //Map y item ba key, pashan check krdnawa w xstni item y be stock lo shortages.
        //check kawa la ruy efficiency
        Map<UUID, Item> byId = itemRepo.findAllForUpdate(itemIds).stream()
                .collect(Collectors.toMap(Item::getId, x -> x));

        List<ReservationResult.UnavailableLine> shortages = new ArrayList<>();
        for (OrderItemDomain oi : items) {
            Item inv = byId.get(oi.getItemId());
            int available = inv.getQuantity() - inv.getReserved();
            if (oi.getQuantity() > available) {
                shortages.add(new ReservationResult.UnavailableLine(inv.getId(), oi.getQuantity(), available));
            }
        }
        if (!shortages.isEmpty()) return new ReservationResult.Failure(shortages);

        UUID resId = UUID.randomUUID();
        Reservation res = new Reservation();
        res.setId(resId);
        res.setStatus(ReservationStatus.HELD);
        res.setExpiresAt(Instant.now().plus(Duration.ofMinutes(15)));
        res = resRepo.save(res);

        for (OrderItemDomain oi : items) {
            lineRepo.save(new ReservationLine(res, oi.getItemId(), oi.getQuantity()));
            itemRepo.bumpReserved(oi.getItemId(), oi.getQuantity());
        }

        return new ReservationResult.Success(resId);
    }

    @Override
    @Transactional
    public void releaseReservation(UUID reservationId) {
        var res = resRepo.findById(reservationId).orElse(null);
        if (res == null || res.getStatus() != ReservationStatus.HELD) return;

        var itemIds = res.getLines().stream().map(ReservationLine::getItemId).toList();
        itemRepo.findAllForUpdate(itemIds);

        for (var line : res.getLines()) {
            itemRepo.bumpReserved(line.getItemId(), -line.getQty());
        }
        res.setStatus(ReservationStatus.RELEASED);
        resRepo.save(res);
    }

    @Override
    @Transactional
    public void confirmReservation(UUID reservationId) {
        var res = resRepo.findById(reservationId).orElseThrow();
        if (res.getStatus() != ReservationStatus.HELD) return;

        var itemIds = res.getLines().stream().map(ReservationLine::getItemId).toList();
        var invById = itemRepo.findAllForUpdate(itemIds).stream()
                .collect(Collectors.toMap(Item::getId, x -> x));

        for (var line : res.getLines()) {
            Item inv = invById.get(line.getItemId());
            inv.setReserved(inv.getReserved() - line.getQty());
            inv.setQuantity(inv.getQuantity() - line.getQty());

        }
        res.setStatus(ReservationStatus.COMMITTED);
        resRepo.save(res);
    }

    @Override
    public Either<StructuredError, ReservationDomain> FindReservationById(UUID reservationId) {
        ReservationDomain reservation = reserveMapper.toDomain(resRepo
                .findOptionById(reservationId).toEither(() -> new StructuredError("Reservation Not Found", ErrorType.NOT_FOUND_ERROR))
                .get());
        return Either.right(reservation);
    }
}
