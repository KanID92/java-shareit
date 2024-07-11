package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.ValidationService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ValidationService validationService;


    @PostMapping
    public ItemDto add(@RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("==> POST. Adding new item: {} by user with id {}", itemDto, userId);
        validationService.validateItemDtoCreate(itemDto);
        Item item = ItemDtoMapper.fromDto(itemDto);
        log.debug("Controller. Adding item {}", item);
        item.setOwnerId(userId);
        Item receivedItem = itemService.add(item);
        ItemDto receivedItemDto = ItemDtoMapper.toDto(receivedItem);
        log.info("<== POST. Item added: {}", receivedItemDto);
        return receivedItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("==> PATCH /{itemId}. Updating item: {} by user with id {}", itemDto, userId);
        Item item = ItemDtoMapper.fromDto(itemDto);
        item.setId(itemId);
        item.setOwnerId(userId);
        ItemDto receivedItemDto = ItemDtoMapper.toDto(itemService.update(item));
        log.info("<== PATCH /{itemId}. Item updated: {}", receivedItemDto);
        return receivedItemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable long itemId) {
        log.info("==> GET /{itemId}. Getting item with id = {}", itemId);
        ItemDto itemDto = ItemDtoMapper.toDto(itemService.get(itemId));
        log.info("<== GET /{itemId}. Returning item found: {}", itemDto);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("==> GET. Getting all items of user with id {}", userId);
        List<ItemDto> itemsDtos = ItemDtoMapper.toDtos(itemService.getAllUserItems(userId));
        log.info("<== GET. Returning itemList. Size: {}", itemsDtos.size());
        return itemsDtos;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("==> GET /search. Search items by user with id {} with text = {}", userId, text);
        List<ItemDto> searchingItems = ItemDtoMapper.toDtos(itemService.search(userId, text));
        log.info("<== GET /search. Returning found items. Size of list = {}", searchingItems.size());
        return searchingItems;
    }
}
