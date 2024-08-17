package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemShortOutputDto;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestOutputDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public ItemRequestOutputDto add(ItemRequestCreateDto itemRequestCreateDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        ItemRequest itemRequestFromDb = itemRequestRepository.save(
                ItemRequestMapper.fromCreateDto(itemRequestCreateDto, user));
        return ItemRequestMapper.toOutputDto(itemRequestFromDb);
    }

    @Override
    public ItemRequestOutputDto get(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        ItemRequest itemRequestFromDb = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest with id " + requestId + " not found"));
        List<ItemShortOutputDto> itemsShortDtoForRequestList = itemRepository.findByRequestId(requestId)
                .stream()
                .map(ItemDtoMapper::toShortOutputDto)
                .toList();
        return ItemRequestMapper.toOutputDto(itemRequestFromDb, itemsShortDtoForRequestList);
    }

    @Override
    public List<ItemRequestOutputDto> getAllOwnRequests(long ownerOfRequestsId) {
        userRepository.findById(ownerOfRequestsId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerOfRequestsId + " not found"));
        List<ItemRequest> itemRequestOfOwnerList = itemRequestRepository.findAllByRequestorId(ownerOfRequestsId);
        List<Long> itemRequestIds = itemRequestOfOwnerList
                .stream()
                .map(ItemRequest::getId)
                .toList();
        Map<Long, List<Item>> itemsMapByRequestId = itemRepository.findByRequestIdIn(
                itemRequestIds)
                .stream()
                .collect(groupingBy(Item::getRequestId, toList()));
        Map<Long, List<ItemShortOutputDto>> requestIdItemShortOutputDtoMap = new HashMap<>();
        for (Long requestId : itemsMapByRequestId.keySet()) {
            List<ItemShortOutputDto> itemShortOutputDtos = itemsMapByRequestId.get(requestId)
                    .stream()
                    .map(ItemDtoMapper::toShortOutputDto)
                    .toList();
            requestIdItemShortOutputDtoMap.put(requestId, itemShortOutputDtos);
        }

        return itemRequestOfOwnerList
                .stream()
                .map(request -> ItemRequestMapper.toOutputDto(
                        request, requestIdItemShortOutputDtoMap.get(request.getId())))
                .sorted(Collections.reverseOrder(Comparator.comparing(ItemRequestOutputDto::created)))
                .toList();
    }

    @Override
    public List<ItemRequestOutputDto> getAllRequests(long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Sort sortByCreatedDesc = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sortByCreatedDesc);

        return itemRequestRepository.findAll(page)
                .stream()
                .map(ItemRequestMapper::toOutputDto)
                .toList();

    }
}
