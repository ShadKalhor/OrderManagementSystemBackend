package ordermanager.infrastructure.service;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.ItemDomain;
import ordermanager.domain.model.OrderDomain;
import ordermanager.domain.model.OrderItemDomain;
import ordermanager.domain.port.out.OrderItemPersistencePort;
import ordermanager.infrastructure.web.dto.order.OrderDto;
import ordermanager.domain.port.out.InventoryReservationPort;
import ordermanager.domain.port.out.OrderPersistencePort;
import ordermanager.domain.port.out.ReservationResult;
import ordermanager.domain.service.OrderPricingService;
import ordermanager.infrastructure.exception.EntityNotFoundException;
import ordermanager.infrastructure.exception.InsufficientInventoryException;
import ordermanager.infrastructure.mapper.OrderMapper;
import ordermanager.infrastructure.store.persistence.entity.Item;
import ordermanager.infrastructure.store.persistence.entity.Order;
import ordermanager.infrastructure.store.persistence.entity.OrderItem;
import ordermanager.domain.model.Status;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {

    private final ItemService itemService;
    private final OrderPricingService orderPricingService;
    private final OrderPersistencePort orderPort;
    private final InventoryReservationPort inventoryReservationPort;
    private final OrderItemPersistencePort orderItemPort;

    public OrderService( ItemService itemService, OrderPricingService orderPricingService,
                         OrderPersistencePort orderPort,
                         InventoryReservationPort inventoryReservationPort,
                         OrderItemPersistencePort orderItemPort){
        this.itemService = itemService;
        this.orderPricingService = orderPricingService;
        this.orderPort = orderPort;
        this.inventoryReservationPort = inventoryReservationPort;
        this.orderItemPort = orderItemPort;
    }


    public Either<StructuredError, OrderDomain> CreateOrder(OrderDomain draft) {
        List<OrderItem> orderItems = Objects.requireNonNull(hydrateItems(draft))
                .toEither(new StructuredError("OrderItems Not Found",
                        ErrorType.NOT_FOUND_ERROR)).get();


        //reserve y item akan daka gar hatu item habu unavailable bu result dabta instance ak la ReservationResult.Failure
        var result = inventoryReservationPort.reserveItems(orderItems);
        if (result instanceof ReservationResult.Failure f) {
            throw new InsufficientInventoryException(f.lines());
        }
        var reservationId = ((ReservationResult.Success) result).reservationId();

        return Try.of(()->{

            OrderDomain calculationResult = orderPricingService.ApplyPricing(draft);

            draft.setSubTotal(calculationResult.getSubTotal());
            draft.setDeliveryFee(calculationResult.getDeliveryFee());
            draft.setTax(calculationResult.getTax());
            draft.setTotalPrice(calculationResult.getTotalPrice());

            draft.setReservationId(inventoryReservationPort.FindReservationById(reservationId).getId());
            draft.setStatus(Status.Pending);
            return orderPort.save(draft);
        }).onFailure(ex -> inventoryReservationPort.releaseReservation(reservationId))
            .getOrElseGet(ex -> Either.left(new StructuredError(ex.getMessage(), ErrorType.SERVER_ERROR)));

    }


    //dastkari item&price detail nakre.
    public Either<StructuredError, OrderDomain> UpdateOrder(UUID orderId, Order patchedOrder){

        Option<OrderDomain> orderExists = orderPort.findById(orderId);
        if(orderExists.isEmpty())
            throw new EntityNotFoundException("Order Not Found with Id ", orderId);
        OrderDomain order = orderExists.get();

        order.setAddressId(patchedOrder.getAddress().getId());
        order.setDriverId(patchedOrder.getDriver().getId());
        order.setStatus(patchedOrder.getStatus());
        order.setDeliveryStatus(patchedOrder.getDeliveryStatus());
        order.setNotes(patchedOrder.getNotes());

        return orderPort.save(order);
    }


    public List<OrderDomain> GetAllOrders(){
        return orderPort.findAll();
    }


    public Either<StructuredError, OrderDomain> GetOrderById(UUID orderId){
        return orderPort.findById(orderId).toEither(() -> new StructuredError("Order Not Found", ErrorType.NOT_FOUND_ERROR));
    }


    public List<OrderDomain> GetByUserId(UUID userId) {
        return orderPort.findByUserId(userId);
    }


    public Either<StructuredError, Void> DeleteOrder(UUID orderId){
        return orderPort.findById(orderId).toEither(() -> new StructuredError("Order Not Found", ErrorType.NOT_FOUND_ERROR)).map(existing -> {
            orderPort.deleteById(orderId);
            return null;
        });
    }

    private Either<StructuredError, List<OrderItem>> hydrateItems(OrderDomain order) {
        for (UUID orderItemId : order.getItemIds()) {
            OrderItemDomain orderItemDomain = orderItemPort.GetById(orderItemId).getOrNull();
            if (orderItemDomain == null)
                return Either.left(new StructuredError("Order Not Found",ErrorType.NOT_FOUND_ERROR));
            UUID itemId = orderItemDomain.getItemId();

            ItemDomain item = itemService.GetItemById(itemId)
                    .getOrElseThrow(() -> new EntityNotFoundException("Item", itemId));

            orderItemDomain.setItemId(item.getId());
        }
        return null;
    }

}


