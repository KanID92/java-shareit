package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingCreateDtoTest {

    @Autowired
    private JacksonTester<BookingCreateDto> json;

    @Test
    public void testSerialize() throws Exception {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(3L);
        bookingCreateDto.setStart(LocalDateTime.of(2025, 1, 1, 0, 0, 0));
        bookingCreateDto.setEnd(LocalDateTime.of(2025, 2, 2, 0, 22,0));

        JsonContent<BookingCreateDto> jsonContent = json.write(bookingCreateDto);
        assertThat(jsonContent).hasJsonPath("$.itemId")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end");

        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId")
                .satisfies(itemId -> assertThat(itemId.longValue()).isEqualTo(bookingCreateDto.getItemId()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .satisfies(start -> assertThat(start).isEqualTo("2025-01-01T00:00:00"));

        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .satisfies(end -> assertThat(end).isEqualTo("2025-02-02T00:22:00"));

    }


}

