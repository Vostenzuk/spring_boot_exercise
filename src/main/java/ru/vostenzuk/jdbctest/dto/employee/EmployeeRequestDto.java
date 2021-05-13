package ru.vostenzuk.jdbctest.dto.employee;

import com.sun.istack.NotNull;

public class EmployeeRequestDto {

  @NotNull
  private String firstName;
  @NotNull
  private String lastName;
  @NotNull
  private String position;

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

  public static Builder builder() {
    return new Builder();
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

    private final EmployeeRequestDto employeeRequestDto;

    public Builder() {
      this.employeeRequestDto = new EmployeeRequestDto();
    }

    public Builder firstName(String firstName) {
      this.employeeRequestDto.setFirstName(firstName);
      return this;
    }

    public Builder lastName(String lastName) {
      this.employeeRequestDto.setLastName(lastName);
      return this;
    }

    public Builder position(String position) {
      this.employeeRequestDto.setPosition(position);
      return this;
    }

    public EmployeeRequestDto build() {
      return this.employeeRequestDto;
    }
  }
}
