package ordermanager.infrastructure.web.controller;

import ordermanager.infrastructure.service.OrderService;
import ordermanager.infrastructure.web.dto.order.CreateOrderRequest;
import ordermanager.infrastructure.web.dto.order.OrderResponse;
import ordermanager.infrastructure.web.dto.order.UpdateOrderRequest;
import ordermanager.infrastructure.mapper.OrderMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ordermanager.infrastructure.store.persistence.entity.Utilities.*;

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

        var order = orderMapper.create(orderBody);
        order.setStatus(Status.NotProccessed);//Temp, pashan regaki chaktr lo chakrdni status dadanre.
        return orderService.CreateOrder(order)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.badRequest().build());

    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> UpdateOrder(@PathVariable UUID orderId, @Valid @RequestBody UpdateOrderRequest orderBody){


        var order = orderMapper.update(orderBody);
        return orderService.UpdateOrder(orderId, order)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> GetOrderById(@PathVariable("orderId") UUID orderId){

        return orderService.GetOrderById(orderId)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());

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
