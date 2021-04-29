package ru.vostenzuk.jdbctest.dto.employee;

import java.util.Set;
import java.util.UUID;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;

public class EmployeeDto {

  private UUID id;
  private String firstName;
  private String lastName;
  private String position;

  private Set<ItemDto> items;

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

  @Override
  public String toString() {
    return "Employee{" + "id=" + id
        + ", firstName='" + firstName + '\''
        + ", lastName='" + lastName + '\''
        + ", position='" + position + '\''
        + ", items=" + items
        + '}';
  }
}
