package ordermanager.contract;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.vavr.control.Either;
import ordermanager.domain.exception.ErrorType;
import ordermanager.domain.exception.StructuredError;
import ordermanager.domain.model.UserAddressDomain;
import ordermanager.infrastructure.mapper.UserAddressMapper;
import ordermanager.infrastructure.security.JwtService;
import ordermanager.infrastructure.service.AddressService;
import ordermanager.infrastructure.service.UserService;
import ordermanager.infrastructure.web.controller.AddressController;
import ordermanager.infrastructure.web.dto.useraddress.UserAddressResponse;
import ordermanager.infrastructure.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public abstract class BaseContractTest {

    @Autowired
    MockMvc mockMvc;


    @MockBean
    JwtService jwtService;

    @MockBean
    AddressService addressService;

    @MockBean
    UserService userService;

    @MockBean
    UserAddressMapper userAddressMapper;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        UserAddressDomain mappedDomain = new UserAddressDomain(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                UUID.fromString("112961f9-462e-49bd-85dd-b56041ec65d2"),
                "addressName",
                "erbil",
                "my Description",
                "house",
                "405",
                "102",
                true
        );

        UserAddressDomain responseDomain = new UserAddressDomain(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                UUID.fromString("112961f9-462e-49bd-85dd-b56041ec65d2"),
                "addressName",
                "erbil",
                "my Description",
                "house",
                "405",
                "102",
                true
        );

        when(userAddressMapper.createDomain(any())).thenReturn(mappedDomain);
        //when(addressService.CreateAddress(any())).thenReturn(Either.right(responseDomain));
        when(addressService.CreateAddress(any(UserAddressDomain.class)))
                .thenAnswer(inv -> {
                    UserAddressDomain d = inv.getArgument(0);

                    if (d.getUserId() != null &&
                            d.getUserId().equals(UUID.fromString("99999999-9999-9999-9999-999999999999"))) {
                        return Either.left(new StructuredError("User not found", ErrorType.NOT_FOUND_ERROR));
                    }

                    return Either.right(new UserAddressDomain(
                            UUID.fromString("22222222-2222-2222-2222-222222222222"),
                            d.getUserId(),
                            d.getName(),
                            d.getCity(),
                            d.getDescription(),
                            d.getType(),
                            d.getStreet(),
                            d.getResidentialNo(),
                            d.isPrimary()
                    ));
                });

        when(userAddressMapper.domainToResponse(any(UserAddressDomain.class)))
                .thenAnswer(inv -> {
                    UserAddressDomain d = inv.getArgument(0);
                    return new UserAddressResponse(
                            d.getId(),
                            d.getUserId(),
                            d.getName(),
                            d.getCity(),
                            d.getDescription(),
                            d.getType(),
                            d.getStreet(),
                            d.getResidentialNo(),
                            d.isPrimary()
                    );
                });    }
}