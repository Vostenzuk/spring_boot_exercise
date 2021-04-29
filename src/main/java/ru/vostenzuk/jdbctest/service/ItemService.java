package ru.vostenzuk.jdbctest.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import ru.vostenzuk.jdbctest.domain.ItemEntity;

public interface ItemService {

  List<ItemEntity> getAllItems();

  Set<ItemEntity> getAllByIds(Set<UUID> ids);

  ItemEntity getItem(UUID id);

  ItemEntity createItem(ItemEntity itemEntity);

  ItemEntity updateItem(UUID id, ItemEntity itemEntity);

}
