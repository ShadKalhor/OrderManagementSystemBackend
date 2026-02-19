package ordermanager.adapter.in.web.controller;

import ordermanager.application.service.AddressService;
import ordermanager.application.service.OrderService;
import ordermanager.application.service.UserService;
import ordermanager.infrastructure.store.persistence.entity.Order;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import ordermanager.shared.dto.order.OrderResponse;
import ordermanager.shared.dto.user.CreateUserRequest;
import ordermanager.shared.dto.user.UpdateUserRequest;
import ordermanager.shared.dto.user.UserResponse;
import ordermanager.shared.dto.user.UserSummary;
import ordermanager.shared.dto.useraddress.UserAddressResponse;
import ordermanager.shared.mapper.OrderMapper;
import ordermanager.shared.mapper.UserAddressMapper;
import ordermanager.shared.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
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

    //  private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public UserController(AddressService addressService, OrderService orderService, UserService userService, UserMapper userMapper,OrderMapper orderMapper,UserAddressMapper userAddressMapper ,PasswordEncoder passwordEncoder){
        this.addressService = addressService;
        this.orderService = orderService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.userAddressMapper = userAddressMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<UserResponse> CreateUser(@Valid @RequestBody CreateUserRequest userBody){

        var user = userMapper.toDomain(userBody);

        return userService.SaveUser(user)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> UpdateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRequest userBody){

        var userExists = userService.GetUserById(userId).orElse(null);
        if(userExists == null)
            return ResponseEntity.notFound().build();
        userMapper.update(userExists, userBody);
        var updatedUser = userService.SaveUser(userExists);
        return updatedUser.map(user -> ResponseEntity.ok(userMapper.toResponse(user))).orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> GetById(@PathVariable("userId") UUID userId){

        return userService.GetUserById(userId)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<UserResponse> GetByPhoneNumber(@RequestParam(required = false) String phoneNumber){

        return userService.GetUserByPhoneNumber(phoneNumber)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> GetOrders(@PathVariable("userId") UUID userId){

        Optional<List<Order>> result = orderService.GetByUserId(userId);
        return result
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(orderMapper::toResponse).toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<UserAddressResponse>> GetUserById(@PathVariable("userId") UUID userId){

        Optional<List<UserAddress>> result = addressService.GetUserAddresses(userId);
        return result
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(userAddressMapper::toResponse).toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

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
