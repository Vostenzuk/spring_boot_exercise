package ru.vostenzuk.jdbctest.dto.employee;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;

public class EmployeeDto {

  private UUID id;
  private String firstName;
  private String lastName;
  private String position;

  private Set<ItemDto> items = new HashSet<>();

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public Set<ItemDto> getItems() {
    return items;
  }

  public void setItems(Set<ItemDto> items) {
    this.items = items;
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

    EmployeeDto that = (EmployeeDto) o;

    return new EqualsBuilder().append(id, that.id)
        .append(firstName, that.firstName).append(lastName, that.lastName)
        .append(position, that.position).append(items, that.items).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(firstName).append(lastName)
        .append(position)
        .append(items).toHashCode();
  }

  @Override
  public String toString() {
    return "Employee{" + "id=" + id
        + ", firstName='" + firstName + '\''
        + ", lastName='" + lastName + '\''
        + ", position='" + position + '\''
        + ", items=" + items
        + '}';
  }

  public static class Builder {

    private final EmployeeDto employeeDto;

    public Builder() {
      this.employeeDto = new EmployeeDto();
    }

    public Builder id(UUID id) {
      this.employeeDto.setId(id);
      return this;
    }

    public Builder firstName(String firstName) {
      this.employeeDto.setFirstName(firstName);
      return this;
    }

    public Builder lastName(String lastName) {
      this.employeeDto.setLastName(lastName);
      return this;
    }

    public Builder position(String position) {
      this.employeeDto.setPosition(position);
      return this;
    }

    public Builder items(Set<ItemDto> items) {
      this.employeeDto.setItems(items);
      return this;
    }

    public EmployeeDto build() {
      return this.employeeDto;
    }
  }
}
