package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Marker;

import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Validated({Marker.OnCreate.class})
    @Override
    public ItemDto add(@Valid ItemDto itemDto, long userId) {
        userService.getById(userId);

        Item item = ItemDtoMapper.fromDto(itemDto);
        item.setOwnerId(userId);

        return ItemDtoMapper.toDto(itemRepository.add(item));

    }

    @Validated({Marker.OnUpdate.class})
    @Override
    public ItemDto update(@Valid ItemDto itemDto, long userId) {
        userService.getById(userId);
        Item itemOfFromDb = itemRepository.get(itemDto.getId()).orElseThrow(
                () -> new NotFoundException("Item with id " + itemDto.getId() + " not found")
        );
        if (itemOfFromDb.getOwnerId() != userId) {
            throw new AccessException("User with id = " + userId + " do not own this item");
        }

        Item itemForUpdate = ItemDtoMapper.fromDto(itemDto);
        itemForUpdate.setOwnerId(userId);

        return ItemDtoMapper.toDto(itemRepository.update(itemForUpdate));
    }

    @Override
    public ItemDto get(long itemId) {
        Item item = itemRepository.get(itemId).orElseThrow(
                () -> new NotFoundException("Get: Item with id = " + itemId + " not found"));
        return ItemDtoMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllUserItems(long userId) {
        userService.getById(userId);

        return itemRepository.getAllUserItems(userId).stream()
                .map(ItemDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(long userId, String query) {
        userService.getById(userId);

        return itemRepository.search(query).stream()
                .map(ItemDtoMapper::toDto)
                .toList();

    }

}
