package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserUpdateDtoTest {

    private final JacksonTester<UserUpdateDto> jTester;
    private UserUpdateDto userUpdateDto;

    public UserUpdateDtoTest(@Autowired JacksonTester<UserUpdateDto> jTester) {
        this.jTester = jTester;
    }

    @BeforeEach
    void beforeEach() {
        userUpdateDto = new UserUpdateDto(1L, "Igor", "testmail@mail.ru");
    }

    @Test
    void testSerialize() throws Exception {
        JsonContent<UserUpdateDto> result = jTester.write(userUpdateDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Igor");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("testmail@mail.ru");

    }

}
