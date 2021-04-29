package ru.vostenzuk.jdbctest.dto.employee;

import com.sun.istack.NotNull;

public class EmployeeRequestDto {

  @NotNull
  private String firstName;
  @NotNull
  private String lastName;
  @NotNull
  private String position;

  private EmployeeRequestDto(Builder builder) {
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.position = builder.position;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "CreateEmployeeRequest{" +
        "firstName='" + firstName + '\'' +
        ", lastname='" + lastName + '\'' +
        ", position='" + position + '\'' +
        '}';
  }

  public static class Builder {

    private String firstName;
    private String lastName;
    private String position;

    public Builder() {
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder position(String position) {
      this.position = position;
      return this;
    }

    public EmployeeRequestDto build() {
      return new EmployeeRequestDto(this);
    }
  }
}
