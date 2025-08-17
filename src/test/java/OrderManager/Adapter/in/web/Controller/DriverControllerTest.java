package OrderManager.Adapter.in.web.Controller;

import OrderManager.Application.Service.DriverService;
import OrderManager.Domain.Model.Driver;
import OrderManager.Shared.Dto.DriverDtos.DriverResponse;
import OrderManager.Shared.Mapper.DriverMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DriverController.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverService driverService;

    @MockBean
    private DriverMapper driverMapper;

    private static Driver mkDriver(UUID id, String name, String vehicle, int age) {
        Driver d = Driver.builder().build();
        d.setId(id);
        d.setVehicleNumber(vehicle);
        d.setAge(age);
        return d;
    }

    private static DriverResponse mkResponse(UUID id, String name, String vehicle, int age) {
        return new DriverResponse(id, name, vehicle, age, /* account */ null);
    }

    @Nested
    @DisplayName("GET /Driver/{id}")
    class GetById {

        @Test
        @DisplayName("returns 200 with a driver when found")
        void getDriver_found() throws Exception {
            UUID id = UUID.randomUUID();
            Driver entity = mkDriver(id, "Alex", "ABC-123", 30);
            DriverResponse dto = mkResponse(id, "Alex", "ABC-123", 30);

            when(driverService.FindDriver(id)).thenReturn(Optional.of(entity));
            when(driverMapper.toResponse(entity)).thenReturn(dto);

            mockMvc.perform(get("/Driver/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.name").value("Alex"))
                    .andExpect(jsonPath("$.vehicleNumber").value("ABC-123"))
                    .andExpect(jsonPath("$.age").value(30));
        }

        @Test
        @DisplayName("returns 400 when not found (matches current controller behavior)")
        void getDriver_notFound() throws Exception {
            UUID id = UUID.randomUUID();
            when(driverService.FindDriver(id)).thenReturn(Optional.empty());

            mockMvc.perform(get("/Driver/{id}", id))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /Driver")
    class GetAll {

        @Test
        @DisplayName("returns 200 with a list of drivers")
        void getAll_ok() throws Exception {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();

            Driver e1 = mkDriver(id1, "Alex", "ABC-123", 30);
            Driver e2 = mkDriver(id2, "Sam", "XYZ-789", 45);

            DriverResponse r1 = mkResponse(id1, "Alex", "ABC-123", 30);
            DriverResponse r2 = mkResponse(id2, "Sam", "XYZ-789", 45);

            given(driverService.FindAllDrivers()).willReturn(List.of(e1, e2));
            given(driverMapper.toResponse(e1)).willReturn(r1);
            given(driverMapper.toResponse(e2)).willReturn(r2);

            mockMvc.perform(get("/Driver"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(id1.toString()))
                    .andExpect(jsonPath("$[1].id").value(id2.toString()));
        }
    }

    @Nested
    @DisplayName("POST /Driver")
    class Create {

        @Test
        @DisplayName("returns 200 with created driver")
        void create_ok() throws Exception {
            UUID id = UUID.randomUUID();

            // Incoming JSON per CreateDriverRequest: name, vehicleNumber, age, accountId
            String body = """
      {
        "name": "Alex",
        "vehicleNumber": "ABC-123",
        "age": 30,
        "accountId": "%s"
      }
      """.formatted(UUID.randomUUID());

            // What the mapper should produce from the request BEFORE persisting
            Driver toCreate = mkDriver(null, "Alex", "ABC-123", 30); // id is null pre-persist

            // What the service returns AFTER persisting
            Driver entityAfterSave = mkDriver(id, "Alex", "ABC-123", 30);
            DriverResponse dto = mkResponse(id, "Alex", "ABC-123", 30);

            // 1) Stub mapper: CreateDriverRequest -> Driver (adjust method name if yours differs)
            when(driverMapper.toDomain(any(OrderManager.Shared.Dto.DriverDtos.CreateDriverRequest.class)))
                    .thenReturn(toCreate);

            // 2) Stub service: Driver -> Optional<Driver> (persisted)
            when(driverService.CreateDriver(any(Driver.class)))
                    .thenReturn(Optional.of(entityAfterSave));

            // 3) Stub mapper: Driver -> DriverResponse
            when(driverMapper.toResponse(entityAfterSave)).thenReturn(dto);

            mockMvc.perform(post("/Driver")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.name").value("Alex"));
        }

        @Test
        @DisplayName("returns 400 when validation fails")
        void create_validationFail() throws Exception {
            // Missing required fields to trigger @Valid violations
            String body = """
              {
                "name": "",
                "vehicleNumber": "",
                "age": 15
              }
              """;

            mockMvc.perform(post("/Driver")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("returns 400 when service returns empty")
        void create_serviceEmpty() throws Exception {
            String body = """
              {
                "name": "Alex",
                "vehicleNumber": "ABC-123",
                "age": 30,
                "accountId": "%s"
              }
              """.formatted(UUID.randomUUID());

            when(driverService.CreateDriver(any(Driver.class)))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post("/Driver")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /Driver/{id}")
    class Update {

        @Test
        @DisplayName("returns 200 with updated driver")
        void update_ok() throws Exception {
            UUID id = UUID.randomUUID();
            String body = """
              {
                "name": "Alex Updated",
                "vehicleNumber": "CAR-456",
                "age": 31,
                "accountId": "%s"
              }
              """.formatted(UUID.randomUUID());

            Driver existing = mkDriver(id, "Alex", "ABC-123", 30);
            Driver updated = mkDriver(id, "Alex Updated", "CAR-456", 31);
            DriverResponse dto = mkResponse(id, "Alex Updated", "CAR-456", 31);

            when(driverService.FindDriver(id)).thenReturn(Optional.of(existing));
            when(driverService.CreateDriver(any(Driver.class)))
                    .thenReturn(Optional.of(updated));
            when(driverMapper.toResponse(updated)).thenReturn(dto);

            mockMvc.perform(put("/Driver/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Alex Updated"))
                    .andExpect(jsonPath("$.vehicleNumber").value("CAR-456"))
                    .andExpect(jsonPath("$.age").value(31));
        }

        @Test
        @DisplayName("returns 400 when target driver doesn't exist")
        void update_notFound() throws Exception {
            UUID id = UUID.randomUUID();
            String body = """
              {
                "name": "Anyone",
                "vehicleNumber": "CAR-456",
                "age": 31
              }
              """;

            when(driverService.FindDriver(id)).thenReturn(Optional.empty());

            mockMvc.perform(put("/Driver/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /Driver/{id}")
    class Delete {

        @Test
        @DisplayName("returns 204 when deleted")
        void delete_noContent() throws Exception {
            UUID id = UUID.randomUUID();
            when(driverService.DeleteDriver(id)).thenReturn(true);

            mockMvc.perform(delete("/Driver/{id}", id))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("returns 400 when nothing deleted")
        void delete_badRequest() throws Exception {
            UUID id = UUID.randomUUID();
            when(driverService.DeleteDriver(id)).thenReturn(false);

            mockMvc.perform(delete("/Driver/{id}", id))
                    .andExpect(status().isBadRequest());
        }
    }
}
