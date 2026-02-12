package OrderManager.Adapter.in.web.Controller;

import OrderManager.Domain.Model.UserAddress;
import OrderManager.Application.Service.AddressService;
import OrderManager.Shared.Dto.UserAddressDtos.CreateUserAddressRequest;
import OrderManager.Shared.Dto.UserAddressDtos.UpdateUserAddressRequest;
import OrderManager.Shared.Dto.UserAddressDtos.UserAddressResponse;

import OrderManager.Shared.Mapper.UserAddressMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/Address")
public class AddressController {


    private final AddressService addressService;
    private final UserAddressMapper addressMapper;

    public AddressController(AddressService addressService,UserAddressMapper addressMapper){
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @PostMapping
    public ResponseEntity<UserAddressResponse> CreateAddress(@Valid @RequestBody CreateUserAddressRequest addressBody){

        var address = addressMapper.toDomain(addressBody);
        return addressService.CreateAddress(address)
                .map(addressMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> UpdateAddress(@PathVariable UUID addressId, @Valid @RequestBody UpdateUserAddressRequest addressBody){
        /*Optional<UserAddress> result = addressService.CreateAddress(address);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    */
        var addressExists = addressService.GetAddressById(addressId).orElse(null);

        if (addressExists == null)
            return ResponseEntity.notFound().build();

        addressMapper.update(addressExists, addressBody);
        var updatedUser = addressService.CreateAddress(addressExists);
        return updatedUser.map(address -> ResponseEntity.ok(addressMapper.toResponse(address))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> GetAddressById(@PathVariable("addressId") UUID addressId){

        return addressService.GetAddressById(addressId)
                .map(addressMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAddressResponse>> GetUserById(@PathVariable("userId") UUID userId){

        Optional<List<UserAddress>> result = addressService.GetUserAddresses(userId);
        return result
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(addressMapper::toResponse).toList())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> DeleteAddress(@PathVariable("addressId") UUID addressId){
        boolean isDeleted = addressService.DeleteAddress(addressId);
        if(isDeleted)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
