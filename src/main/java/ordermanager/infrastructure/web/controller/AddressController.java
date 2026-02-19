package ordermanager.infrastructure.web.controller;

import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import ordermanager.infrastructure.service.AddressService;
import ordermanager.infrastructure.web.dto.useraddress.CreateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UpdateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UserAddressResponse;

import ordermanager.infrastructure.mapper.UserAddressMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {


    private final AddressService addressService;
    private final UserAddressMapper addressMapper;

    public AddressController(AddressService addressService,UserAddressMapper addressMapper){
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @PostMapping
    public ResponseEntity<UserAddressResponse> CreateAddress(@Valid @RequestBody CreateUserAddressRequest addressBody){

        UserAddress address = addressMapper.toDomain(addressBody);
        return addressService.CreateAddress(address)
                .map(addressMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> UpdateAddress(@PathVariable UUID addressId, @Valid @RequestBody UpdateUserAddressRequest addressBody){

        var address = addressMapper.update(addressBody);
        return addressService.UpdateAddress(addressId, address)
                .map(addressMapper::toResponse)
                .map(ResponseEntity::ok)
                        .getOrElse(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> GetAddressById(@PathVariable("addressId") UUID addressId){

        return addressService.GetAddressById(addressId)
                .map(addressMapper::toResponse)
                .map(ResponseEntity::ok)
                .getOrElse(() -> ResponseEntity.notFound().build());

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
