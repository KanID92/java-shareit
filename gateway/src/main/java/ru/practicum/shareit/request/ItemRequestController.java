package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> add(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long userId,
            @Valid @RequestBody ItemRequestCreateDto requestCreateDto) {
        log.info("==> POST /requests. Adding new item request of user with id {}. Description: {}",
                userId, requestCreateDto);
        ResponseEntity<Object> receivedRequestOutputDto = itemRequestClient.add(requestCreateDto, userId);
        log.info("<== POST /requests. Returning new itemRequest of user with id {}. Description: {}",
                userId, receivedRequestOutputDto);

        return receivedRequestOutputDto;
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnRequests(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long ownerOfRequestsId) {

        log.info("==> GET /requests. Getting all item request of user with id {}.",
                ownerOfRequestsId);
        ResponseEntity<Object> receivedRequestOutputDtoList =
                itemRequestClient.getAllOwnRequests(ownerOfRequestsId);
        log.info("<== GET /requests. Returning list of itemRequests of user with id {}.",
                ownerOfRequestsId);

        return receivedRequestOutputDtoList;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")  Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("==> GET /requests/all?from={from}&size={size}. " +
                "Getting all itemRequests. From = {}, size = {}", from, size);
        ResponseEntity<Object> receivedRequestOutputDtoList =
                itemRequestClient.getAllRequests(userId, from, size);
        log.info("<== GET /requests/all?from={from}&size={size}." +
                " Returning  list of itemRequests. From = {}, size = {}",
                from, size);

        return receivedRequestOutputDtoList;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> get(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long userId,
            @PathVariable(name = "requestId") long requestId) {
        log.info("==> GET /requests/{requestId}. Getting  itemRequest with id {} by user with id {}.",
                requestId, userId);
        ResponseEntity<Object> receivedItemRequestOutputDto = itemRequestClient.get(userId, requestId);
        log.info("<== GET /requests/{requestId}. Returning  itemRequest with id {} by user with id {}. Request: {}",
                requestId, userId, receivedItemRequestOutputDto);

        return receivedItemRequestOutputDto;
    }

}
