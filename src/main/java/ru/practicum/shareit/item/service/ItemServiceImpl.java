package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Validated
    @Override
    public ItemOutputDto add(@Valid ItemCreateDto itemCreateDto, long userId) {
        userRepository.getById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found"));

        Item item = ItemDtoMapper.fromCreateDto(itemCreateDto);
        item.setOwnerId(userId);

        return ItemDtoMapper.toOutputDto(itemRepository.add(item));
    }

    @Validated
    @Override
    public ItemOutputDto update(@Valid ItemUpdateDto itemUpdateDto, long userId) {
        userRepository.getById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found"));

        Item itemFromDb = itemRepository.get(itemUpdateDto.getId()).orElseThrow(
                () -> new NotFoundException("Item with id " + itemUpdateDto.getId() + " not found")
        );
        if (itemFromDb.getOwnerId() != userId) {
            throw new AccessException("User with id = " + userId + " do not own this item");
        }


        Item itemForUpdate = ItemDtoMapper.fromUpdateDto(itemUpdateDto);

        if (itemForUpdate.getName() == null) {
            itemForUpdate.setName(itemFromDb.getName());
        }
        if (itemForUpdate.getDescription() == null) {
            itemForUpdate.setDescription(itemFromDb.getDescription());
        }
        if (itemForUpdate.getAvailable() == null) {
            itemForUpdate.setAvailable(itemFromDb.getAvailable());
        }

        itemForUpdate.setOwnerId(userId);

        return ItemDtoMapper.toOutputDto(itemRepository.update(itemForUpdate));
    }

    @Override
    public ItemOutputDto get(long itemId) {
        Item item = itemRepository.get(itemId).orElseThrow(
                () -> new NotFoundException("Get: Item with id = " + itemId + " not found"));
        return ItemDtoMapper.toOutputDto(item);
    }

    @Override
    public List<ItemOutputDto> getAllUserItems(long userId) {
        userRepository.getById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found"));

        return itemRepository.getAllUserItems(userId).stream()
                .map(ItemDtoMapper::toOutputDto)
                .toList();
    }

    @Override
    public List<ItemOutputDto> search(long userId, String query) {
        userRepository.getById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found"));

        return itemRepository.search(query).stream()
                .map(ItemDtoMapper::toOutputDto)
                .toList();

    }

}
