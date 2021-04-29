package ru.vostenzuk.jdbctest.dto.resourceManagement;

import java.util.Set;
import java.util.UUID;

public class ItemListUpdateDto {

  private Set<UUID> addItems;
  private Set<UUID> removeItems;

  public Set<UUID> getAddItems() {
    return addItems;
  }

  public void setAddItems(Set<UUID> addItems) {
    this.addItems = addItems;
  }

  public Set<UUID> getRemoveItems() {
    return removeItems;
  }

  public void setRemoveItems(Set<UUID> removeItems) {
    this.removeItems = removeItems;
  }
}
