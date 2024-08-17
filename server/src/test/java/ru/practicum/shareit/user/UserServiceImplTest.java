package ru.practicum.shareit.user;

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
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"file:src/main/resources/schema.sql"})
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;

    private UserCreateDto userCreateDto1;
    private UserCreateDto userCreateDto2;

    private UserUpdateDto userUpdateDto1;
    private UserUpdateDto userUpdateDto2;


    @BeforeEach
    void beforeEach() {
        userCreateDto1 = new UserCreateDto();
        userCreateDto1.setName("testName1");
        userCreateDto1.setEmail("testEmail1@mail.ru");

        userCreateDto2 = new UserCreateDto();
        userCreateDto2.setName("testName2");
        userCreateDto2.setEmail("testEmail2@mail.ru");

        userUpdateDto1 = new UserUpdateDto();
        userUpdateDto1.setId(1L);
        userUpdateDto1.setName("testName1Updated");
        userUpdateDto1.setEmail("testEmail1Updated@gmail.com");

        userUpdateDto2 = new UserUpdateDto();
        userUpdateDto2.setId(2L);
        userUpdateDto2.setName("testName2Updated");
        userUpdateDto2.setEmail("testEmail2Updated@gmail.com");
    }

    @AfterEach
    void afterEach() {
        em.createNativeQuery("truncate table users");
    }

    @Test
    void testUserCreatePositive() {
        userService.create(userCreateDto1);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        UserOutputDto userOutputDto = UserDtoMapper.toDto(query.setParameter("email", userCreateDto1.getEmail())
                .getSingleResult());
        assertThat(userOutputDto.id(), notNullValue());
        assertThat(userOutputDto.name(), equalTo(userCreateDto1.getName()));
        assertThat(userOutputDto.email(), equalTo(userCreateDto1.getEmail()));
    }

    @Test
    void testUserUpdateByNameAndEmailPositive() {
        userService.create(userCreateDto1);
        userService.update(userUpdateDto1);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        UserOutputDto userOutputDto = UserDtoMapper.toDto(query.setParameter("email", userUpdateDto1.getEmail())
                .getSingleResult());
        assertThat(userOutputDto.id(), equalTo(userUpdateDto1.getId()));
        assertThat(userOutputDto.name(), equalTo(userUpdateDto1.getName()));
        assertThat(userOutputDto.email(), equalTo(userUpdateDto1.getEmail()));
    }

    @Test
    void testUserUpdateByDuplicateEmailNegative() {
        userService.create(userCreateDto1);
        userService.create(userCreateDto2);
        userUpdateDto2.setEmail(userCreateDto1.getEmail());

        assertThatThrownBy(
                () -> userService.update(userUpdateDto2))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void testGetByIdUserPositive() {
        userService.create(userCreateDto1);
        UserOutputDto userOutputDto = userService.getById(1L);
        assertThat(userOutputDto.id(), equalTo(1L));
        assertThat(userOutputDto.name(), equalTo(userCreateDto1.getName()));
        assertThat(userOutputDto.email(), equalTo(userCreateDto1.getEmail()));
    }

    @Test
    void testGetByIdNotExistUser() {
        assertThatThrownBy(
                () -> userService.getById(3L))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void testDeleteUserById() {
        UserOutputDto userOutputDto = userService.create(userCreateDto1);
        userService.delete(1L);
        assertThatThrownBy(
                () -> userService.getById(userOutputDto.id())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void testGetAllUsers() {
        userService.create(userCreateDto1);
        userService.create(userCreateDto2);

        List<UserOutputDto> userOutputDtoList = userService.getAll();

        assertEquals(userOutputDtoList.size(), 2);
        assertThat(userOutputDtoList.get(0).id(), equalTo(1L));
        assertThat(userOutputDtoList.get(1).id(), equalTo(2L));

    }
}