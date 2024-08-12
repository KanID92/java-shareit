package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorId(Long requestorId);

    @Query(nativeQuery = true,
    value = "SELECT * " +
            "FROM (" +
            "SELECT ROW_NUMBER() OVER (ORDER BY CREATED DESC) AS ROW_NUMBER, * " +
            "FROM REQUESTS) WHERE ROW_NUMBER >= ?1 " +
            "LIMIT ?2")
    List<ItemRequest> findItemRequestWithFromAndSize(int from, int size);

}
