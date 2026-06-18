package ordermanager.infrastructure.web.controller;

import ordermanager.domain.model.UserAddressDomain;
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
    public UserAddressResponse createAddress(@Valid @RequestBody CreateUserAddressRequest addressBody){

        UserAddressDomain address = addressMapper.createDomain(addressBody);
        return addressService.CreateAddress(address)
                .map(addressMapper::domainToResponse)
                .getOrElseThrow(ErrorStructureException::new);
    }

    @PutMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public UserAddressResponse updateAddress(@PathVariable UUID addressId, @Valid @RequestBody UpdateUserAddressRequest addressBody){

        UserAddressDomain address = addressMapper.updateDomain(addressBody);
        return addressService.UpdateAddress(addressId, address)
                .map(addressMapper::domainToResponse)
                        .getOrElseThrow(ErrorStructureException::new);
    }

    @GetMapping("/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public UserAddressResponse getAddressById(@PathVariable("addressId") UUID addressId){

        return addressService.GetAddressById(addressId)
                .map(addressMapper::domainToResponse).getOrElseThrow(ErrorStructureException::new);

    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable("addressId") UUID addressId){
        addressService.DeleteAddress(addressId).getOrElseThrow(ErrorStructureException::new);
    }
}
