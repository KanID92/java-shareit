package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserCreateDtoTest {

    private final JacksonTester<UserCreateDto> jTester;
    private UserCreateDto userCreateDto;

    public UserCreateDtoTest(@Autowired JacksonTester<UserCreateDto> jTester) {
        this.jTester = jTester;
    }

    @BeforeEach
    void beforeEach() {
        userCreateDto = new UserCreateDto("Igor", "testmail@mail.ru");
    }

    @Test
    void testSerialize() throws Exception {
        JsonContent<UserCreateDto> result = jTester.write(userCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Igor");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("testmail@mail.ru");

    }

}
