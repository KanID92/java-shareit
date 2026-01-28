package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemMapperTest {

    Item item1;
    User user1;
    User user2;

    BookingOutputDto bookingOutputDto3;
    BookingOutputDto bookingOutputDto4;

    Comment comment11;
    Comment comment12;


    @BeforeEach
    void setUp() {

        user1 = new User();
        user1.setName("testName1");
        user1.setEmail("testEmail1@mail.ru");
        user2 = new User(2L, "testUser2", "testEmail2@mail.ru");

        item1 = new Item();
        item1.setId(1L);
        item1.setName("testItem1");
        item1.setDescription("testItem1Description");
        item1.setIsAvailable(true);
        item1.setOwner(user1);

        bookingOutputDto3 = new BookingOutputDto(
                3L,
                UserDtoMapper.toDto(user2),
                ItemDtoMapper.toOutputDto(item1),
                LocalDateTime.of(2021, 12,2,12,2,1),
                LocalDateTime.of(2022, 1,3,13,5,3),
                Status.APPROVED);
        bookingOutputDto4 = new BookingOutputDto(
                4L,
                UserDtoMapper.toDto(user2),
                ItemDtoMapper.toOutputDto(item1),
                LocalDateTime.of(2025, 12,2,12,2,1),
                LocalDateTime.of(2025, 1,3,13,5,3),
                Status.APPROVED);

        comment11 = new Comment();
        comment11.setText("good thing");
        comment11.setItem(item1);
        comment11.setAuthor(user2);
        comment11.setCreated(LocalDateTime.of(2023,6,23, 6, 15));

        comment12 = new Comment();
        comment12.setText("bad thing");
        comment12.setItem(item1);
        comment12.setAuthor(user2);
        comment12.setCreated(LocalDateTime.of(2023,7,1, 2, 10));

    }

    @Test
    void testMapItemWithCommentAndBooking() {

        ItemOutputDto outputDtoList =
                ItemDtoMapper.toOutputDtoWithBooking(
                        item1, bookingOutputDto3, bookingOutputDto4, List.of(comment11, comment12));

        assertEquals(bookingOutputDto3, outputDtoList.lastBooking());
        assertEquals(bookingOutputDto4, outputDtoList.nextBooking());
        assertEquals("good thing", outputDtoList.comments().get(0).text());
        assertEquals("bad thing", outputDtoList.comments().get(1).text());

    }

    @Test
    void testMapItemWithComment() {

        ItemOutputDto outputDtoList =
                ItemDtoMapper.toOutputDto(
                        item1, List.of(comment11, comment12));
        assertEquals("good thing", outputDtoList.comments().get(0).text());
        assertEquals("bad thing", outputDtoList.comments().get(1).text());

    }


}
