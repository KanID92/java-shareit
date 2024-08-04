package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
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
import java.util.List;
import java.util.Optional;

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
    public ItemOutputDto add(@Valid ItemCreateDto itemCreateDto, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Item item = ItemDtoMapper.fromCreateDto(itemCreateDto);
        item.setOwnerId(userId);
        try {
            return ItemDtoMapper.toOutputDto(itemRepository.save(item));
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Validated
    @Override
    public ItemOutputDto update(@Valid ItemUpdateDto itemUpdateDto, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Item itemFromDb = itemRepository.findById(itemUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Item with id " + itemUpdateDto.getId() + " not found"));


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
        if (itemForUpdate.getIsAvailable() == null) {
            itemForUpdate.setIsAvailable(itemFromDb.getIsAvailable());
        }

        itemForUpdate.setOwnerId(userId);

        return ItemDtoMapper.toOutputDto(itemRepository.save(itemForUpdate));
    }

    @Override
    public ItemOutputDto get(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        List<Comment> commentList = commentRepository.findAllByItemIdOrderByCreatedAsc(itemId);

        if (item.getOwnerId() != userId) {

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
    public List<ItemOutputDto> getAllUserItems(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemDtoMapper::toOutputDto)
                .toList();
    }

    @Override
    public List<ItemOutputDto> search(long userId, String textQuery) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        return itemRepository.searchItemByByText(textQuery)
                .stream()
                .map(ItemDtoMapper::toOutputDto)
                .toList();
    }

    @Override
    public CommentOutputDto addComment(CommentCreateDto commentCreateDto, long userId, long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        Comment comment = CommentDtoMapper.fromCreateDto(commentCreateDto);
        comment.setAuthor(user);
        comment.setItem(item);

        Optional<Booking> bookingOpt = bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBefore(
                itemId, userId, LocalDateTime.now());
        if (bookingOpt.isPresent()) {
            return CommentDtoMapper.toOutputDto(commentRepository.save(comment));
        } else {
            throw new ValidateException("User with id " + userId + " not booked this item with id " + itemId +
                    " , or booking is not complete.");
        }
    }

}
