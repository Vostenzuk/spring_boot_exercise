package ru.vostenzuk.jdbctest.dto;

import java.util.Set;
import java.util.UUID;

public class UpdateEmployeeRequest extends CreateEmployeeRequest {

    private final Set<UUID> addItems;
    private final Set<UUID> removeItems;

    private UpdateEmployeeRequest(Builder builder) {
        super(builder.firstName, builder.lastName, builder.position);
        this.removeItems = builder.removeItems;
        this.addItems = builder.addItems;
    }

    private UpdateEmployeeRequest(String firstName, String lastName, String position, Set<UUID> addItems, Set<UUID> removeItems) {
        super(firstName, lastName, position);
        this.addItems = addItems;
        this.removeItems = removeItems;
    }

    public Set<UUID> getAddItems() {
        return addItems;
    }

    public Set<UUID> getRemoveItems() {
        return removeItems;
    }

    public static class Builder {
        private Set<UUID> addItems;
        private Set<UUID> removeItems;
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

        public Builder addItems(Set<UUID> addItems) {
            this.addItems = addItems;
            return this;
        }

        public Builder removeItems(Set<UUID> removeItems) {
            this.removeItems = removeItems;
            return this;
        }

        public UpdateEmployeeRequest build() {
            return new UpdateEmployeeRequest(this);
        }
    }
}
