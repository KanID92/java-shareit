package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    ItemOutputDto add(@Valid ItemCreateDto itemCreateDto, long userId);

    ItemOutputDto update(@Valid ItemUpdateDto itemUpdateDto, long userId);

    ItemOutputDto get(long itemId);

    List<ItemOutputDto> getAllUserItems(long userId);

    List<ItemOutputDto> search(long userId, String query);

}
