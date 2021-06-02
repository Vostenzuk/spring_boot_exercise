package ru.vostenzuk.jdbctest.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ItemMapper {

    ItemDto fromEntity(ItemEntity itemEntity);

    ItemEntity toEntity(CreateItemRequestDto request);
}
