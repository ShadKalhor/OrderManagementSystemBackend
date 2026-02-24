package ordermanager.infrastructure.web.controller;

import liquibase.pro.packaged.R;
import ordermanager.infrastructure.service.OrderService;
import ordermanager.infrastructure.web.dto.order.CreateOrderRequest;
import ordermanager.infrastructure.web.dto.order.OrderResponse;
import ordermanager.infrastructure.web.dto.order.UpdateOrderRequest;
import ordermanager.infrastructure.mapper.OrderMapper;
import ordermanager.infrastructure.store.persistence.entity.Status;
import ordermanager.infrastructure.web.exception.ErrorStructureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse CreateOrder(@Valid @RequestBody CreateOrderRequest orderBody){

        var order = orderMapper.create(orderBody);
        order.setStatus(Status.NotProccessed);//TODO:Temp, pashan regaki chaktr lo chakrdni status dadanre.

        return orderService.CreateOrder(order).map(orderMapper::toResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @PutMapping("/{orderId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponse UpdateOrder(@PathVariable UUID orderId, @Valid @RequestBody UpdateOrderRequest orderBody){
        var order = orderMapper.update(orderBody);
        return orderService.UpdateOrder(orderId,order).map(orderMapper::toResponse).getOrElseThrow(ErrorStructureException::new);
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponse GetOrderById(@PathVariable("orderId") UUID orderId){
        return orderService.GetOrderById(orderId).map(orderMapper::toResponse).getOrElseThrow(ErrorStructureException::new);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<OrderResponse> GetAllOrders(){
        return orderService.GetAllOrders().stream().map(orderMapper::toResponse).toList();
    }


    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void DeleteOrder(@PathVariable("orderId") UUID orderId){

        orderService.DeleteOrder(orderId).getOrElseThrow(ErrorStructureException::new);

    }

}
