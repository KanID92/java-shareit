package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(BookingCreateDto bookingCreateDto, long bookingUserId) {
        return post("", bookingUserId, bookingCreateDto);
    }

    public ResponseEntity<Object> patchApproving(long bookingId, boolean isApproved, long ownerUserId) {
        return patch("/" + bookingId + "?approved=" + isApproved, ownerUserId);
    }


    public ResponseEntity<Object> get(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getCurrentBookingsByBookerUserId(long bookerUserId, SearchState state) {
        String path = "";
        if (state != null) {
            path += "?state=" + state;
        }
        return get(path, bookerUserId);
    }

    public ResponseEntity<Object> getCurrentBookingsByOwnerId(long ownerUserId, SearchState state) {
        String path = "/owner";
        if (state != null) {
            path += "?state=" + state;
        }
        return get(path, ownerUserId);
    }
}
