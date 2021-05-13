package ru.vostenzuk.jdbctest.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;
import ru.vostenzuk.jdbctest.mapper.ItemMapper;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

  private final ItemRepository repository;
  private final ItemMapper mapper;

  public ItemServiceImpl(ItemRepository repository, ItemMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<ItemDto> getAllItems() {
    return repository.findAll().stream().map(mapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  public Set<ItemDto> getAllByIds(Set<UUID> ids) {
    Set<ItemEntity> foundItems = new HashSet<>(repository.findAllById(ids));
    if (foundItems.size() != ids.size()) {
      Set<UUID> foundItemIds = foundItems.stream().map(ItemEntity::getId)
          .collect(Collectors.toSet());
      ids.removeAll(foundItemIds);
      throw new EntityNotFoundException("Items with ids " + ids + " are not found!");
    }
    return foundItems.stream().map(mapper::fromEntity).collect(Collectors.toSet());
  }

  @Override
  public ItemDto getItem(UUID id) {
    return repository.findById(id).map(mapper::fromEntity).orElseThrow(() ->
        new EntityNotFoundException(String.format("Item with id %s is not found", id)));
  }

  @Override
  public ItemDto createItem(CreateItemRequestDto request) {
    return mapper.fromEntity(repository.save(mapper.toEntity(request)));
  }

  @Override
  public ItemDto updateItem(UUID id, ItemUpdateRequestDto request) {
    ItemEntity item = repository.findById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format("Item with id %s is not found", id)));
    item.setPrice(request.getPrice());
    return mapper.fromEntity(repository.save(item));
  }
}
