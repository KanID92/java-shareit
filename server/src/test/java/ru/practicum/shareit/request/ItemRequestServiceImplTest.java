package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/main/resources/schema.sql"})
class ItemRequestServiceImplTest {

    private final EntityManager em;

    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User user1;
    private User user2;
    private User user3;

    private Item item1;
    private Item item2;

    private ItemRequestCreateDto itemRequestCreateDto1;
    private ItemRequestCreateDto itemRequestCreateDto2;


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

        itemRequestCreateDto1 = new ItemRequestCreateDto();
        itemRequestCreateDto1.setDescription("Need reliable strong hammer");

        itemRequestCreateDto2 = new ItemRequestCreateDto();
        itemRequestCreateDto2.setDescription("Need small drill");


    }

    @AfterEach
    void tearDown() {
        em.createNativeQuery("truncate table REQUESTS");
        em.createNativeQuery("truncate table items");
        em.createNativeQuery("truncate table users");
    }

    @Test
    void testAddItemRequest() {
        userRepository.save(user1);

        ItemRequestOutputDto itemRequestOutputDto1 = itemRequestService.add(itemRequestCreateDto1,user1.getId());

        assertThat(itemRequestOutputDto1.id(), notNullValue());
        assertThat(itemRequestOutputDto1.description(), equalTo(itemRequestCreateDto1.getDescription()));
        assertThat(itemRequestOutputDto1.created(), notNullValue());

    }

    @Test
    void testGet() {
        User testUser1 = userRepository.save(user1);
        User testUser3 = userRepository.save(user3);

        ItemRequestOutputDto creatingItemRequestOutputDto = itemRequestService.add(
                itemRequestCreateDto1, testUser3.getId());

        item1.setRequestId(creatingItemRequestOutputDto.id());
        itemRepository.save(item1);

        ItemRequestOutputDto itemRequestOutputDto =
                itemRequestService.get(testUser3.getId(), creatingItemRequestOutputDto.id());

        assertThat(itemRequestOutputDto.id(), notNullValue());
        assertThat(itemRequestOutputDto.description(), equalTo(itemRequestCreateDto1.getDescription()));
        assertThat(itemRequestOutputDto.created(), notNullValue());
        assertThat(itemRequestOutputDto.items().get(0), equalTo(ItemDtoMapper.toShortOutputDto(item1)));

    }

    @Test
    void getAllOwnRequests() {
        User testUser1 = userRepository.save(user1);
        User testUser3 = userRepository.save(user3);

        ItemRequestOutputDto creatingItemRequestOutputDto1 = itemRequestService.add(
                itemRequestCreateDto1, testUser3.getId());

        ItemRequestOutputDto creatingItemRequestOutputDto2 = itemRequestService.add(
                itemRequestCreateDto2, testUser3.getId());

        item1.setRequestId(creatingItemRequestOutputDto1.id());
        itemRepository.save(item1);

        item2.setRequestId(creatingItemRequestOutputDto2.id());
        itemRepository.save(item2);

        List<ItemRequestOutputDto> itemRequestOutputDtoList = itemRequestService.getAllOwnRequests(testUser3.getId());

        assertThat(itemRequestOutputDtoList.size(), equalTo(2));
        assertThat(itemRequestOutputDtoList.get(0).description(), equalTo(itemRequestCreateDto2.getDescription()));
        assertThat(itemRequestOutputDtoList.get(1).description(), equalTo(itemRequestCreateDto1.getDescription()));

    }

    @Test
    void getAllRequests() {
        User testUser1 = userRepository.save(user1);
        User testUser2 = userRepository.save(user2);
        User testUser3 = userRepository.save(user3);

        ItemRequestCreateDto itemRequestCreateDto3 = new ItemRequestCreateDto();
        itemRequestCreateDto3.setDescription("Need reliable sharp spire");

        ItemRequestCreateDto itemRequestCreateDto4 = new ItemRequestCreateDto();
        itemRequestCreateDto4.setDescription("Need deep plate");

        itemRequestService.add(
                itemRequestCreateDto1, testUser3.getId());

        itemRequestService.add(
                itemRequestCreateDto2, testUser3.getId());

        itemRequestService.add(
                itemRequestCreateDto3, testUser2.getId());

        itemRequestService.add(
                itemRequestCreateDto4, testUser1.getId());

        List<ItemRequestOutputDto> itemRequestOutputDtoList = itemRequestService.getAllRequests(testUser2.getId(), 0, 2);

        assertThat(itemRequestOutputDtoList.size(), equalTo(2));

    }
}