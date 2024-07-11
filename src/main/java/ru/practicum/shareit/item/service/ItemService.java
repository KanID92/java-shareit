package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item add(Item item);

    Item update(Item item);

    Item get(long itemId);

    List<Item> getAllUserItems(long userId);

    List<Item> search(long userId, String query);


}
