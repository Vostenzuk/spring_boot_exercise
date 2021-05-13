package ru.vostenzuk.jdbctest.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NamedEntityGraph(
    name = "employee_items_graph",
    attributeNodes = {
        @NamedAttributeNode("items")
    }
)
@Entity(name = "employee")
public class EmployeeEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @ColumnDefault("random_uuid()")
  @Type(type = "uuid-char")
  private UUID id;
  private String firstName;
  private String lastName;
  private String position;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "employee_item",
      joinColumns = {
          @JoinColumn(name = "employee_id", referencedColumnName = "id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "item_id", referencedColumnName = "id")
      }
  )
  private Set<ItemEntity> items = new HashSet<>();

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

  public Set<ItemEntity> getItems() {
    return items;
  }

  public void setItems(Set<ItemEntity> items) {
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
    EmployeeEntity that = (EmployeeEntity) o;
    return id.equals(that.id) && firstName.equals(that.firstName) && lastName.equals(that.lastName)
        && position.equals(that.position) && items.equals(that.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, position, items);
  }

  @Override
  public String toString() {
    return "Employee{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", position='" + position + '\'' +
        ", items=" + items +
        '}';
  }

  public static class Builder {

    private final EmployeeEntity employeeEntity;

    public Builder() {
      employeeEntity = new EmployeeEntity();
    }

    public Builder firstName(String firstName) {
      employeeEntity.setFirstName(firstName);
      return this;
    }

    public Builder lastName(String lastName) {
      employeeEntity.setLastName(lastName);
      return this;
    }

    public Builder position(String position) {
      employeeEntity.setPosition(position);
      return this;
    }

    public Builder id(UUID id) {
      employeeEntity.setId(id);
      return this;
    }

    public Builder items(Set<ItemEntity> items) {
      employeeEntity.setItems(items);
      return this;
    }

    public EmployeeEntity build() {
      return this.employeeEntity;
    }
  }
}
