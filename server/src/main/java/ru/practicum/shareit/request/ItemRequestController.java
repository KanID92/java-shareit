package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestOutputDto add(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long userId,
            @RequestBody ItemRequestCreateDto requestCreateDto) {
        log.info("==> POST /requests. Adding new item request of user with id {}. Description: {}",
                userId, requestCreateDto);
        ItemRequestOutputDto receivedRequestOutputDto = itemRequestService.add(requestCreateDto, userId);
        log.info("<== POST /requests. Returning new itemRequest of user with id {}. Description: {}",
                userId, receivedRequestOutputDto);

        return receivedRequestOutputDto;
    }

    @GetMapping
    public List<ItemRequestOutputDto> getAllOwnRequests(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long ownerOfRequestsId) {

        log.info("==> GET /requests. Getting all item request of user with id {}.",
                ownerOfRequestsId);
        List<ItemRequestOutputDto> receivedRequestOutputDtoList =
                itemRequestService.getAllOwnRequests(ownerOfRequestsId);
        log.info("<== GET /requests. Returning list of itemRequests of user with id {}. Size: {}",
                ownerOfRequestsId, receivedRequestOutputDtoList.size());

        return receivedRequestOutputDtoList ;
    }

    @GetMapping("/all")
    public List<ItemRequestOutputDto> getAllRequests(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long userId,
            @RequestParam int from,
            @RequestParam int size) {
        log.info("==> GET /requests/all?from={from}&size={size}. " +
                "Getting all itemRequests. From = {}, size = {}", from, size);
        List<ItemRequestOutputDto> receivedRequestOutputDtoList =
                itemRequestService.getAllRequests(userId, from, size);
        log.info("<== GET /requests/all?from={from}&size={size}." +
                " Returning  list of itemRequests. From = {}, actual size = {}",
                from, receivedRequestOutputDtoList.size());

        return receivedRequestOutputDtoList;
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutputDto get(
            @RequestHeader(name = USER_ID_REQUEST_HEADER) long userId,
            @PathVariable(name = "requestId") long requestId) {
        log.info("==> GET /requests/{requestId}. Getting  itemRequest with id {} by user with id {}.",
                requestId, userId);
        ItemRequestOutputDto receivedItemRequestOutputDto = itemRequestService.get(userId, requestId);
        log.info("<== GET /requests/{requestId}. Returning  itemRequest with id {} by user with id {}. Request: {}",
                requestId, userId, receivedItemRequestOutputDto);

        return receivedItemRequestOutputDto;
    }

}
