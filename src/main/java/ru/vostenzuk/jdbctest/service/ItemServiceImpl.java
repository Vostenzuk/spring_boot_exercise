package ru.vostenzuk.jdbctest.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.mapper.ItemMapper;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

  private final ItemRepository repository;

  public ItemServiceImpl(ItemRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<ItemEntity> getAllItems() {
    return repository.findAll();
  }

  @Override
  public Set<ItemEntity> getAllByIds(Set<UUID> ids) {
    Set<ItemEntity> foundItems = new HashSet<>(repository.findAllById(ids));
    if (foundItems.size() != ids.size()) {
      Set<UUID> foundItemIds = foundItems.stream().map(ItemEntity::getId)
          .collect(Collectors.toSet());
      ids.removeAll(foundItemIds);
      throw new EntityNotFoundException("Items with ids " + ids + " are not found!");
    }
    return foundItems;
  }

  @Override
  public ItemEntity getItem(UUID id) {
    return repository.findById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format("Item with id %s is not found", id)));
  }

  @Override
  public ItemEntity createItem(ItemEntity itemEntity) {
    return repository.save(itemEntity);
  }

  @Override
  public ItemEntity updateItem(UUID id, ItemEntity itemEntity) {
    ItemEntity item = repository.findById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format("Item with id %s is not found", id)));
    item.setPrice(itemEntity.getPrice());
    return repository.save(item);
  }
}
