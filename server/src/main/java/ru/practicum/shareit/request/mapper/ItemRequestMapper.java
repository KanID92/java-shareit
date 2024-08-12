package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemShortOutputDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemRequestMapper {

    public static ItemRequest fromCreateDto(ItemRequestCreateDto itemRequestCreateDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestCreateDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);

        return itemRequest;
    }

    public static ItemRequestOutputDto toOutputDto(ItemRequest itemRequest) {
        return new ItemRequestOutputDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                null);
    }

    public static ItemRequestOutputDto toOutputDto(
            ItemRequest itemRequest, List<ItemShortOutputDto> itemShortOutputDtos) {
        return new ItemRequestOutputDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemShortOutputDtos);
    }
}
