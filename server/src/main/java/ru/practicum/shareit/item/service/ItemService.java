package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemOutputDto add(ItemCreateDto itemCreateDto, long userId);

    ItemOutputDto update(ItemUpdateDto itemUpdateDto, long userId);

    ItemOutputDto get(long itemId, long userId);

    List<ItemOutputDto> getAllUserItems(long userId);

    List<ItemOutputDto> search(long userId, String query);

    CommentOutputDto addComment(CommentCreateDto commentCreateDto, long userId, long itemId);

}
