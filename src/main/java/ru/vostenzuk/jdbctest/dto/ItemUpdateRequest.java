package ru.vostenzuk.jdbctest.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemUpdateRequest {

    private BigDecimal price;

    public ItemUpdateRequest() {
    }

    public ItemUpdateRequest(BigDecimal price) {
        this.price = price;
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
        ItemUpdateRequest that = (ItemUpdateRequest) o;
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
