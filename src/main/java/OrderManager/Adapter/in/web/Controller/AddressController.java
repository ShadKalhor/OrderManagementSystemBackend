package OrderManager.Adapter.in.web.Controller;

import OrderManager.Domain.Model.User;
import OrderManager.Domain.Model.UserAddress;
import OrderManager.Application.Service.AddressService;
import OrderManager.Application.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<UserAddress> CreateAddress(@RequestBody UserAddress address){
        Optional<UserAddress> result = addressService.CreateAddress(address);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<UserAddress> UpdateAddress(@RequestBody UserAddress address){
        Optional<UserAddress> result = addressService.CreateAddress(address);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{Id}")
    public ResponseEntity<UserAddress> GetAddressById(@PathVariable("Id") UUID uuid){
        Optional<UserAddress> result = addressService.GetAddressById(uuid);
        return result.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping("/getbyuser")
    public ResponseEntity<List<UserAddress>> GetUserAddresses(@RequestBody User user){
        Optional<List<UserAddress>> result = addressService.GetUserAddresses(user.getId());
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/getbyuser{id}")
    public ResponseEntity<List<UserAddress>> GetUserById(@PathVariable("id") UUID uuid){
        System.out.println(uuid);
        Optional<List<UserAddress>> result = addressService.GetUserAddresses(uuid);
        System.out.println(result);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> DeleteAddress(@PathVariable("id") UUID uuid){
        boolean isDeleted = addressService.DeleteAddress(uuid);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
