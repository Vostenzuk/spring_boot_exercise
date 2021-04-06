package ru.vostenzuk.jdbctest.service;

import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.CreateItemRequest;
import ru.vostenzuk.jdbctest.dto.ItemUpdateRequest;
import ru.vostenzuk.jdbctest.mapper.ItemMapper;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository repository;
    private final ItemMapper mapper;

    public ItemService(ItemRepository repository,
                       ItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ItemEntity> getAllItems() {
        return repository.findAll();
    }

    public ItemEntity getItem(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Item with id %s is not found", id)));
    }

    public ItemEntity createItem(CreateItemRequest request) {
        return repository.save(mapper.fromRequest(request));
    }

    public ItemEntity updateItem(UUID id, ItemUpdateRequest request) {
        ItemEntity item = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Item with id %s is not found", id)));
        item.setPrice(request.getPrice());
        return repository.save(item);
    }
}
