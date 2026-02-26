package ordermanager.infrastructure.service;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
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
import java.util.UUID;

@Service
public class OrderService {

    private final ItemService itemService;
    private final OrderPricingService orderPricingService;
    private final OrderPersistencePort orderPort;
    private final InventoryReservationPort inventoryReservationPort;
    private final OrderMapper orderMapper;


    public OrderService( ItemService itemService, OrderPricingService orderPricingService,
                         OrderPersistencePort orderPort,
                         InventoryReservationPort inventoryReservationPort, OrderMapper orderMapper){
        this.itemService = itemService;
        this.orderPricingService = orderPricingService;
        this.orderPort = orderPort;
        this.inventoryReservationPort = inventoryReservationPort;
        this.orderMapper = orderMapper;
    }


    public Either<StructuredError, Order> CreateOrder(Order draft) {
        hydrateItems(draft);

        //reserve y item akan daka gar hatu item habu unavailable bu result dabta instance ak la ReservationResult.Failure
        var result = inventoryReservationPort.reserveItems(draft.getItems());
        if (result instanceof ReservationResult.Failure f) {
            throw new InsufficientInventoryException(f.lines());
        }
        var reservationId = ((ReservationResult.Success) result).reservationId();

        return Try.of(()->{
            OrderDto orderDto = orderMapper.toOrderDto(draft);
            OrderDto calculationResult = orderPricingService.ApplyPricing(orderDto);

            draft.setSubTotal(calculationResult.subTotal());
            draft.setDeliveryFee(calculationResult.deliveryFee());
            draft.setTax(calculationResult.tax());
            draft.setTotalPrice(calculationResult.totalPrice());

            draft.setReservation(inventoryReservationPort.FindReservationById(reservationId));
            draft.setStatus(Status.Pending);
            return orderPort.save(draft);
        }).onFailure(ex -> inventoryReservationPort.releaseReservation(reservationId))
            .getOrElseGet(ex -> Either.left(new StructuredError(ex.getMessage(), ErrorType.SERVER_ERROR)));

    }


    //dastkari item&price detail nakre.
    public Either<StructuredError, Order> UpdateOrder(UUID orderId, Order patchedOrder){

        Option<Order> orderExists = orderPort.findById(orderId);
        if(orderExists.isEmpty())
            throw new EntityNotFoundException("Order Not Found with Id ", orderId);
        Order order = orderExists.get();

        order.setAddress(patchedOrder.getAddress());
        order.setDriver(patchedOrder.getDriver());
        order.setStatus(patchedOrder.getStatus());
        order.setDeliveryStatus(patchedOrder.getDeliveryStatus());
        order.setNotes(patchedOrder.getNotes());

        return orderPort.save(order);
    }


    public List<Order> GetAllOrders(){
        return orderPort.findAll();
    }


    public Either<StructuredError, Order> GetOrderById(UUID orderId){
        return orderPort.findById(orderId).toEither(() -> new StructuredError("Order Not Found", ErrorType.NOT_FOUND_ERROR));
    }


    public List<Order> GetByUserId(UUID userId) {
        return orderPort.findByUserId(userId);
    }


    public Either<StructuredError, Void> DeleteOrder(UUID orderId){
        return orderPort.findById(orderId).toEither(() -> new StructuredError("Order Not Found", ErrorType.NOT_FOUND_ERROR)).map(existing -> {
            orderPort.deleteById(orderId);
            return null;
        });
    }

    private void hydrateItems(Order order) {
        for (OrderItem oi : order.getItems()) {
            UUID itemId = oi.getItem().getId();

            Item item = itemService.GetItemById(itemId)
                    .getOrElseThrow(() -> new EntityNotFoundException("Item", itemId));

            oi.setItem(item);
        }
    }

}


