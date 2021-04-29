package ru.vostenzuk.jdbctest.dto.resourceManagement;

import java.math.BigDecimal;

public class ExpenseDto {

  private BigDecimal amount;

  public ExpenseDto(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
