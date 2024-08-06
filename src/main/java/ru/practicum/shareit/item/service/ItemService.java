package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemOutputDto add(@Valid ItemCreateDto itemCreateDto, long userId);

    ItemOutputDto update(@Valid ItemUpdateDto itemUpdateDto, long userId);

    ItemOutputDto get(long itemId, long userId);

    List<ItemOutputDto> getAllUserItems(long userId);

    List<ItemOutputDto> search(long userId, String query);

    CommentOutputDto addComment(CommentCreateDto commentCreateDto, long userId, long itemId);

}
