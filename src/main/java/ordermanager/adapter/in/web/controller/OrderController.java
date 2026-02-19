package ordermanager.adapter.in.web.controller;

import ordermanager.application.service.OrderService;
import ordermanager.shared.dto.order.CreateOrderRequest;
import ordermanager.shared.dto.order.OrderResponse;
import ordermanager.shared.dto.order.UpdateOrderRequest;
import ordermanager.shared.mapper.OrderMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ordermanager.domain.model.Utilities.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;


    public OrderController(OrderService orderService, OrderMapper orderMapper){

        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> CreateOrder(@Valid @RequestBody CreateOrderRequest orderBody){

        var order = orderMapper.toDomain(orderBody);
        order.setStatus(Status.NotProccessed);//Temp, pashan regaki chaktr lo chakrdni status dadanre.
        return orderService.CreateOrder(order)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrder(@PathVariable UUID orderId, @Valid @RequestBody UpdateOrderRequest orderBody){

        var orderExists = orderService.GetOrderById(orderId).orElse(null);

        if (orderExists == null)
            return ResponseEntity.notFound().build();

        orderMapper.update(orderExists, orderBody);
        var updatedUser = orderService.CreateOrder(orderExists);
        return updatedUser.map(order -> ResponseEntity.ok(orderMapper.toResponse(order))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> GetOrderById(@PathVariable("orderId") UUID orderId){

        return orderService.GetOrderById(orderId)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping()
    public ResponseEntity<List<OrderResponse>> GetAllOrders(){

        var orders = orderService.GetAllOrders().stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(orders);
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> DeleteOrder(@PathVariable("orderId") UUID uuid){
        boolean isDeleted = orderService.DeleteOrder(uuid);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

}
