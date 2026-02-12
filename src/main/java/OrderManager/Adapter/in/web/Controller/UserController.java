package OrderManager.Adapter.in.web.Controller;

import OrderManager.Domain.Model.User;
import OrderManager.Application.Service.UserService;
import OrderManager.Shared.Config.SecurityConfig;
import OrderManager.Shared.Dto.UserDtos.CreateUserRequest;
import OrderManager.Shared.Dto.UserDtos.UpdateUserRequest;
import OrderManager.Shared.Dto.UserDtos.UserResponse;
import OrderManager.Shared.Dto.UserDtos.UserSummary;
import OrderManager.Shared.Mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@RestController
@RequestMapping("/User")
public class UserController {


    private final UserService userService;
    private final UserMapper userMapper;

    //  private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder){

        this.userService = userService;
        this.userMapper = userMapper;
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

    @GetMapping("/findbyphone/{phone}")
    public ResponseEntity<UserResponse> GetByPhoneNumber(@PathVariable("phone") String phoneNumber){

        return userService.GetUserByPhoneNumber(phoneNumber)
                .map(userMapper::toResponse)
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
