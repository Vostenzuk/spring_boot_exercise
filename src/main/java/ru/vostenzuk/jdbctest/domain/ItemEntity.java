package ru.vostenzuk.jdbctest.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "item")
public class ItemEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @ColumnDefault("random_uuid()")
  @Type(type = "uuid-char")
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
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    ItemEntity item = (ItemEntity) o;
    return Objects.equals(id, item.id) && Objects.equals(type, item.type) && Objects
        .equals(price, item.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, price);
  }

  @Override
  public String toString() {
    return "Item{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", price=" + price +
        '}';
  }

  public static class Builder {
    private final ItemEntity itemEntity;

    public Builder() {
      this.itemEntity = new ItemEntity();
    }

    public Builder id(UUID id) {
      this.itemEntity.setId(id);
      return this;
    }

    public Builder type(String type) {
      this.itemEntity.setType(type);
      return this;
    }

    public Builder price(BigDecimal price) {
      this.itemEntity.setPrice(price);
      return this;
    }

    public ItemEntity build() {
      return this.itemEntity;
    }
  }
}
