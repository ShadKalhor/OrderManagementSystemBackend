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
import ordermanager.infrastructure.web.exception.ErrorStructureException;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse CreateUser(@Valid @RequestBody CreateUserRequest userBody){

        var user = userMapper.create(userBody);
        return userService.CreateUser(user).map(userMapper::toResponse).getOrElseThrow(ErrorStructureException::new);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse UpdateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRequest userBody){

        var user = userMapper.update(userBody);
        return userService.UpdateUser(userId,user).map(userMapper::toResponse).getOrElseThrow(ErrorStructureException::new);
    }


    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse GetById(@PathVariable("userId") UUID userId){

        return userService.GetUserById(userId).map(userMapper::toResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponse GetByPhoneNumber(@RequestParam(required = false) String phoneNumber){

        return userService.GetUserByPhoneNumber(phoneNumber).map(userMapper::toResponse).getOrElseThrow(ErrorStructureException::new);

    }


    @GetMapping("/{userId}/orders")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<OrderResponse> GetOrders(@PathVariable("userId") UUID userId){

        //TODO:Move this function from order Service to User Service.
        return orderService.GetByUserId(userId).stream().map(orderMapper::toResponse).toList();

    }


    //TODO:Refactor this to be compatible with addressService.
    @GetMapping("/{userId}/addresses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<UserAddressResponse> GetAddressById(@PathVariable("userId") UUID userId){

        return addressService.GetUserAddresses(userId).stream().map(userAddressMapper::toResponse).toList();

    }



    @GetMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<UserSummary> GetAllUsers(){

        return userService.GetAllUsers().stream().map(userMapper::toSummary).toList();


    }
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void DeleteUser(@PathVariable("userId") UUID userId){
        userService.DeleteUser(userId).getOrElseThrow(ErrorStructureException::new);
    }
}
