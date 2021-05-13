package ru.vostenzuk.jdbctest.dto.item;

import java.math.BigDecimal;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "Item{" + "id=" + id
        + ", type='" + type + '\''
        + ", price=" + price
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ItemDto itemDto = (ItemDto) o;

    return new EqualsBuilder().append(id, itemDto.id)
        .append(type, itemDto.type).append(price, itemDto.price).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(type).append(price).toHashCode();
  }

  public static class Builder {

    private final ItemDto item;

    public Builder() {
      this.item = new ItemDto();
    }

    public Builder id(UUID id) {
      this.item.setId(id);
      return this;
    }

    public Builder type(String type) {
      this.item.setType(type);
      return this;
    }

    public Builder price(BigDecimal price) {
      this.item.setPrice(price);
      return this;
    }

    public ItemDto build() {
      return this.item;
    }
  }
}
