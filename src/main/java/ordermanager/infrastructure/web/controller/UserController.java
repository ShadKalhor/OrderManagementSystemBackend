package ordermanager.infrastructure.web.controller;

import io.vavr.control.Option;
import ordermanager.infrastructure.service.AddressService;
import ordermanager.infrastructure.service.OrderService;
import ordermanager.infrastructure.service.UserService;
import ordermanager.infrastructure.store.persistence.entity.Order;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import ordermanager.infrastructure.web.dto.order.OrderResponse;
import ordermanager.infrastructure.web.dto.user.CreateUserRequest;
import ordermanager.infrastructure.web.dto.user.UpdateUserRequest;
import ordermanager.infrastructure.web.dto.user.UserResponse;
import ordermanager.infrastructure.web.dto.user.UserSummary;
import ordermanager.infrastructure.web.dto.useraddress.UserAddressResponse;
import ordermanager.infrastructure.mapper.OrderMapper;
import ordermanager.infrastructure.mapper.UserAddressMapper;
import ordermanager.infrastructure.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;



@RestController
@RequestMapping("/users")
public class UserController {

    private final AddressService addressService;
    private final OrderService orderService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final UserAddressMapper userAddressMapper;


    public UserController(AddressService addressService, OrderService orderService, UserService userService,
                          UserMapper userMapper,OrderMapper orderMapper,UserAddressMapper userAddressMapper){
        this.addressService = addressService;
        this.orderService = orderService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.userAddressMapper = userAddressMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> CreateUser(@Valid @RequestBody CreateUserRequest userBody){

        var user = userMapper.create(userBody);

        return userService.SaveUser(user)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> UpdateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRequest userBody){

        var user = userMapper.update(userBody);
        return userService.UpdateUser(userId, user)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.badRequest().build());
        }


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> GetById(@PathVariable("userId") UUID userId){

        return userService.GetUserById(userId)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<UserResponse> GetByPhoneNumber(@RequestParam(required = false) String phoneNumber){

        return userService.GetUserByPhoneNumber(phoneNumber)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> GetOrders(@PathVariable("userId") UUID userId){

        Option<List<Order>> result = orderService.GetByUserId(userId);
        return result
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(orderMapper::toResponse).toList())
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<UserAddressResponse>> GetUserById(@PathVariable("userId") UUID userId){

        Option<List<UserAddress>> result = addressService.GetUserAddresses(userId);
        return result
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(userAddressMapper::toResponse).toList())
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());

    }



    @GetMapping()
    public ResponseEntity<List<UserSummary>> GetAllUsers(){

        var users = userService.GetAllUsers().stream()
                .map(userMapper::toSummary)
                .toList();
        return ResponseEntity.ok(users);

    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> DeleteUser(@PathVariable("userId") UUID userId){
        boolean isDeleted = userService.DeleteUser(userId);
        if(isDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
