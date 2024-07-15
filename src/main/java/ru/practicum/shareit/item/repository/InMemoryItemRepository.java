package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private long itemIdCounter = 0;

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

        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
    }

    @Override
    public List<Item> search(String query) {

        if (query.isBlank()) {
            return List.of();
        }

        return items.values().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().contains(query.toLowerCase())
                                ||
                                item.getDescription().toLowerCase().contains(query.toLowerCase())))
                .toList();
    }

    private Long getId() {
        return ++itemIdCounter;
    }

}
