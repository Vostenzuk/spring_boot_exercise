package ru.vostenzuk.jdbctest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {

  boolean existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(String lastName, String firstName,
      String position);

  @Query(value = "select * from employee where id = (select ei.employee_id from employee_item ei where item_id = :itemId)",
      nativeQuery = true)
  Optional<EmployeeEntity> findEmployeeEntityByItemId(@Param("itemId") String itemId);

}
