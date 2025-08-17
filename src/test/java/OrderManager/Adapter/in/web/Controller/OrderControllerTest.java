package OrderManager.Adapter.in.web.Controller;

import OrderManager.Application.Service.OrderService;
import OrderManager.Domain.Model.Order;
import OrderManager.Shared.Dto.OrderDtos.CreateOrderRequest;
import OrderManager.Shared.Dto.OrderDtos.OrderResponse;
import OrderManager.Shared.Dto.OrderDtos.UpdateOrderRequest;
import OrderManager.Shared.Dto.OrderItemDtos.CreateOrderItemRequest;
import OrderManager.Shared.Mapper.OrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean OrderService orderService;
    @MockBean OrderMapper orderMapper;

    // --- Helpers -------------------------------------------------------------

    private static Order mkOrder(UUID id) {
        Order o = Order.builder().build();
        o.setId(id);
        // we don't need to fully populate nested fields because we map to OrderResponse via mocked mapper
        o.setSubTotal(new BigDecimal("10.00"));
        o.setDeliveryFee(new BigDecimal("2.00"));
        o.setTax(new BigDecimal("1.00"));
        o.setTotalPrice(new BigDecimal("13.00"));
        return o;
    }

    private static OrderResponse mkResp(UUID id) {
        // All nested DTOs can be null for controller tests; we assert a couple of top-level fields
        return new OrderResponse(
                id,
                /* user */ null,
                /* address */ null,
                /* driver */ null,
                /* status */ null,
                /* deliveryStatus */ null,
                /* items */ List.of(),
                new BigDecimal("10.00"),
                new BigDecimal("2.00"),
                new BigDecimal("1.00"),
                new BigDecimal("13.00"),
                /* notes */ null
        );
    }

    // --- POST /order ---------------------------------------------------------

    @Nested
    @DisplayName("POST /order")
    class Create {

        @Test
        @DisplayName("returns 200 with created order")
        void create_ok() throws Exception {
            UUID id = UUID.randomUUID();

            // Valid CreateOrderRequest JSON
            UUID userId = UUID.randomUUID();
            UUID addressId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            String body = """
              {
                "userId": "%s",
                "addressId": "%s",
                "items": [
                  {"itemId": "%s", "quantity": 2}
                ],
                "notes": "leave at door"
              }
              """.formatted(userId, addressId, itemId);

            Order toCreate = mkOrder(null);       // pre-persist (no id)
            Order saved    = mkOrder(id);         // post-persist
            OrderResponse dto = mkResp(id);

            // mapper: request -> domain
            when(orderMapper.toDomain(any(CreateOrderRequest.class))).thenReturn(toCreate);
            // service: save
            when(orderService.CreateOrder(any(Order.class))).thenReturn(Optional.of(saved));
            // mapper: domain -> response
            when(orderMapper.toResponse(saved)).thenReturn(dto);

            mockMvc.perform(post("/order")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.totalPrice").value(13.00));
        }

        @Test
        @DisplayName("returns 400 when validation fails")
        void create_validationFail() throws Exception {
            // Missing required fields (userId, addressId, items) -> @Valid should trigger
            String body = """
              { "notes": "invalid payload missing required fields" }
              """;

            mockMvc.perform(post("/order")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("returns 400 when service returns empty")
        void create_serviceEmpty() throws Exception {
            UUID userId = UUID.randomUUID();
            UUID addressId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            String body = """
              {
                "userId": "%s",
                "addressId": "%s",
                "items": [
                  {"itemId": "%s", "quantity": 2}
                ]
              }
              """.formatted(userId, addressId, itemId);

            Order toCreate = mkOrder(null);

            when(orderMapper.toDomain(any(CreateOrderRequest.class))).thenReturn(toCreate);
            when(orderService.CreateOrder(any(Order.class))).thenReturn(Optional.empty());

            mockMvc.perform(post("/order")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    // --- PUT /order/{id} -----------------------------------------------------

    @Nested
    @DisplayName("PUT /order/{id}")
    class Update {

        @Test
        @DisplayName("returns 200 with updated order")
        void update_ok() throws Exception {
            UUID id = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            String body = """
              {
                "items": [
                  {"itemId": "%s", "quantity": 1}
                ],
                "notes": "ring bell"
              }
              """.formatted(itemId);

            Order existing = mkOrder(id);
            Order updated  = mkOrder(id);
            OrderResponse dto = mkResp(id);

            when(orderService.GetOrderById(id)).thenReturn(Optional.of(existing));
            // controller calls mapper.update(existing, body) -> void; Mockito is fine without explicit stub

            // controller then calls service.CreateOrder(existing) as an "update/save"
            when(orderService.CreateOrder(existing)).thenReturn(Optional.of(updated));
            when(orderMapper.toResponse(updated)).thenReturn(dto);

            mockMvc.perform(put("/order/{orderId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()));
        }

        @Test
        @DisplayName("returns 404 when target order doesn't exist")
        void update_notFound() throws Exception {
            UUID id = UUID.randomUUID();

            String body = """
              {
                "items": [
                  {"itemId": "%s", "quantity": 1}
                ]
              }
              """.formatted(UUID.randomUUID());

            when(orderService.GetOrderById(id)).thenReturn(Optional.empty());

            mockMvc.perform(put("/order/{orderId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("returns 400 when save fails")
        void update_saveFails() throws Exception {
            UUID id = UUID.randomUUID();

            String body = """
              {
                "items": [
                  {"itemId": "%s", "quantity": 1}
                ]
              }
              """.formatted(UUID.randomUUID());

            Order existing = mkOrder(id);
            when(orderService.GetOrderById(id)).thenReturn(Optional.of(existing));
            when(orderService.CreateOrder(existing)).thenReturn(Optional.empty());

            mockMvc.perform(put("/order/{orderId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    // --- GET /order/{Id} -----------------------------------------------------

    @Nested
    @DisplayName("GET /order/{Id}")
    class GetById {

        @Test
        @DisplayName("returns 200 when found")
        void get_found() throws Exception {
            UUID id = UUID.randomUUID();
            Order entity = mkOrder(id);
            OrderResponse dto = mkResp(id);

            when(orderService.GetOrderById(id)).thenReturn(Optional.of(entity));
            when(orderMapper.toResponse(entity)).thenReturn(dto);

            mockMvc.perform(get("/order/{Id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id.toString()));
        }

        @Test
        @DisplayName("returns 404 when not found")
        void get_notFound() throws Exception {
            UUID id = UUID.randomUUID();
            when(orderService.GetOrderById(id)).thenReturn(Optional.empty());

            mockMvc.perform(get("/order/{Id}", id))
                    .andExpect(status().isNotFound());
        }
    }

    // --- GET /order ----------------------------------------------------------

    @Nested
    @DisplayName("GET /order")
    class GetAll {

        @Test
        @DisplayName("returns 200 with list")
        void getAll_ok() throws Exception {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();

            Order e1 = mkOrder(id1);
            Order e2 = mkOrder(id2);
            OrderResponse r1 = mkResp(id1);
            OrderResponse r2 = mkResp(id2);

            given(orderService.GetAllOrders()).willReturn(List.of(e1, e2));
            given(orderMapper.toResponse(e1)).willReturn(r1);
            given(orderMapper.toResponse(e2)).willReturn(r2);

            mockMvc.perform(get("/order"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(id1.toString()))
                    .andExpect(jsonPath("$[1].id").value(id2.toString()));
        }
    }

    // --- GET /order/findbyuserid/{Id} ---------------------------------------

    @Nested
    @DisplayName("GET /order/findbyuserid/{Id}")
    class GetByUserId {

        @Test
        @DisplayName("returns 200 with list when present")
        void byUser_ok() throws Exception {
            UUID userId = UUID.randomUUID();
            UUID o1 = UUID.randomUUID();
            UUID o2 = UUID.randomUUID();

            Order e1 = mkOrder(o1);
            Order e2 = mkOrder(o2);
            OrderResponse r1 = mkResp(o1);
            OrderResponse r2 = mkResp(o2);

            when(orderService.GetByUserId(userId)).thenReturn(Optional.of(List.of(e1, e2)));
            when(orderMapper.toResponse(e1)).thenReturn(r1);
            when(orderMapper.toResponse(e2)).thenReturn(r2);

            mockMvc.perform(get("/order/findbyuserid/{Id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(o1.toString()))
                    .andExpect(jsonPath("$[1].id").value(o2.toString()));
        }

        @Test
        @DisplayName("returns 404 when empty")
        void byUser_notFound() throws Exception {
            UUID userId = UUID.randomUUID();
            when(orderService.GetByUserId(userId)).thenReturn(Optional.empty());

            mockMvc.perform(get("/order/findbyuserid/{Id}", userId))
                    .andExpect(status().isNotFound());
        }
    }

    // --- DELETE /order/{Id} --------------------------------------------------

    @Nested
    @DisplayName("DELETE /order/{Id}")
    class Delete {

        @Test
        @DisplayName("returns 200 when deleted")
        void delete_ok() throws Exception {
            UUID id = UUID.randomUUID();
            when(orderService.DeleteOrder(id)).thenReturn(true);

            mockMvc.perform(delete("/order/{Id}", id))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("returns 400 when not deleted")
        void delete_badRequest() throws Exception {
            UUID id = UUID.randomUUID();
            when(orderService.DeleteOrder(id)).thenReturn(false);

            mockMvc.perform(delete("/order/{Id}", id))
                    .andExpect(status().isBadRequest());
        }
    }
}
