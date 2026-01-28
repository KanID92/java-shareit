package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(ItemCreateDto itemCreateDto, long userId) {
        return post("", userId, itemCreateDto);
    }

    public ResponseEntity<Object> update(ItemUpdateDto itemUpdateDto, long userId) {
        return patch("/" + itemUpdateDto.getId(), userId, null, itemUpdateDto);
    }

    public ResponseEntity<Object> get(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllUserItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> search(long userId, String textQuery) {
        return get("/search?text=" + textQuery, userId);
    }

    public ResponseEntity<Object> addComment(CommentCreateDto commentCreateDto, long userId, long itemId) {
        return post("/" + itemId + "/comment", userId, commentCreateDto);
    }

}
