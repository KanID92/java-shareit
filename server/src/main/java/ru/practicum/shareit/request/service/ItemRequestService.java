package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestOutputDto add(ItemRequestCreateDto itemRequestCreateDto, long userId);

    ItemRequestOutputDto get(long requestId, long userId);

    List<ItemRequestOutputDto> getAllOwnRequests(long ownerOfRequestsId);

    List<ItemRequestOutputDto> getAllRequests(long userId, int from, int size);

}
