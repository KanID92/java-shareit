package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    @MockBean
    private ItemRequestService itemRequestService;

    private User user1;
    private User user2;
    private User user3;

    private Item item1;
    private Item item2;

    private ItemRequestCreateDto itemRequestCreateDto1;
    private ItemRequestCreateDto itemRequestCreateDto2;

    private ItemRequestOutputDto itemRequestOutputDto1;

    @BeforeEach
    void setUp() {

        user1 = new User();
        user1.setId(1L);
        user1.setName("testName1");
        user1.setEmail("testEmail1@mail.ru");

        user2 = new User();
        user2.setId(2L);
        user2.setName("testName2");
        user2.setEmail("testEmail2@mail.ru");

        user3 = new User();
        user3.setId(3L);
        user3.setName("testName3");
        user3.setEmail("testEmail3@mail.ru");

        item1 = new Item();
        item1.setId(1L);
        item1.setName("testItem1");
        item1.setIsAvailable(true);
        item1.setOwner(user1);
        item1.setDescription("TestItem1Description");
        item1.setRequestId(1L);

        item2 = new Item();
        item2.setId(2L);
        item2.setName("testItem2");
        item2.setOwner(user2);

        itemRequestCreateDto1 = new ItemRequestCreateDto();
        itemRequestCreateDto1.setDescription("itemRequestCreateDto1Description");

        itemRequestCreateDto2 = new ItemRequestCreateDto();
        itemRequestCreateDto2.setDescription("itemRequestCreateDto2Description");

        itemRequestOutputDto1 = new ItemRequestOutputDto(
                1L,
                itemRequestCreateDto1.getDescription(),
                LocalDateTime.of(2023, 9, 12, 2, 15),
                List.of(ItemDtoMapper.toShortOutputDto(item1))
        );

    }

    @Test
    void testAdd() throws Exception {

        when(itemRequestService.add(any(), any(Long.class)))
                .thenReturn(itemRequestOutputDto1);
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestCreateDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestOutputDto1.description())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestOutputDto1.created().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));

    }

    @Test
    void getAllOwnRequests() throws Exception {
        when(itemRequestService.getAllOwnRequests(any(Long.class)))
                .thenReturn(List.of(itemRequestOutputDto1));
        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestOutputDto1.description())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestOutputDto1.created().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(any(Long.class), any(Integer.class) , any(Integer.class) ))
                .thenReturn(List.of(itemRequestOutputDto1));
        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestOutputDto1.description())))
                .andExpect(jsonPath("$.[0].created",
                        is(itemRequestOutputDto1.created().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void testGet() throws Exception {
        when(itemRequestService.get(any(Long.class), any(Long.class)))
                .thenReturn(itemRequestOutputDto1);
        mockMvc.perform(MockMvcRequestBuilders.get("/requests/1")
                        .content(objectMapper.writeValueAsString(itemRequestCreateDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestOutputDto1.description())))
                .andExpect(jsonPath("$.created",
                        is(itemRequestOutputDto1.created().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}