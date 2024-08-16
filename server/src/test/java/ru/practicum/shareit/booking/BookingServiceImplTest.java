package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/main/resources/schema.sql"})
class BookingServiceImplTest {

    private final EntityManager em;

    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User user1;
    private User user2;
    private User user3;

    private Item item1;
    private Item item2;

    private BookingCreateDto bookingCreateDto1;
    private BookingCreateDto bookingCreateDto2;


    @BeforeEach
    void setUp() {
        user1 = new User(1L, "testUser1", "testEmail1@mail.ru");
        user2 = new User(2L, "testUser2", "testEmail2@mail.ru");
        user3 = new User(3L, "testUser3", "testEmail3@mail.ru");

        item1 = new Item();
        item1.setId(1L);
        item1.setName("testItem1");
        item1.setDescription("testItem1Description");
        item1.setIsAvailable(true);
        item1.setOwner(user1);

        item2 = new Item();
        item2.setId(2L);
        item2.setName("testItem2");
        item2.setDescription("testItem2Description");
        item2.setIsAvailable(true);
        item2.setOwner(user2);

        bookingCreateDto1 = new BookingCreateDto();
        bookingCreateDto1.setItemId(item1.getId());
        bookingCreateDto1.setStart(LocalDateTime.of(2025, 3, 10, 13, 1, 10));
        bookingCreateDto1.setEnd(LocalDateTime.of(2025, 3, 12, 12, 0, 0));

        bookingCreateDto2 = new BookingCreateDto();
        bookingCreateDto2.setItemId(item2.getId());
        bookingCreateDto2.setStart(LocalDateTime.of(2025, 10, 1, 11, 17, 50));
        bookingCreateDto2.setEnd(LocalDateTime.of(2025, 12, 3, 7, 0, 0));

    }

    @AfterEach
    void tearDown() {
        em.createNativeQuery("truncate table bookings");
        em.createNativeQuery("truncate table items");
        em.createNativeQuery("truncate table users");
    }

    @Test
    void testAddPositive() {
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);

        bookingService.add(bookingCreateDto1, user2.getId());

        TypedQuery<Booking> queryBooking = em.createQuery(
                "Select b from Booking b where b.booker.name like :bookingItemOwnerName", Booking.class);
        BookingOutputDto bookingOutputDto = BookingDtoMapper.toDto(queryBooking.setParameter(
                "bookingItemOwnerName", user2.getName()).getSingleResult());

