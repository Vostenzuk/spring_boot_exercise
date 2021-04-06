package ru.vostenzuk.jdbctest.dto;

import com.sun.istack.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

public class CreateItemRequest {

    @NotNull
    private String type;
    @NotNull
    private BigDecimal price;

    public CreateItemRequest() {
    }

    public CreateItemRequest(String type, BigDecimal price) {
        this.type = type;
        this.price = price;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateItemRequest that = (CreateItemRequest) o;
        return type.equals(that.type) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, price);
    }

    @Override
    public String toString() {
        return "CreateItemRequest{" +
                "type='" + type + '\'' +
                ", price=" + price +
                '}';
    }
}
