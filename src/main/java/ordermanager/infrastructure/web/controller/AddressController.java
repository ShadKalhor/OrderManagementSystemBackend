package ordermanager.infrastructure.web.controller;

import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import ordermanager.infrastructure.service.AddressService;
import ordermanager.infrastructure.web.dto.useraddress.CreateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UpdateUserAddressRequest;
import ordermanager.infrastructure.web.dto.useraddress.UserAddressResponse;

import ordermanager.infrastructure.mapper.UserAddressMapper;
import ordermanager.infrastructure.web.exception.ErrorStructureException;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public UserAddressResponse CreateAddress(@Valid @RequestBody CreateUserAddressRequest addressBody){

        UserAddress address = addressMapper.create(addressBody);
        return addressService.CreateAddress(address)
                .map(addressMapper::toResponse)
                .getOrElseThrow(ErrorStructureException::new);
    }

    @PutMapping("/{addressId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserAddressResponse UpdateAddress(@PathVariable UUID addressId, @Valid @RequestBody UpdateUserAddressRequest addressBody){

        var address = addressMapper.update(addressBody);
        return addressService.UpdateAddress(addressId, address)
                .map(addressMapper::toResponse)
                        .getOrElseThrow(ErrorStructureException::new);
    }

    @GetMapping("/{addressId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserAddressResponse GetAddressById(@PathVariable("addressId") UUID addressId){

        return addressService.GetAddressById(addressId)
                .map(addressMapper::toResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void DeleteAddress(@PathVariable("addressId") UUID addressId){
        addressService.DeleteAddress(addressId).getOrElseThrow(ErrorStructureException::new);
    }
}
