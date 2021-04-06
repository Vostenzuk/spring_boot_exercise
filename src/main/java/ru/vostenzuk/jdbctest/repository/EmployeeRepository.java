package ru.vostenzuk.jdbctest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

    boolean existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(String lastName, String firstName, String position);

}
