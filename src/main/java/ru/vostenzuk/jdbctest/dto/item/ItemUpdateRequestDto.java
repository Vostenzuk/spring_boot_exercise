package ru.vostenzuk.jdbctest.dto.item;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemUpdateRequestDto {

  private BigDecimal price;

  public ItemUpdateRequestDto() {
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    ItemUpdateRequestDto that = (ItemUpdateRequestDto) o;
    return Objects.equals(price, that.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(price);
  }

  @Override
  public String toString() {
    return "ItemUpdateRequest{" +
        "price=" + price +
        '}';
  }
}
