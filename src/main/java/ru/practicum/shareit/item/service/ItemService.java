package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto add(@Valid ItemDto itemDto, long userId);

    ItemDto update(@Valid ItemDto itemDto, long userId);

    ItemDto get(long itemId);

    List<ItemDto> getAllUserItems(long userId);

    List<ItemDto> search(long userId, String query);

}
