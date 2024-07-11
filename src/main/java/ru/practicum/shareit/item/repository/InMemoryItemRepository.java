package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private static long idItemCounter = 0;

    @Override
    public Item add(Item item) {
        log.debug("Repository. Adding item {}", item);
        item.setId(getId());
        items.put(item.getId(), item);
        log.debug("Repository. Returning item {}", items.get(item.getId()));
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item) {
        Item savedItem = get(item.getId()).orElseThrow(
                () -> new NotFoundException("Item with id " + item.getId() + " not found"));
        if (Optional.ofNullable(item.getName()).isEmpty()) {
            item.setName(savedItem.getName());
        }
        if (Optional.ofNullable(item.getDescription()).isEmpty()) {
            item.setDescription(savedItem.getDescription());
        }
        if (Optional.ofNullable(item.getAvailable()).isEmpty()) {
            item.setAvailable(savedItem.getAvailable());
        }
        if (item.getRequestId() == 0) {
            item.setRequestId(savedItem.getRequestId());
        }

        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Optional<Item> get(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        List<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId() == userId) {
                userItems.add(item);
            }
        }

        return userItems;
    }

    @Override
    public List<Item> search(String query) {
        List<Item> searchItems = new ArrayList<>();
        if (query.isBlank()) {
            return List.of();
        }
        for (Item item : items.values()) {
            if (item.getAvailable() && (item.getName().contains(query.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(query.toLowerCase()))) {
                searchItems.add(item);
            }
        }
        return searchItems;
    }

    private Long getId() {
        return ++idItemCounter;
    }
}
