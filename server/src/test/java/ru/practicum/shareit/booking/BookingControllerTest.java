package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.SearchState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    @MockBean
    private BookingService bookingService;

    private User user1;
    private User user2;
    private User user3;

    private Item item1;
    private Item item2;

    BookingCreateDto bookingCreateDto1;
    BookingCreateDto bookingCreateDto2;

    BookingOutputDto bookingOutputDto1;


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

        bookingCreateDto1 = new BookingCreateDto();
        bookingCreateDto1.setItemId(1L);
        bookingCreateDto1.setStart(LocalDateTime.of(2025, 2, 4, 12, 15));
        bookingCreateDto1.setEnd(LocalDateTime.of(2025, 2, 15, 14, 20));

        bookingCreateDto2 = new BookingCreateDto();
        bookingCreateDto2.setItemId(2L);
        bookingCreateDto2.setStart(LocalDateTime.of(2025, 3, 7, 2, 45));
        bookingCreateDto1.setEnd(LocalDateTime.of(2025, 5, 4, 2, 1));

        item1 = new Item();
        item1.setId(1L);
        item1.setName("testItem1");
        item1.setIsAvailable(true);
        item1.setOwner(user1);
        item1.setDescription("TestItem1Description");

        item2 = new Item();
        item2.setId(2L);
        item2.setName("testItem2");
        item2.setOwner(user2);

        bookingOutputDto1 = new BookingOutputDto(
                1L,
                UserDtoMapper.toDto(user1),
                ItemDtoMapper.toOutputDto(item1),
                bookingCreateDto1.getStart(),
                bookingCreateDto1.getEnd(),
                Status.WAITING
        );
    }

    @Test
    void testAddBooking() throws Exception {

        when(bookingService.add(bookingCreateDto1, user3.getId()))
                .thenReturn(bookingOutputDto1);

        mockMvc.perform(post("/bookings")
                        .header(USER_ID, 3L)
                        .content(objectMapper.writeValueAsString(bookingCreateDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void testPatchApproving() throws Exception {
        bookingOutputDto1 = new BookingOutputDto(
                1L,
                UserDtoMapper.toDto(user1),
                ItemDtoMapper.toOutputDto(item1),
                bookingCreateDto1.getStart(),
                bookingCreateDto1.getEnd(),
                Status.APPROVED
        );
        when(bookingService.patchApproving(1L, true, user1.getId()))
                .thenReturn(bookingOutputDto1);
        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header(USER_ID, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void tesGetBooking() throws Exception {

        when(bookingService.get(1L, user1.getId()))
                .thenReturn(bookingOutputDto1);
        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header(USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }


    @Test
    void getCurrentBookingsByBookerUserId() throws Exception {
        when(bookingService.getCurrentBookingsByBookerUserId(user3.getId(), SearchState.ALL))
                .thenReturn(List.of(bookingOutputDto1));
        mockMvc.perform(get("/bookings")
                        .header(USER_ID, 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }

    @Test
    void getCurrentBookingsByOwnerId() throws Exception {
        when(bookingService.getCurrentBookingsByOwnerId(anyLong(), any(SearchState.class)))
                .thenReturn(List.of(bookingOutputDto1));

        mockMvc.perform(get("/bookings/owner?status=ALL")
                        .content(objectMapper.writeValueAsString(bookingCreateDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingOutputDto1.id()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingOutputDto1.start()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(bookingOutputDto1.end()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].item.id", is(bookingOutputDto1.item().id()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingOutputDto1.booker().id()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(bookingOutputDto1.status().toString())));


    }
}