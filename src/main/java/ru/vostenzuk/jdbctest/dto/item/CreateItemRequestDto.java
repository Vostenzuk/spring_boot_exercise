package ru.vostenzuk.jdbctest.dto.item;

import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;

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
  public void setPrice(BigDecimal price) {
    super.setPrice(price);
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

  public static class Builder {

    private final CreateItemRequestDto requestDto;

    public Builder() {
      requestDto = new CreateItemRequestDto();
    }

    public Builder type(String type) {
      this.requestDto.setType(type);
      return this;
    }

    public Builder price(BigDecimal price) {
      this.requestDto.setPrice(price);
      return this;
    }

    public CreateItemRequestDto build() {
      return this.requestDto;
    }
  }
}
