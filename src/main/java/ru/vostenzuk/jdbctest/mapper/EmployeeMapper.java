package ru.vostenzuk.jdbctest.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT
)
public interface EmployeeMapper {

    EmployeeEntity fromRequest(EmployeeRequestDto request);

    EmployeeDto fromEntity(EmployeeEntity employeeEntity);
}
