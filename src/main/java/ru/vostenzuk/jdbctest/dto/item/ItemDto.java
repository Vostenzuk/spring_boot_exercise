package ru.vostenzuk.jdbctest.dto.item;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemDto {

  private UUID id;
  private String type;
  private BigDecimal price;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Item{" + "id=" + id
        + ", type='" + type + '\''
        + ", price=" + price
        + '}';
  }
}
