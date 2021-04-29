package ru.vostenzuk.jdbctest.dto.item;

import com.sun.istack.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class CreateItemRequestDto extends ItemUpdateRequestDto {

  @NotNull
  private String type;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public BigDecimal getPrice() {
    return super.getPrice();
  }

  @Override
  public void setPrice(BigDecimal price) {
    super.setPrice(price);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    CreateItemRequestDto that = (CreateItemRequestDto) o;
    return type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, super.getPrice());
  }

  @Override
  public String toString() {
    return "CreateItemRequest{" +
        "type='" + type + '\'' +
        ", price=" + super.getPrice() +
        '}';
  }
}
