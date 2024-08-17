package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;


@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestCreateDto> jsonCreate;

    @Autowired
    private JacksonTester<ItemRequestOutputDto> jsonOutput;

    @Test
    void testSerialize() throws Exception {
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto();
        itemRequestCreateDto.setDescription("description");
        JsonContent<ItemRequestCreateDto> result = jsonCreate.write(itemRequestCreateDto);
        Assertions.assertEquals(result.getJson(), "{\"description\":\"description\"}");
    }

    @Test
    void testDeserialize() throws Exception {
        String makeJson = "{\"id\":1,\"description\":\"description\",\"created\":\"" +
                "2024-10-10T15:12:02\",\"items\":null}";
        ItemRequestOutputDto itemRequestDto = jsonOutput.parseObject(makeJson);
        Assertions.assertEquals(itemRequestDto.id(), 1);
    }
}