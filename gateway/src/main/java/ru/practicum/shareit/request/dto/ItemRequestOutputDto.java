package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemShortOutputDto;

import java.time.LocalDateTime;
import java.util.List;

public record ItemRequestOutputDto(
        long id,
        String description,
        LocalDateTime created,
        List<ItemShortOutputDto> items
) {
}
