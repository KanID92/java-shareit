package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void addUsers() {
        userRepository.save(new User(1L, "john", "john@doe.com"));
        userRepository.save(new User(2L, "jane", "jane@doe.com"));
        userRepository.save(new User(3L, "mary", "mary@doe.com"));
    }

    @AfterEach
    void removeUsers() {
        userRepository.deleteAll();
    }

    @Test
    void testCheckForEmailExisting() {
        int result = userRepository.checkForEmailExisting("jane@doe.com");
        assertEquals(1, result);
    }
}