package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";


    @PostMapping
    @Validated
    public ResponseEntity<Object> add(@Valid @RequestBody ItemCreateDto itemCreateDto,
                                      @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> POST /items. Adding new item: {} by user with id {}", itemCreateDto, userId);
        ResponseEntity<Object> receivedItemOutputDto = itemClient.add(itemCreateDto, userId);
        log.info("<== POST /items. Item added: {}", receivedItemOutputDto);
        return receivedItemOutputDto;
    }

    @PatchMapping("/{itemId}")
    @Validated
    public ResponseEntity<Object> update(@PathVariable long itemId,
                                @Valid @RequestBody ItemUpdateDto itemUpdateDto,
                                @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> PATCH /items/{itemId}. Updating item: {} by user with id {}", itemUpdateDto, userId);
        itemUpdateDto.setId(itemId);
        ResponseEntity<Object> receivedItemOutputDto = itemClient.update(itemUpdateDto, userId);
        log.info("<== PATCH /items/{itemId}. Item updated: {}", receivedItemOutputDto);
        return receivedItemOutputDto;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@PathVariable long itemId,
                             @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> GET /items/{itemId}. Getting item with id = {}", itemId);
        ResponseEntity<Object> itemOutputDto = itemClient.get(itemId, userId);
        log.info("<== GET /items/{itemId}. Returning item found: {}", itemOutputDto);
        return itemOutputDto;
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> GET /items. Getting all items of user with id {}", userId);
        ResponseEntity<Object> itemsDtos = itemClient.getAllUserItems(userId);
        log.info("<== GET /items. Returning itemList.");
        return itemsDtos;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                      @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> GET /items/search. Search items by user with id {} with text = {}", userId, text);
        ResponseEntity<Object> searchingItems = itemClient.search(userId, text);
        log.info("<== GET /items/search. Returning found items.");
        return searchingItems;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                       @RequestHeader(USER_ID_REQUEST_HEADER) long userId,
                                       @RequestBody CommentCreateDto commentCreateDto) {
        log.info("==> POST /items/{itemId}/comment. Adding comment by user with id {} to Item = {} : {}",
                userId, itemId, commentCreateDto);
        ResponseEntity<Object> commentOutputDto = itemClient.addComment(commentCreateDto, userId, itemId);
        log.info("<== POST /items/{itemId}/comment. Added comment by user with id {} to Item = {} : {}",
                userId, itemId, commentOutputDto);
        return commentOutputDto;
    }
}
