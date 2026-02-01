package OrderManager.Adapter.out.Persistence;

import OrderManager.Application.Port.out.InventoryReservationPort;
import OrderManager.Application.Port.out.ReservationResult;
import OrderManager.Domain.Model.Item;
import OrderManager.Domain.Model.OrderItem;
import OrderManager.Domain.Model.Reservation;
import OrderManager.Domain.Model.ReservationLine;
import OrderManager.Exception.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import OrderManager.Domain.Model.Utilities.ReservationStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InventoryReservationAdapter implements InventoryReservationPort {

    private final SpringDataItemRepository itemRepo;
    private final SpringDataReservationRepository resRepo;
    private final SpringDataReservationLineRepository lineRepo;

    public InventoryReservationAdapter(SpringDataItemRepository itemRepo,
                                       SpringDataReservationRepository resRepo,
                                       SpringDataReservationLineRepository lineRepo) {
        this.itemRepo = itemRepo;
        this.resRepo = resRepo;
        this.lineRepo = lineRepo;
    }

    @Override
    @Transactional
    public ReservationResult reserveItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return new ReservationResult.Failure(List.of());
        }

        var itemIds = items.stream().map(oi -> oi.getItem().getId()).toList();

        Map<UUID, Item> byId = itemRepo.findAllForUpdate(itemIds).stream()
                .collect(Collectors.toMap(Item::getId, x -> x));

        List<ReservationResult.UnavailableLine> shortages = new ArrayList<>();
        for (OrderItem oi : items) {
            Item inv = byId.get(oi.getItem().getId());
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

        for (OrderItem oi : items) {
            lineRepo.save(new ReservationLine(res, oi.getItem().getId(), oi.getQuantity()));
            itemRepo.bumpReserved(oi.getItem().getId(), oi.getQuantity());
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
    public Reservation FindReservationById(UUID reservationId) {
        Optional<Reservation> reservation = resRepo.findById(reservationId);
        if(reservation.isPresent())
            return reservation.get();

        throw new EntityNotFoundException("Reservation",reservationId);
    }
}
