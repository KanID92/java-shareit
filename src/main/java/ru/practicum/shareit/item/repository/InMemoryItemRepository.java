package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, Set<Item>> itemsByOwner = new HashMap<>();
    private long itemIdCounter = 0;

    @Override
    public Item add(Item item) {
        log.debug("Repository. Adding item {}", item);
        item.setId(getId());
        items.put(item.getId(), item);
        itemsByOwner.computeIfAbsent(item.getOwnerId(), k -> new HashSet<>());
        itemsByOwner.get(item.getOwnerId()).add(item);
        log.debug("Repository. Returning item {}", items.get(item.getId()));
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        itemsByOwner.get(item.getOwnerId()).remove(item);
        itemsByOwner.get(item.getOwnerId()).add(item);
        return items.get(item.getId());
    }

    @Override
    public Optional<Item> get(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Set<Item> getAllUserItems(long userId) {
        return itemsByOwner.getOrDefault(userId, Collections.emptySet());
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
