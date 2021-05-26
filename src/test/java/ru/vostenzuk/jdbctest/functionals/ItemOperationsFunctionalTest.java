package ru.vostenzuk.jdbctest.functionals;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

public class ItemOperationsFunctionalTest extends AbstractRestFunctionalTest {

  @Autowired
  private ItemRepository itemRepository;

  public static final String BASE_URL = "/items";

  @Test
  @DisplayName("POSITIVE: Создаём предмет")
  public void whenCreateItem_thenReturnCreatedItem() throws Exception {
    CreateItemRequestDto request = easyRandom.nextObject(CreateItemRequestDto.class);

    mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.type").value(request.getType()))
        .andExpect(jsonPath("$.price").value(request.getPrice()));
  }

  @Test
  @DisplayName("POSITIVE: Получаем все предметы")
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  public void givenItemsExist_whenGetAllItems_thenReturnAllItems() throws Exception {
    List<ItemEntity> itemEntities = itemRepository
        .saveAll(
            easyRandom.objects(ItemEntity.class, 3)
                .peek(i -> i.setPrice(BigDecimal.TEN))
                .collect(Collectors.toList()));

    mockMvc.perform(get(BASE_URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(itemEntities.size())))
        .andExpect(jsonPath("$[*].id").value(containsInAnyOrder(
            itemEntities.stream().map(i -> i.getId().toString()).toArray())))
        .andExpect(jsonPath("$[*].type").value(containsInAnyOrder(
            itemEntities.stream().map(ItemEntity::getType).toArray())))
        .andExpect(jsonPath("$[*].price").value(containsInAnyOrder(
            itemEntities.stream().map(i -> i.getPrice().doubleValue()).toArray()
        )));
  }

  @Test
  @DisplayName("POSITIVE: Получаем данные о предмете по id")
  public void givenItemExists_whenGetItemById_thenReturnItem() throws Exception {

    ItemEntity itemToSave = easyRandom.nextObject(ItemEntity.class);
    itemToSave.setPrice(BigDecimal.TEN);
    ItemEntity itemEntity = itemRepository.save(itemToSave);

    mockMvc.perform(get(BASE_URL + "/" + itemEntity.getId()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(itemEntity.getId().toString()))
        .andExpect(jsonPath("$.type").value(itemEntity.getType()))
        .andExpect(jsonPath("$.price")
            .value(itemEntity.getPrice().doubleValue()));
  }

  @Test
  @DisplayName("NEGATIVE: Получаем данные по несуществующему id")
  public void givenItemDoesntExist_whenGetItemById_thenReturnNotFound() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc.perform(get(BASE_URL + "/" + id)).
        andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage")
            .value("Item with id " + id + " is not found"));
  }

  @Test
  @DisplayName("POSITIVE: Обновляем стоимость предмета")
  public void givenItemExists_whenUpdateItemPrice_thenUpdateAndReturnItem() throws Exception {

    ItemEntity itemToSave = easyRandom.nextObject(ItemEntity.class);
    itemToSave.setPrice(BigDecimal.ONE);

    ItemEntity savedItem = itemRepository.save(itemToSave);

    ItemUpdateRequestDto request = new ItemUpdateRequestDto();
    request.setPrice(BigDecimal.TEN);

    mockMvc.perform(patch(BASE_URL + "/" + savedItem.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.id").value(savedItem.getId().toString()))
        .andExpect(jsonPath("$.type").value(savedItem.getType()))
        .andExpect(jsonPath("$.price").value(request.getPrice()));
  }
}

