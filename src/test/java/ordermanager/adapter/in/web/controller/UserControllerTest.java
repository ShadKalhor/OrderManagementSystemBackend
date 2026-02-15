package ordermanager.adapter.in.web.controller;

import ordermanager.domain.model.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ordermanager.domain.model.User;
import ordermanager.application.service.*;
import ordermanager.shared.mapper.UserMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    UserMapper userMapper;

    @Test
    void getUser_found() throws Exception {
        var id = UUID.randomUUID();
        var entity = new User(); entity.setId(id);
        var dto = new UserResponse(id, "Alice", "alice@example.com", Utilities.UserRoles.Member, Utilities.Genders.Female);

        given(userService.GetUserById(id)).willReturn(Optional.of(entity));
        given(userMapper.toResponse(entity)).willReturn(dto);

        mvc.perform(get("/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getUser_notFound() throws Exception {
        var id = UUID.randomUUID();
        given(userService.GetUserById(id)).willReturn(Optional.empty());

        mvc.perform(get("/user/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void listUsers_ok() throws Exception {
        var u1 = new User(); u1.setId(UUID.randomUUID());
        var u2 = new User(); u2.setId(UUID.randomUUID());

        var s1 = new UserSummary(u1.getId(), "A","07501234567",Utilities.UserRoles.Member,Utilities.Genders.Male);
        var s2 = new UserSummary(u2.getId(), "B","07501234568",Utilities.UserRoles.Member,Utilities.Genders.Male);

        given(userService.GetAllUsers()).willReturn(List.of(u1, u2));
        given(userMapper.toSummary(u1)).willReturn(s1);
        given(userMapper.toSummary(u2)).willReturn(s2);

        mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(u1.getId().toString()))
                .andExpect(jsonPath("$[1].name").value("B"));
    }

    @Test
    void createUser_created() throws Exception {

        var body = """
        {
          "name": "Alice",
          "password": "Alice@12",
          "phone": "07501233234"
        }
        """;

        var entity = new User();
        var saved = new User(); saved.setId(UUID.randomUUID());
        var response = new UserResponse(saved.getId(), "Alice", "alice@example.com", Utilities.UserRoles.Member, Utilities.Genders.Female);

        given(userMapper.toDomain(any(CreateUserRequest.class))).willReturn(entity);
        given(userService.SaveUser(entity)).willReturn(Optional.of(saved));
        given(userMapper.toResponse(saved)).willReturn(response);

        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ok() throws Exception {

        var id = UUID.randomUUID();
        var body = """
        { "name": "Alice Updated" }
        """;


        var existing = new User(); existing.setId(id);
        var saved = new User(); saved.setId(id);
        var response = new UserResponse(id, "Alice Updated", "alice@example.com", Utilities.UserRoles.Member,Utilities.Genders.Female);

        given(userService.GetUserById(id)).willReturn(Optional.of(existing));
        given(userService.SaveUser(existing)).willReturn(Optional.of(saved));
        given(userMapper.toResponse(saved)).willReturn(response);

        mvc.perform(put("/user/{userId}", id).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void updateUser_notFound() throws Exception {
        var id = UUID.randomUUID();
        given(userService.GetUserById(id)).willReturn(Optional.empty());

        mvc.perform(put("/user/{userId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X\"}"))
                .andExpect(status().isNotFound());
    }
}
