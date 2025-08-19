package OrderManager.Adapter.in.web.Controller;

import OrderManager.Shared.Dto.UserAddressDtos.CreateUserAddressRequest;
import OrderManager.Shared.Dto.UserAddressDtos.UpdateUserAddressRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import OrderManager.Domain.Model.UserAddress;
import OrderManager.Application.Service.AddressService;
import OrderManager.Shared.Dto.UserAddressDtos.UserAddressResponse;
import OrderManager.Shared.Mapper.UserAddressMapper;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AddressService addressService;

    @MockBean
    UserAddressMapper addressMapper;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getUserAddresses_ok_withResults() throws Exception {
        var userId = UUID.randomUUID();

        var a1 = new UserAddress(); a1.setId(UUID.randomUUID());
        var a2 = new UserAddress(); a2.setId(UUID.randomUUID());

        var r1 = new UserAddressResponse(
                a1.getId(),
                userId,
                "Line1",
                "City",
                "State",
                "Zip",
                "",
                "",
                false
        );
        var r2 = new UserAddressResponse(
                a2.getId(),
                userId,
                "Line2",
                "City2",
                "State2",
                "Zip2",
                "",
                "",
                false
        );

        given(addressService.GetUserAddresses(userId))
                .willReturn(Optional.of(List.of(a1, a2)));

        given(addressMapper.toResponse(a1)).willReturn(r1);
        given(addressMapper.toResponse(a2)).willReturn(r2);

        mvc.perform(get("/address/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(a1.getId().toString()))
                .andExpect(jsonPath("$[0].city").value("City"))
                .andExpect(jsonPath("$[1].id").value(a2.getId().toString()))
                .andExpect(jsonPath("$[1].city").value("City2"));
    }

    @Test
    void getUserAddresses_notFound_whenOptionalEmpty() throws Exception {
        var userId = UUID.randomUUID();

        given(addressService.GetUserAddresses(userId)).willReturn(Optional.empty());

        mvc.perform(get("/address/user/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserAddresses_notFound_whenListEmpty_andControllerTreatsEmptyAs404() throws Exception {
        var userId = UUID.randomUUID();

        given(addressService.GetUserAddresses(userId)).willReturn(Optional.of(List.of()));

        mvc.perform(get("/address/user/{userId}", userId))
                .andExpect(status().isNotFound());
    }


    @Test
    void createAddress_created() throws Exception {
        var request = new CreateUserAddressRequest(UUID.randomUUID(), "City", "State", "ss", "House", "street1", "123",false);
        var entity = new UserAddress(); entity.setId(UUID.randomUUID());
        var response = new UserAddressResponse(entity.getId(), UUID.randomUUID(), "Line1", "City", "State", "Zip", "street1", "234", false);

        given(addressMapper.toDomain(request)).willReturn(entity);

        given(addressService.CreateAddress(any(UserAddress.class)))
                .willReturn(Optional.of(entity));

        given(addressMapper.toResponse(entity)).willReturn(response);

        mvc.perform(post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId().toString()))
                .andExpect(jsonPath("$.city").value("City"));

        verify(addressService).CreateAddress(entity);
    }

    @Test
    void updateAddress_ok() throws Exception {
        var id = UUID.randomUUID();
        var updateRequest = new UpdateUserAddressRequest(UUID.randomUUID(), "CityX", "StateX", "ZipX",
                "", "", "", false);

        var entity = new UserAddress(); entity.setId(id);
        var updated = new UserAddress(); updated.setId(id);
        var response = new UserAddressResponse(id, UUID.randomUUID(), "LineX", "CityX", "StateX",
                "ZipX", "", "", true);

        given(addressService.GetAddressById(id)).willReturn(Optional.of(entity));

        //because of optional, TODO: Check if optional is needed when creating or updating entities.
        given(addressService.CreateAddress(any(UserAddress.class)))
                .willReturn(Optional.of(updated));
/*

        given(addressService.CreateAddress(entity)).willReturn(updated);
*/
        given(addressMapper.toResponse(updated)).willReturn(response);

        mvc.perform(put("/address/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.city").value("CityX"));

        verify(addressService).CreateAddress(entity);
    }

    @Test
    void updateAddress_notFound() throws Exception {
        var id = UUID.randomUUID();
        var updateRequest = new UpdateUserAddressRequest(UUID.randomUUID(), "CityX", "StateX", "ZipX",
                "", "", "",true);

        given(addressService.GetAddressById(id)).willReturn(Optional.empty());

        mvc.perform(put("/address/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

}
