package ru.practicum.shareit.item;

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
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/main/resources/schema.sql"})
class ItemServiceImplTest {

    private final EntityManager em;

    private final ItemService itemService;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    ItemCreateDto itemCreateDto1;
    Booking booking1;
    Comment comment11;
    Comment comment12;

    Booking booking2;

    ItemCreateDto itemCreateDto2;



    ItemUpdateDto itemUpdateDto1;
    ItemUpdateDto itemUpdateDto2;

    User user1;
    User user2;
    @Autowired
    private ItemRepository itemRepository;


    @BeforeEach
    void setUp() {
        itemCreateDto1 = new ItemCreateDto();
        itemCreateDto1.setName("testItem1");
        itemCreateDto1.setDescription("testDescription1");
        itemCreateDto1.setAvailable(true);
        itemCreateDto1.setRequestId(null);
        booking1 = new Booking();
        booking1.setStart((LocalDateTime.of(2001,1,30, 2, 40)));
        booking1.setEnd((LocalDateTime.of(2003,5,21, 5, 12)));
        booking2 = new Booking();
        booking2.setStart((LocalDateTime.of(2101,1,30, 2, 40)));
        booking2.setEnd((LocalDateTime.of(2203,5,21, 5, 12)));


        comment11 = new Comment();
        comment11.setText("good thing");
        comment11.setCreated(LocalDateTime.of(2003,6,23, 6, 15));
        comment12 = new Comment();
        comment12.setText("bad thing");
        comment12.setCreated(LocalDateTime.of(2008,7,1, 2, 10));


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
        itemUpdateDto2.setName("testUpdatename");
        itemUpdateDto2.setDescription("testdescription");
        itemUpdateDto2.setAvailable(false);

        user1 = new User();
        user1.setName("testName1");
        user1.setEmail("testEmail1@mail.ru");

        user2 = new User();
        user2.setName("testName2");
        user2.setEmail("testEmail2@mail.ru");

    }

    @AfterEach
    void tearDown() {
        em.createNativeQuery("truncate table comments");
        em.createNativeQuery("truncate table bookings");
        em.createNativeQuery("truncate table items");
        em.createNativeQuery("truncate table users");
    }

    @Test
    void testAddItem() {
        User testUser = userRepository.save(user1);

        itemService.add(itemCreateDto1, testUser.getId());

        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name like :item", Item.class);
        ItemOutputDto itemOutputDto = ItemDtoMapper.toOutputDto(queryItem.setParameter(
                "item", itemCreateDto1.getName()).getSingleResult());

        assertThat(itemOutputDto.id(), notNullValue());
        assertThat(itemOutputDto.name(), equalTo(itemCreateDto1.getName()));
        assertThat(itemOutputDto.description(), equalTo(itemCreateDto1.getDescription()));
        assertThat(itemOutputDto.available(), equalTo(true));

    }

    @Test
    void testUpdateItemByNameAndDescriptionAndAvailablePositive() {
        User testUser = userRepository.save(user1);

        itemService.add(itemCreateDto1, testUser.getId());
        itemService.add(itemCreateDto2, testUser.getId());
        ItemOutputDto itemOutputDto = itemService.update(itemUpdateDto2, testUser.getId());

        assertThat(itemOutputDto.id(), equalTo(itemUpdateDto2.getId()));
        assertThat(itemOutputDto.name(), equalTo(itemUpdateDto2.getName()));
        assertThat(itemOutputDto.description(), equalTo(itemUpdateDto2.getDescription()));
        assertThat(itemOutputDto.available(), equalTo(itemUpdateDto2.getAvailable()));

    }

    @Test
    void testUpdateItemByAvailablePositive() {
        User testUser = userRepository.save(user1);

        itemService.add(itemCreateDto1, testUser.getId());
        itemService.add(itemCreateDto2, testUser.getId());

        ItemOutputDto itemOutputDto = itemService.update(itemUpdateDto1, testUser.getId());

        assertThat(itemOutputDto.id(), equalTo(itemUpdateDto1.getId()));
        assertThat(itemOutputDto.name(), equalTo(itemCreateDto2.getName()));
        assertThat(itemOutputDto.description(), equalTo(itemCreateDto2.getDescription()));
        assertThat(itemOutputDto.available(), equalTo(itemUpdateDto1.getAvailable()));

    }

