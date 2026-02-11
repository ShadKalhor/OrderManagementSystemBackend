package OrderManager.Adapter.in.web.Controller;

import OrderManager.Domain.Model.Order;
import OrderManager.Application.Service.OrderService;
import OrderManager.Domain.Model.UserAddress;
import OrderManager.Domain.Model.Utilities;
import OrderManager.Shared.Dto.OrderDtos.CreateOrderRequest;
import OrderManager.Shared.Dto.OrderDtos.OrderResponse;
import OrderManager.Shared.Dto.OrderDtos.UpdateOrderRequest;
import OrderManager.Shared.Mapper.OrderMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;


    public OrderController(OrderService orderService, OrderMapper orderMapper){

        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> CreateOrder(@Valid @RequestBody CreateOrderRequest orderBody){
        /*Optional<Order> result = orderService.CreateOrder(order);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    */
        var order = orderMapper.toDomain(orderBody);
        order.setStatus(Utilities.Status.NotProccessed);//Temp, pashan regaki chaktr lo chakrdni status dadanre.
        return orderService.CreateOrder(order)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrder(@PathVariable UUID orderId, @Valid @RequestBody UpdateOrderRequest orderBody){
        /*Optional<Order> result = orderService.CreateOrder(order);
        return result.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    */

        var orderExists = orderService.GetOrderById(orderId).orElse(null);

        if (orderExists == null)
            return ResponseEntity.notFound().build();

        orderMapper.update(orderExists, orderBody);
        var updatedUser = orderService.CreateOrder(orderExists);
        return updatedUser.map(order -> ResponseEntity.ok(orderMapper.toResponse(order))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{Id}")
    public ResponseEntity<OrderResponse> GetOrderById(@PathVariable("Id") UUID orderId){
        /*Optional<Order> order = orderService.GetOrderById(orderId);

        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    */
        return orderService.GetOrderById(orderId)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> GetAllOrders(){
        /*Optional<List<Order>> orders = orderService.GetAllOrders();
        return orders.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    */

        var orders = orderService.GetAllOrders().stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/findbyuserid/{Id}")
    public ResponseEntity<List<OrderResponse>> GetByUserId(@PathVariable("Id") UUID userId){
        /*Optional<List<Order>> orders = orderService.GetByUserId(userId);

        return orders.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    */
        Optional<List<Order>> result = orderService.GetByUserId(userId);
        return result
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(orderMapper::toResponse).toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> DeleteOrder(@PathVariable("Id") UUID uuid){
        boolean isDeleted = orderService.DeleteOrder(uuid);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

}
