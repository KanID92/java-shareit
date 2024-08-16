package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final UserCreateDto userCreateDto = new UserCreateDto(
            "Igor",
            "kanId92@yandex.ru");
    private final UserOutputDto userOutputDto1 = new UserOutputDto(
            1L,
            "Igor",
            "kanId92@yandex.ru");
    private final UserUpdateDto userUpdateDto = new UserUpdateDto(
            1L,
            null,
            "kanId92@mail.ru");
    private final UserOutputDto userOutputDtoUpdated = new UserOutputDto(
            1L,
            "Igor",
            "kanId92@mail.ru");
    private final UserOutputDto userOutputDto2 = new UserOutputDto(
            1L,
            "Igor",
            "kanId92@mail.ru");
    private final  List<UserOutputDto> listUserOutputDto = List.of(
            userOutputDto1, userOutputDto2);


    @Test
    void testGetUserById() throws Exception {
        when(userService.getById(1L)).thenReturn(userOutputDto1 );

        mockMvc.perform(get("/users/{userId}", userOutputDto1 .id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userOutputDto1 .id()), Long.class))
                .andExpect(jsonPath("$.name", is(userOutputDto1 .name())))
                .andExpect(jsonPath("$.email", is(userOutputDto1 .email())));

        verify(userService, times(1)).getById(1L);
    }

    @Test
    void testUserCreate() throws Exception {

        when(userService.create(userCreateDto)).thenReturn(userOutputDto1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userOutputDto1 .id()), Long.class))
                .andExpect(jsonPath("$.name", is(userOutputDto1 .name())))
                .andExpect(jsonPath("$.email", is(userOutputDto1 .email())));

    }

    @Test
    void testUserUpdate() throws Exception {
        when(userService.update(userUpdateDto)).thenReturn(userOutputDtoUpdated);

        mockMvc.perform(patch("/users/{userId}", userUpdateDto.getId())
                    .content(objectMapper.writeValueAsString(userUpdateDto))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userOutputDtoUpdated.id()),Long.class))
                .andExpect(jsonPath("$.name", is(userOutputDtoUpdated.name())))
                .andExpect(jsonPath("$.email", is(userOutputDtoUpdated.email())));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{userId}", userOutputDto1.id()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAll()).thenReturn(listUserOutputDto);

        mockMvc.perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listUserOutputDto)));
    }

}