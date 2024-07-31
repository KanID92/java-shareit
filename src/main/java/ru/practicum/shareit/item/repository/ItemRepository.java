package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemRepository {

    Item add(Item item);

    Item update(Item item);

    Optional<Item> get(long itemId);

    Set<Item> getAllUserItems(long userId);

    List<Item> search(String query);

}
