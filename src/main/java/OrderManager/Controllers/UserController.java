package OrderManager.Controllers;

import OrderManager.DTO.UserDTO;
import OrderManager.Entities.User;
import OrderManager.Entities.UserRole;
import OrderManager.Repository.UserRoleRepository;
import OrderManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRoleRepository userRoleRepository;
    @PostMapping
    public ResponseEntity<User> CreateUser(@RequestBody User user){
        Optional<User> savedUser =  userService.SaveUser(user);
        return savedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping
    public ResponseEntity<User> UpdateUser(@RequestBody User user){

       Optional<User> savedUser =  userService.SaveUser(user);
        return savedUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/getbyphone{phone}")
    public ResponseEntity<User> GetByPhoneNumber(@PathVariable("phone") String phoneNumber){
        Optional<User> result = userService.GetUserByPhoneNumber(phoneNumber);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getbyid{id}")
    public ResponseEntity<User> GetById(@PathVariable("id") UUID uuid){
        Optional<User> user = userService.GetUserById(uuid);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getall")
    public ResponseEntity<List<User>> GetAllUsers(){
        Optional<List<User>> users =Optional.of(userService.GetAllUsers());
        return users.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
    @DeleteMapping("{Id}")
    public ResponseEntity<Boolean> DeleteUser(@PathVariable("Id") UUID uuid){
        boolean isDeleted = userService.DeleteUser(uuid);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
