package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public Item add(Item item) {
        userService.getById(item.getOwnerId());
        try {
            return itemRepository.add(item);
        } catch (NotFoundException e) {
            throw new NotFoundException(
                    "User with id = " + item.getOwnerId() + "owner of Item: " + item.getName() + " - not found");
        }
    }

    public Item update(Item item) {
        userService.getById(item.getOwnerId());
        Item itemOfFromDb = get(item.getId());
        if (itemOfFromDb.getOwnerId() != item.getOwnerId()) {
            throw new AccessException("User with id = " + item.getOwnerId() + " do not own this item");
        }
        try {
            return itemRepository.update(item);
        } catch (NotFoundException e) {
            throw new NotFoundException("Owner with User id = " + item.getOwnerId() + "Item: " + item.getName() +
                    "with id = " + item.getId() +
                    "User or Item - not found");
        }
    }

    @Override
    public Item get(long itemId) {
        return itemRepository.get(itemId).orElseThrow(
                () -> new NotFoundException("Get: Item with id = " + itemId + " not found"));
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        userService.getById(userId);

        return itemRepository.getAllUserItems(userId);
    }

    @Override
    public List<Item> search(long userId, String query) {
        userService.getById(userId);

        return itemRepository.search(query);

    }

}
