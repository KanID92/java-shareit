package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(long userId);

    @Query(value = "SELECT * " +
            "FROM items " +
            "WHERE ((is_available = true) AND (name ILIKE ?1 OR description ILIKE ?1))",
            nativeQuery = true)
    List<Item> searchItemByByText(String searchingText);

    Optional<Item> findByIdAndOwnerId(long itemId, long userId);

}
