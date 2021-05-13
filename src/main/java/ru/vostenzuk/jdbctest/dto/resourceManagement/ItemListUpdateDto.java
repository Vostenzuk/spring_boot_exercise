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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final ItemListUpdateDto itemListUpdateDto;

    public Builder() {
      this.itemListUpdateDto = new ItemListUpdateDto();
    }

    public Builder addItems(Set<UUID> items) {
      this.itemListUpdateDto.setAddItems(items);
      return this;
    }

    public Builder removeItems(Set<UUID> items) {
      this.itemListUpdateDto.setRemoveItems(items);
      return this;
    }

    public ItemListUpdateDto build() {
      return this.itemListUpdateDto;
    }
  }
}