        assertThat(bookingOutputDto.id(), notNullValue());
        assertThat(bookingOutputDto.item().id(), equalTo(1L));
        assertThat(bookingOutputDto.item().name(), equalTo(item1.getName()));
        assertThat(bookingOutputDto.booker().id(), equalTo(user2.getId()));
        assertThat(bookingOutputDto.booker().name(), equalTo(user2.getName()));
        assertThat(bookingOutputDto.start(), equalTo(bookingCreateDto1.getStart()));
        assertThat(bookingOutputDto.end(), equalTo(bookingCreateDto1.getEnd()));
        assertThat(bookingOutputDto.status(), equalTo(Status.WAITING));

    }

    @Test
    void testAddNegative() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item1);
        itemRepository.save(item2);

        bookingCreateDto2.setStart(LocalDateTime.of(2025, 2, 1, 11, 17, 50));
        bookingCreateDto2.setEnd(LocalDateTime.of(2025, 4, 3, 7, 0, 0));

        BookingOutputDto bookingOutputDto1 = bookingService.add(bookingCreateDto1, user2.getId());

        bookingService.patchApproving(bookingOutputDto1.id(),true,item1.getId());

        assertThrows(NotAvailableException.class, () -> bookingService.add(bookingCreateDto1, user3.getId()));

    }

    @Test
    void testPatchApprovingPositive() {
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);

        BookingOutputDto creatingBookingOutputDto = bookingService.add(bookingCreateDto1, user2.getId());

        BookingOutputDto updateBookingOutputDto = bookingService.patchApproving(creatingBookingOutputDto.id(),true, user1.getId());

        assertThat(updateBookingOutputDto.status(), equalTo(Status.APPROVED));

    }

    @Test
    void testPatchNotApprovingByOwner() {
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);

        BookingOutputDto creatingBookingOutputDto = bookingService.add(bookingCreateDto1, user2.getId());

        BookingOutputDto updateBookingOutputDto = bookingService.patchApproving(creatingBookingOutputDto.id(),false, user1.getId());

        assertThat(updateBookingOutputDto.status(), notNullValue());
        assertThat(updateBookingOutputDto.status(), equalTo(Status.REJECTED));

    }

    @Test
    void testPatchNApprovingByNotOwnerNegative() {
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);

        BookingOutputDto creatingBookingOutputDto = bookingService.add(bookingCreateDto1, user2.getId());

        assertThrows(ValidateException.class,
                () -> bookingService.patchApproving(creatingBookingOutputDto.id(),false, user2.getId()));

    }

    @Test
    void testGetBookingByOwnerOrBooker() {
        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        itemRepository.save(item2);

        BookingOutputDto creatingBookingOutputDto1 = bookingService.add(bookingCreateDto1, user2.getId());
        BookingOutputDto creatingBookingOutputDto2 = bookingService.add(bookingCreateDto2, user1.getId());

        BookingOutputDto bookingOutputDto1 = bookingService.get(creatingBookingOutputDto2.id(), user1.getId());
        BookingOutputDto bookingOutputDto2 = bookingService.get(creatingBookingOutputDto2.id(), user2.getId());

        assertEquals(bookingOutputDto1,bookingOutputDto2);

        assertThat(bookingOutputDto1.item().id(), equalTo(2L));

        assertThat(bookingOutputDto1.id(), notNullValue());
        assertThat(bookingOutputDto1.item().id(), equalTo(2L));
        assertThat(bookingOutputDto1.item().name(), equalTo(item2.getName()));
        assertThat(bookingOutputDto1.booker().id(), equalTo(user1.getId()));
        assertThat(bookingOutputDto1.booker().name(), equalTo(user1.getName()));
        assertThat(bookingOutputDto1.start(), equalTo(bookingCreateDto2.getStart()));
        assertThat(bookingOutputDto1.end(), equalTo(bookingCreateDto2.getEnd()));
        assertThat(bookingOutputDto1.status(), equalTo(Status.WAITING));
    }

    @Test
    void testGetBookingByNotOwnerOrBookerNegative() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item1);
        itemRepository.save(item2);

        BookingOutputDto creatingBookingOutputDto1 = bookingService.add(bookingCreateDto1, user2.getId());

        assertThrows(AccessException.class, () -> bookingService.get(creatingBookingOutputDto1.id(), user3.getId()));

    }

    @Test
    void getCurrentBookingsByBookerUserId() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item1);
        itemRepository.save(item2);

        BookingOutputDto creatingBookingOutputDto1 = bookingService.add(bookingCreateDto1, user3.getId());
        BookingOutputDto creatingBookingOutputDto2 = bookingService.add(bookingCreateDto2, user3.getId());

        BookingOutputDto bookingOutputDto1 = bookingService.patchApproving(
                creatingBookingOutputDto1.id(),true, user1.getId());
        BookingOutputDto bookingOutputDto2 = bookingService.patchApproving(
                creatingBookingOutputDto2.id(),true, user2.getId());

        List<BookingOutputDto> bookingByBooker = bookingService.getCurrentBookingsByBookerUserId(
                user3.getId(), "FUTURE");

        assertEquals(2, bookingByBooker.size());
        assertEquals(bookingOutputDto1, bookingByBooker.get(1));
        assertEquals(bookingOutputDto2, bookingByBooker.get(0));
    }

    @Test
    void getCurrentBookingsByOwnerId() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item1);
        itemRepository.save(item2);

        BookingOutputDto creatingBookingOutputDto1 = bookingService.add(bookingCreateDto1, user3.getId());
        BookingOutputDto bookingOutputDto1 = bookingService.patchApproving(
                creatingBookingOutputDto1.id(),true, user1.getId());

        List<BookingOutputDto> bookingsByOwner =  bookingService.getCurrentBookingsByOwnerId(
                user1.getId(),"ALL");

        assertEquals(1, bookingsByOwner.size());
        assertEquals(bookingOutputDto1, bookingsByOwner.getFirst());
    }

}