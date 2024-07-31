package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ItemOutputDto add(@RequestBody ItemCreateDto itemCreateDto,
                             @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> POST. Adding new item: {} by user with id {}", itemCreateDto, userId);
        ItemOutputDto receivedItemOutputDto = itemService.add(itemCreateDto, userId);
        log.info("<== POST. Item added: {}", receivedItemOutputDto);
        return receivedItemOutputDto;
    }

    @PatchMapping("/{itemId}")
    public ItemOutputDto update(@PathVariable long itemId,
                                @RequestBody ItemUpdateDto itemUpdateDto,
                                @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> PATCH /{itemId}. Updating item: {} by user with id {}", itemUpdateDto, userId);
        itemUpdateDto.setId(itemId);
        ItemOutputDto receivedItemOutputDto = itemService.update(itemUpdateDto, userId);
        log.info("<== PATCH /{itemId}. Item updated: {}", receivedItemOutputDto);
        return receivedItemOutputDto;
    }

    @GetMapping("/{itemId}")
    public ItemOutputDto get(@PathVariable long itemId) {
        log.info("==> GET /{itemId}. Getting item with id = {}", itemId);
        ItemOutputDto itemOutputDto = itemService.get(itemId);
        log.info("<== GET /{itemId}. Returning item found: {}", itemOutputDto);
        return itemOutputDto;
    }

    @GetMapping
    public List<ItemOutputDto> getAllUserItems(@RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> GET. Getting all items of user with id {}", userId);
        List<ItemOutputDto> itemsDtos = itemService.getAllUserItems(userId);
        log.info("<== GET. Returning itemList. Size: {}", itemsDtos.size());
        return itemsDtos;
    }

    @GetMapping("/search")
    public List<ItemOutputDto> search(@RequestParam String text,
                                      @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> GET /search. Search items by user with id {} with text = {}", userId, text);
        List<ItemOutputDto> searchingItems = itemService.search(userId, text);
        log.info("<== GET /search. Returning found items. Size of list = {}", searchingItems.size());
        return searchingItems;
    }
}
