package ordermanager.adapter.in.web.controller;

import io.vavr.control.Option;
import ordermanager.infrastructure.service.ItemService;
import ordermanager.infrastructure.store.persistence.entity.Item;
import ordermanager.infrastructure.web.controller.ItemController;
import ordermanager.domain.dto.item.CreateItemRequest;
import ordermanager.domain.dto.item.ItemResponse;
import ordermanager.infrastructure.mapper.ItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean ItemService itemService;
    @MockBean ItemMapper itemMapper;

    private static Item mkItem(UUID id, String name, BigDecimal price, String desc) {
        Item i = Item.builder().build();
        i.setId(id);
        i.setName(name);
        i.setPrice(price);
        i.setDescription(desc);
        i.setSize("Large");
        i.setQuantity(20);
        i.setDiscount(BigDecimal.ZERO);
        return i;
    }

    private static ItemResponse mkResp(UUID id, String name, BigDecimal price, String desc, String size, BigDecimal discount, boolean isAvailable, int quantity) {
        return new ItemResponse(
                id,
                name,
                desc,
                price,
                size,
                discount,
                isAvailable,
                quantity
        );
    }

    @Nested
    @DisplayName("POST /item")
    class Create {

        @Test
        @DisplayName("returns 200 with created item")
        void create_ok() throws Exception {
            UUID id = UUID.randomUUID();

            String body = """
              {
                "name": "Burger",
                "price": 7.50,
                "description": "Juicy beef burger",
                "size": "Large"
              }
              """;

            Item toCreate = mkItem(null, "Burger", new BigDecimal("7.50"), "Juicy beef burger");
            Item saved    = mkItem(id,   "Burger", new BigDecimal("7.50"), "Juicy beef burger");
            ItemResponse dto = mkResp(id, "Burger", new BigDecimal("7.50"), "Juicy beef burger", "Large", BigDecimal.ZERO,true,20 );

            when(itemMapper.create(any(CreateItemRequest.class))).thenReturn(toCreate);
            when(itemService.CreateItem(any(Item.class))).thenReturn(Option.of(saved));
            when(itemMapper.toResponse(saved)).thenReturn(dto);

            mockMvc.perform(post("/item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.name").value("Burger"))
                    .andExpect(jsonPath("$.price").value(7.50));
        }

        @Test
        @DisplayName("returns 400 when validation fails")
        void create_validationFail() throws Exception {
            String body = """
              { "name": "", "price": -1 }
              """;

            mockMvc.perform(post("/item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("returns 400 when service returns empty")
        void create_serviceEmpty() throws Exception {
            String body = """
              {
                "name": "Burger",
                "price": 7.50,
                "description": "Juicy beef burger"
              }
              """;

            Item toCreate = mkItem(null, "Burger", new BigDecimal("7.50"), "Juicy beef burger");

            when(itemMapper.create(any(CreateItemRequest.class))).thenReturn(toCreate);
            when(itemService.CreateItem(any(Item.class))).thenReturn(Option.none());

            mockMvc.perform(post("/item")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }


    @Nested
    @DisplayName("PUT /item/{id}")
    class Update {

        @Test
        @DisplayName("returns 200 with updated item")
        void update_ok() throws Exception {
            UUID id = UUID.randomUUID();

            String body = """
              {
                "name": "Burger Deluxe",
                "price": 8.75,
                "description": "Upgraded burger"
              }
              """;

            Item existing = mkItem(id, "Burger", new BigDecimal("7.50"), "Juicy beef burger");
            Item updated  = mkItem(id, "Burger Deluxe", new BigDecimal("8.75"), "Upgraded burger");
            ItemResponse dto = mkResp(id, "Burger Deluxe", new BigDecimal("8.75"), "Upgraded burger","No Size",BigDecimal.ZERO,true,100);

            when(itemService.GetItemById(id)).thenReturn(Option.of(existing));
            when(itemService.CreateItem(existing)).thenReturn(Option.of(updated));
            when(itemMapper.toResponse(updated)).thenReturn(dto);

            mockMvc.perform(put("/item/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.name").value("Burger Deluxe"))
                    .andExpect(jsonPath("$.price").value(8.75));
        }
        @Test
        @DisplayName("returns 404 when target item doesn't exist")
        void update_notFound() throws Exception {
            UUID id = UUID.randomUUID();

            String body = """
              { "name": "X", "price": 1.0 }
              """;

            when(itemService.GetItemById(id)).thenReturn(Option.none());

            mockMvc.perform(put("/item/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("returns 400 when save fails")
        void update_saveFails() throws Exception {
            UUID id = UUID.randomUUID();

            String body = """
              { "name": "Burger Deluxe", "price": 8.75 }
              """;

            Item existing = mkItem(id, "Burger", new BigDecimal("7.50"), "Juicy beef burger");
            when(itemService.GetItemById(id)).thenReturn(Option.of(existing));
            when(itemService.CreateItem(existing)).thenReturn(Option.none());

            mockMvc.perform(put("/item/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /item/{id}")
    class GetById {

        @Test
        @DisplayName("returns 200 when found")
        void get_found() throws Exception {
            UUID id = UUID.randomUUID();
            Item entity = mkItem(id, "Burger", new BigDecimal("7.50"), "Juicy beef burger");
            ItemResponse dto = mkResp(id, "Burger", new BigDecimal("7.50"), "Juicy beef burger","No Size",BigDecimal.ZERO,true,100);

            when(itemService.GetItemById(id)).thenReturn(Option.of(entity));
            when(itemMapper.toResponse(entity)).thenReturn(dto);

            mockMvc.perform(get("/item/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.name").value("Burger"));
        }

        @Test
        @DisplayName("returns 404 when not found")
        void get_notFound() throws Exception {
            UUID id = UUID.randomUUID();
            when(itemService.GetItemById(id)).thenReturn(Option.none());

            mockMvc.perform(get("/item/{id}", id))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /item")
    class GetAll {

        @Test
        @DisplayName("returns 200 with list")
        void getAll_ok() throws Exception {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();

            Item e1 = mkItem(id1, "Burger", new BigDecimal("7.50"), "A");
            Item e2 = mkItem(id2, "Pizza",  new BigDecimal("12.00"), "B");
            ItemResponse r1 = mkResp(id1, "Burger", new BigDecimal("7.50"), "A","A",BigDecimal.ZERO,true,10);
            ItemResponse r2 = mkResp(id2, "Pizza",  new BigDecimal("12.00"), "B","B",BigDecimal.ZERO,true,10);

            given(itemService.GetAllItems()).willReturn(List.of(e1, e2));
            given(itemMapper.toResponse(e1)).willReturn(r1);
            given(itemMapper.toResponse(e2)).willReturn(r2);

            mockMvc.perform(get("/item"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(id1.toString()))
                    .andExpect(jsonPath("$[1].id").value(id2.toString()));
        }
    }

    @Nested
    @DisplayName("DELETE /item/{id}")
    class Delete {

        @Test
        @DisplayName("returns 200 when deleted")
        void delete_ok() throws Exception {
            UUID id = UUID.randomUUID();
            when(itemService.DeleteItem(id)).thenReturn(true);

            mockMvc.perform(delete("/item/{id}", id))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("returns 400 when not deleted")
        void delete_badRequest() throws Exception {
            UUID id = UUID.randomUUID();
            when(itemService.DeleteItem(id)).thenReturn(false);

            mockMvc.perform(delete("/item/{id}", id))
                    .andExpect(status().isBadRequest());
        }
    }
}
