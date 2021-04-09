package ru.vostenzuk.jdbctest.dto;

import ru.vostenzuk.jdbctest.domain.ItemEntity;

import java.util.Set;
import java.util.UUID;

public class UpdateEmployeeRequest extends CreateEmployeeRequest {

    private Set<UUID> addItems;
    private Set<UUID> removeItems;

    public Set<UUID> getAddItems() {
        return addItems;
    }

    public Set<UUID> getRemoveItems() {
        return removeItems;
    }
}
