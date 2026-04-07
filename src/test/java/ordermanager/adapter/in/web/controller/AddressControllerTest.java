package ordermanager.adapter.in.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.Genders;
import ordermanager.domain.model.UserAddressDomain;
import ordermanager.domain.model.UserDomain;
import ordermanager.domain.model.UserRoles;
import ordermanager.infrastructure.mapper.UserAddressMapper;
import ordermanager.infrastructure.security.JwtService;
import ordermanager.infrastructure.service.AddressService;
import ordermanager.infrastructure.service.UserService;
import ordermanager.infrastructure.store.persistence.entity.User;
import ordermanager.infrastructure.store.persistence.entity.UserAddress;
import ordermanager.infrastructure.web.controller.AddressController;
import ordermanager.infrastructure.web.dto.useraddress.CreateUserAddressRequest;
import ordermanager.infrastructure.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class AddressControllerTest{


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AddressService addressService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserAddressMapper userAddressMapper;


    @Test
    void shouldCreateAddress() throws Exception {

        String id = "112961f9-462e-49bd-85dd-b56041ec65d2";

        Either<StructuredError,UserDomain> savedUser = Either.right(new UserDomain(UUID.fromString(id), UserRoles.Member,"Hogn",
                "07502342323","123asdQWE$", Genders.Male));

        CreateUserAddressRequest addressRequest =
                new CreateUserAddressRequest(savedUser.get().getId(),
                        "addressName", "erbil",
                        "myDescription","House",
                "1st Street","405",true);



        UserAddressDomain addressDomain = new UserAddressDomain(UUID.randomUUID(), addressRequest.userId()
                ,addressRequest.name(),addressRequest.city(),addressRequest.description(),addressRequest.type(),
                addressRequest.street(), addressRequest.residentialNo(), addressRequest.isPrimary());


        given(addressService.CreateAddress(any()))
                .willReturn(Either.right(addressDomain));
        given(userService.CreateUser(any(UserDomain.class))).willReturn(savedUser);

        String jsonRequest = """
                
                {
                    "userId": "112961f9-462e-49bd-85dd-b56041ec65d2",
                    "name": "addressName",
                    "city": "erbil",
                    "description": "my Description",
                    "type": "house",
                    "street": "405",
                    "residentialNo": "102",
                    "isPrimary": true
                }
                """;

        mockMvc.perform(post("/addresses").contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(status().isCreated());

    }

    @Test
    void shouldNotReturnAddressBecauseOfInvalidUserId() throws Exception {

        UserAddressDomain mappedDomain = new UserAddressDomain(
                UUID.randomUUID(),
                UUID.fromString("112961f9-462e-49bd-85dd-b56041ec65d2"),
                "addressName",
                "erbil",
                "my Description",
                "house",
                "405",
                "102",
                true
        );

        when(userAddressMapper.createDomain(any(CreateUserAddressRequest.class)))
                .thenReturn(mappedDomain);

        when(addressService.CreateAddress(any(UserAddressDomain.class)))
                .thenReturn(Either.left(
                        new StructuredError("User not found", ErrorType.NOT_FOUND_ERROR)
                ));

        String jsonRequest = """
            {
                "userId": "112961f9-462e-49bd-85dd-b56041ec65d2",
                "name": "addressName",
                "city": "erbil",
                "description": "my Description",
                "type": "house",
                "street": "405",
                "residentialNo": "102",
                "isPrimary": true
            }
            """;

        mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

}