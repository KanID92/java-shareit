package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    @MockBean
    private ItemService itemService;

    private ItemCreateDto itemCreateDto1;
    private ItemOutputDto itemOutputDto1;

    private ItemCreateDto itemCreateDto2;

    private ItemUpdateDto itemUpdateDto1;
    private ItemOutputDto itemOutputUpdateDto1;

    private ItemUpdateDto itemUpdateDto2;
    private ItemOutputDto itemOutputUpdateDto2;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        itemCreateDto1 = new ItemCreateDto();
        itemCreateDto1.setName("testItem1");
        itemCreateDto1.setDescription("testDescription1");
        itemCreateDto1.setAvailable(true);
        itemCreateDto1.setRequestId(null);


        itemCreateDto2 = new ItemCreateDto();
        itemCreateDto2.setName("testItem2");
        itemCreateDto2.setDescription("testDescription2");
        itemCreateDto2.setAvailable(true);
        itemCreateDto2.setRequestId(null);


        itemUpdateDto1 = new ItemUpdateDto();
        itemUpdateDto1.setId(2L);
        itemUpdateDto1.setAvailable(false);

        itemUpdateDto2 = new ItemUpdateDto();
        itemUpdateDto2.setId(1L);
        itemUpdateDto2.setName("testUpdateName");
        itemUpdateDto2.setDescription("testUpdateDescription");
        itemUpdateDto2.setAvailable(false);

        user1 = new User();
        user1.setId(1L);
        user1.setName("testName1");
        user1.setEmail("testEmail1@mail.ru");

        user2 = new User();
        user1.setId(2L);
        user2.setName("testName2");
        user2.setEmail("testEmail2@mail.ru");
    }

    @Test
    void testAddItem() throws Exception {
        itemOutputDto1 = new ItemOutputDto(
                1L,
                itemCreateDto1.getName(),
                itemCreateDto1.getDescription(),
                itemCreateDto1.getAvailable(),
                null,
                null,
                null,
                null
        );


        when(itemService.add(any(), any(Long.class)))
                .thenReturn(itemOutputDto1);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemCreateDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.name", is(itemOutputDto1.name())))
                .andExpect(jsonPath("$.description", is(itemOutputDto1.description())))
                .andExpect(jsonPath("$.available", is(itemOutputDto1.available())));

    }


    @Test
    void testUpdateAvailablePositive() throws Exception {

        itemOutputUpdateDto1 = new ItemOutputDto(
                2L,
                itemCreateDto2.getName(),
                itemCreateDto2.getDescription(),
                itemUpdateDto1.getAvailable(),
                null,
                null,
                null,
                null
        );

        when(itemService.update(any(), any(Long.class)))
                .thenReturn(itemOutputUpdateDto1);
        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(itemUpdateDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemOutputUpdateDto1.id()), Long.class))
                .andExpect(jsonPath("$.name", is(itemOutputUpdateDto1.name())))
                .andExpect(jsonPath("$.description", is(itemOutputUpdateDto1.description())))
                .andExpect(jsonPath("$.available", is(itemOutputUpdateDto1.available())));

    }

    @Test
    void testUpdateAllPositive() throws Exception {

        itemOutputUpdateDto2 = new ItemOutputDto(
                1L,
                itemUpdateDto2.getName(),
                itemUpdateDto2.getDescription(),
                itemUpdateDto2.getAvailable(),
                null,
                null,
                null,
                null
        );

        when(itemService.update(any(), any(Long.class)))
                .thenReturn(itemOutputUpdateDto2);
        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(itemUpdateDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemOutputUpdateDto2.id()), Long.class))
                .andExpect(jsonPath("$.name", is(itemOutputUpdateDto2.name())))
                .andExpect(jsonPath("$.description", is(itemOutputUpdateDto2.description())))
                .andExpect(jsonPath("$.available", is(itemOutputUpdateDto2.available())));

    }

    @Test
    void testGetItem() throws Exception {

        itemOutputDto1 = new ItemOutputDto(
                1L,
                itemCreateDto1.getName(),
                itemCreateDto1.getDescription(),
                itemCreateDto1.getAvailable(),
                null,
                null,
                null,
                null
        );

        when(itemService.get(any(Long.class), any(Long.class)))
                .thenReturn(itemOutputDto1);
        mockMvc.perform(get("/items/1")
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.name", is(itemOutputDto1.name())))
                .andExpect(jsonPath("$.description", is(itemOutputDto1.description())))
                .andExpect(jsonPath("$.available", is(itemOutputDto1.available())));

    }

    @Test
    void testGetAllUserItems() throws Exception {
        itemOutputDto1 = new ItemOutputDto(
                1L,
                itemCreateDto1.getName(),
                itemCreateDto1.getDescription(),
                itemCreateDto1.getAvailable(),
                null,
                null,
                null,
                null
        );

        when(itemService.getAllUserItems(any(Long.class)))
                .thenReturn(List.of(itemOutputDto1));
        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(itemOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemOutputDto1.name())))
                .andExpect(jsonPath("$.[0].description", is(itemOutputDto1.description())))
                .andExpect(jsonPath("$.[0].available", is(itemOutputDto1.available())));


    }

    @Test
    void testSearch() throws Exception {

        itemOutputDto1 = new ItemOutputDto(
                1L,
                "testName1",
                "testDescription1",
                itemCreateDto1.getAvailable(),
                null,
                null,
                null,
                null
        );

        when(itemService.search(anyLong(), anyString()))
                .thenReturn(List.of(itemOutputDto1));
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("testName1")));

    }


    @Test
    void addComment() throws Exception {

        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("good item");

        CommentOutputDto commentOutputDto = new CommentOutputDto(
                1L,
                commentCreateDto.getText(),
                new ItemOutputDto(
                        1L,
                        "ItemTest1",
                        "anyDescription",
                        true,
                        null,
                        null,
                        null,
                        null),
                "userTestName",
                LocalDateTime.of(2022, 10, 2, 10, 12)
        );

        when(itemService.addComment(any(), any(Long.class), any(Long.class)))
                .thenReturn(commentOutputDto);
        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentOutputDto.id()), Long.class))
                .andExpect(jsonPath("$.text", is(commentOutputDto.text())))
                .andExpect(jsonPath("$.authorName", is(commentOutputDto.authorName())))
                .andExpect(jsonPath("$.created",
                        is(commentOutputDto.created().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));


    }
}