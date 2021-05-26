package ru.vostenzuk.jdbctest.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;
import ru.vostenzuk.jdbctest.mapper.ItemMapper;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

  @Mock
  private ItemRepository repository;
  @Mock
  private ItemMapper mapper;
  private ItemService service;

  private final List<ItemDto> items = new ArrayList<>();
  private final List<ItemEntity> itemEntities = new ArrayList<>();
  private Set<UUID> ids;

  @BeforeEach
  public void setUp() {
    service = new ItemServiceImpl(repository, mapper);

    for (int i = 0; i < 3; i++) {
      items.add(ItemDto.builder().id(UUID.randomUUID()).price(BigDecimal.ONE).type("test").build());
      itemEntities.add(ItemEntity.builder().id(items.get(i).getId()).type(items.get(i).getType())
          .price(items.get(i).getPrice()).build());
    }
    ids = items.subList(0, 2).stream().map(ItemDto::getId).collect(Collectors.toSet());
  }

  @Test
  @DisplayName("Получаем все предметы из БД")
  public void givenItemsExist_whenGetAllItems_thenReturnAllItems() {
    when(repository.findAll()).thenReturn(itemEntities);
    when(mapper.fromEntity(any(ItemEntity.class)))
        .thenReturn(items.get(0), items.get(1), items.get(2));

    List<ItemDto> foundItems = service.getAllItems();
    assertThat(foundItems).isEqualTo(items);
  }

  @Test
  @DisplayName("Получаем все предметы по списку id")
  public void givenItemsExist_whenGetItemsByIds_thenReturnItems() {

    when(repository.findAllById(ids)).thenReturn(itemEntities.subList(0, 2));
    when(mapper.fromEntity(any(ItemEntity.class)))
        .thenReturn(items.get(0), items.get(1), items.get(2));

    Set<ItemDto> foundItems = service.getAllByIds(ids);
    assertThat(foundItems)
        .hasSize(2)
        .hasSameElementsAs(items.subList(0, 2));
  }

  @Test
  @DisplayName("Получаем предметы по списку несуществующих id")
  public void givenItemsDontExist_whenGetItemsByIds_thenThrowEntityNotFound() {
    UUID fakeId = UUID.randomUUID();
    ids.add(fakeId);
    Set<UUID> existingAndNonExistingIds = ids;

    when(repository.findAllById(existingAndNonExistingIds)).thenReturn(itemEntities.subList(0, 2));

    assertThatThrownBy(() -> service.getAllByIds(existingAndNonExistingIds))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Items with ids %s are not found!", Collections.singleton(fakeId));
  }

  @Test
  @DisplayName("Получаем данные о предмете по id")
  public void givenItemExist_whenGetItemById_thenReturnItem() {
    ItemDto item = items.get(0);

    when(repository.findById(item.getId())).thenReturn(Optional.of(itemEntities.get(0)));
    when(mapper.fromEntity(itemEntities.get(0))).thenReturn(item);
    ItemDto foundItem = service.getItem(item.getId());

    assertThat(foundItem)
        .isEqualTo(item);
  }

  @Test
  @DisplayName("Получаем данные несуществующего предмета")
  public void givenItemDoesntExist_whenGetItemById_thenThrowEntityNotFound() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getItem(id))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Item with id %s is not found", id);
  }

  @Test
  @DisplayName("Обновляем данные о предмете")
  public void givenItemExists_whenUpdateItem_thenUpdateAndReturnItem() {
    ItemUpdateRequestDto request = new ItemUpdateRequestDto();
    UUID id = items.get(0).getId();
    request.setPrice(BigDecimal.ONE);

    when(repository.findById(id)).thenReturn(Optional.of(itemEntities.get(0)));
    when(repository.save(itemEntities.get(0))).thenReturn(itemEntities.get(0));

    ItemDto itemDto = ItemDto.builder().id(itemEntities.get(0).getId())
        .type(itemEntities.get(0).getType()).price(request.getPrice()).build();

    when(mapper.fromEntity(itemEntities.get(0))).thenReturn(itemDto);

    ItemDto updatedItem = service.updateItem(id, request);

    assertThat(updatedItem)
        .isEqualTo(items.get(0));
  }

  @Test
  @DisplayName("Обновляем данные несуществующего предмета")
  public void givenItemDoesntExist_whenUpdateItem_thenThrowEntityNotFound() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.updateItem(id, new ItemUpdateRequestDto()))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Item with id %s is not found", id);
  }

  @Test
  @DisplayName("Создаём предмет")
  public void whenCreateItem_thenReturnCreatedItem() {
    ItemEntity itemEntity = itemEntities.get(0);
    CreateItemRequestDto request = CreateItemRequestDto.builder().type(itemEntity.getType())
        .price(itemEntity.getPrice()).build();

    when(mapper.toEntity(request)).thenReturn(itemEntity);
    when(repository.save(itemEntity)).thenReturn(itemEntity);
    when(mapper.fromEntity(itemEntity)).thenReturn(items.get(0));

    ItemDto createdItem = service.createItem(request);
    assertThat(createdItem)
        .isEqualTo(items.get(0));
  }
}