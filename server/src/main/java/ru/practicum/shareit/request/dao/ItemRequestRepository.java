package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Long userId);

    List<ItemRequest> findAllByRequestorIdNotOrderByCreatedDesc(Long userId);

    List<ItemRequest> findAllByRequestorId(Long userId);

}