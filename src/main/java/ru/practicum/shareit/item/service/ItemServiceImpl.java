package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentDtoMapper;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Validated
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Validated
    @Override
    @Transactional
    public ItemOutputDto add(@Valid ItemCreateDto itemCreateDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Item item = ItemDtoMapper.fromCreateDto(itemCreateDto);
        item.setOwner(user);
        try {
            return ItemDtoMapper.toOutputDto(itemRepository.save(item));
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Validated
    @Override
    @Transactional
    public ItemOutputDto update(@Valid ItemUpdateDto itemUpdateDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Item itemFromDb = itemRepository.findById(itemUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Item with id " + itemUpdateDto.getId() + " not found"));


        if (itemFromDb.getOwner().getId() != userId) {
            throw new AccessException("User with id = " + userId + " do not own this item");
        }


        Item itemForUpdate = ItemDtoMapper.fromUpdateDto(itemUpdateDto);

        if (itemForUpdate.getName() == null) {
            itemForUpdate.setName(itemFromDb.getName());
        }
        if (itemForUpdate.getDescription() == null) {
            itemForUpdate.setDescription(itemFromDb.getDescription());
        }
        if (itemForUpdate.getIsAvailable() == null) {
            itemForUpdate.setIsAvailable(itemFromDb.getIsAvailable());
        }

        itemForUpdate.setOwner(user);

        return ItemDtoMapper.toOutputDto(itemRepository.save(itemForUpdate));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemOutputDto get(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        List<Comment> commentList = commentRepository.findAllByItemIdOrderByCreatedAsc(itemId);

        if (item.getOwner().getId() != userId) {

            return ItemDtoMapper.toOutputDto(item, commentList);

        } else {
            Optional<Booking> lastBookingOpt = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(
                    itemId, LocalDateTime.now());

            Optional<Booking> nextBookingOpt = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(
                    itemId, LocalDateTime.now());

            return ItemDtoMapper.toOutputDtoWithBooking(
                    item,
                    lastBookingOpt.map(BookingDtoMapper::toDto).orElse(null),
                    nextBookingOpt.map(BookingDtoMapper::toDto).orElse(null),
                    commentList);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemOutputDto> getAllUserItems(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        List<Item> itemList = itemRepository.findAllByOwnerId(userId);
        Map<Item, List<Comment>> comments =
                commentRepository.findByItemIn(itemList, Sort.by(Sort.Direction.DESC, "created"))
                        .stream()
                        .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, Booking> lastBookings = bookingRepository.findFirstByItemInAndEndBefore(
                itemList, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "end"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, booking -> booking));
        Map<Item, Booking> nextBookings = bookingRepository.findFirstByItemInAndStartAfter(
                        itemList, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, booking -> booking));

        List<ItemOutputDto> itemOutputDtoList = new ArrayList<>();

        for (Item item : itemList) {

            Optional<Booking> lastBookingOpt = Optional.ofNullable(lastBookings.get(item));
            BookingOutputDto lastBookingOutput = lastBookingOpt.map(BookingDtoMapper::toDto).orElse(null);
            Optional<Booking> nextBookingOpt = Optional.ofNullable(nextBookings.get(item));
            BookingOutputDto nextBookingOutput = nextBookingOpt.map(BookingDtoMapper::toDto).orElse(null);

            itemOutputDtoList.add(
                    ItemDtoMapper.toOutputDtoWithBooking(
                            item,
                            lastBookingOutput,
                            nextBookingOutput,
                            comments.get(item)));
        }

        return itemOutputDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemOutputDto> search(long userId, String textQuery) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return itemRepository.searchItemByByText(textQuery)
                .stream()
                .map(ItemDtoMapper::toOutputDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentOutputDto addComment(CommentCreateDto commentCreateDto, long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        Optional<Booking> bookingOpt = bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBefore(
                itemId, userId, LocalDateTime.now());
        if (bookingOpt.isPresent()) {
            Comment comment = CommentDtoMapper.fromCreateDto(commentCreateDto);
            comment.setAuthor(user);
            comment.setItem(item);
            return CommentDtoMapper.toOutputDto(commentRepository.save(comment));
        } else {
            throw new ValidateException("User with id " + userId + " not booked this item with id " + itemId +
                    " , or booking is not complete.");
        }
    }

}