    @Test
    void testGetItemByIdWithCommentAndBooking() {
        User testUser1 = userRepository.save(user1);
        User testUser2 = userRepository.save(user2);
        ItemOutputDto itemOutputDto = itemService.add(itemCreateDto1, testUser1.getId());


        booking1.setBooker(testUser2);
        booking1.setItem(itemRepository.findById(itemOutputDto.id()).get());
        booking1.setStatus(Status.APPROVED);

        booking2.setBooker(testUser2);
        booking2.setItem(itemRepository.findById(itemOutputDto.id()).get());
        booking2.setStatus(Status.APPROVED);

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        comment11.setAuthor(testUser2);
        comment12.setAuthor(testUser2);
        comment11.setItem(itemRepository.findById(itemOutputDto.id()).get());
        comment12.setItem(itemRepository.findById(itemOutputDto.id()).get());

        commentRepository.save(comment11);
        commentRepository.save(comment12);

        ItemOutputDto itemOutputDtoForTest = itemService.get(1L, testUser1.getId());

        assertThat(itemOutputDtoForTest.name(), equalTo(itemCreateDto1.getName()));
        assertThat(itemOutputDtoForTest.id(), equalTo(itemOutputDto.id()));
        assertThat(itemOutputDtoForTest.description(), equalTo(itemCreateDto1.getDescription()));
        assertThat(itemOutputDtoForTest.nextBooking(), equalTo(BookingDtoMapper.toDto(booking2)));
        assertThat(itemOutputDtoForTest.lastBooking(), equalTo(BookingDtoMapper.toDto(booking1)));
        assertThat(itemOutputDtoForTest.comments().get(1), equalTo(CommentDtoMapper.toOutputDto(comment12)));
        assertThat(itemOutputDtoForTest.comments().get(0), equalTo(CommentDtoMapper.toOutputDto(comment11)));

    }

    @Test
    void getAllUserItems() {
        User testUser1 = userRepository.save(user1);
        User testUser2 = userRepository.save(user2);

        ItemOutputDto itemOutputDto1 = itemService.add(itemCreateDto1, testUser1.getId());
        ItemOutputDto itemOutputDto2 = itemService.add(itemCreateDto2, testUser1.getId());

        List<ItemOutputDto> itemOutputDtoList = itemService.getAllUserItems(testUser1.getId());

        assertThat(itemOutputDtoList.get(0).name(), equalTo(itemCreateDto1.getName()));
        assertThat(itemOutputDtoList.get(0).id(), notNullValue());
        assertThat(itemOutputDtoList.get(0).description(), equalTo(itemCreateDto1.getDescription()));
        assertThat(itemOutputDtoList.get(1).name(), equalTo(itemCreateDto2.getName()));
        assertThat(itemOutputDtoList.get(1).id(), notNullValue());
        assertThat(itemOutputDtoList.get(1).description(), equalTo(itemCreateDto2.getDescription()));

    }

    @Test
    void search() {
        User user1Db = userRepository.save(user1);
        User user2Db = userRepository.save(user2);

        itemService.add(itemCreateDto1, user1.getId());
        itemService.add(itemCreateDto2, user1.getId());
        List<ItemOutputDto> itemOutputDtoList = itemService.search(user2.getId(), "testDescription1");
        assertEquals(1, itemOutputDtoList.size());
    }

    @Test
    void testAddComment() {
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("Good item");

        User testUser1 = userRepository.save(user1);
        User testUser2 = userRepository.save(user2);
        ItemOutputDto itemOutputDto = itemService.add(itemCreateDto1, testUser1.getId());

        booking1.setBooker(testUser2);
        booking1.setItem(itemRepository.findById(itemOutputDto.id()).get());
        booking1.setStatus(Status.APPROVED);
        bookingRepository.save(booking1);

        CommentOutputDto commentOutputDto = itemService.addComment(commentCreateDto, testUser2.getId(),itemOutputDto.id());

        assertThat(commentOutputDto.id(), notNullValue());
        assertThat(commentOutputDto.text(), equalTo(commentCreateDto.getText()));
        assertThat(commentOutputDto.authorName(), equalTo(user2.getName()));
        assertThat(commentOutputDto.item(), equalTo(itemOutputDto));
    }

}