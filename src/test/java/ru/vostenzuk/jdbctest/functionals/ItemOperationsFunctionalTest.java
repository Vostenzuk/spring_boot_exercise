package ru.vostenzuk.jdbctest.functionals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.IsEqual.equalTo;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.web.servlet.MvcResult;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

public class ItemOperationsFunctionalTest extends AbstractRestFunctionalTest {

  @Autowired
  private ItemRepository itemRepository;

  public static final String BASE_URL = "/items";

  @Test
  @DisplayName("POSITIVE: Создаём предмет")
  public void createItemSuccess() throws Exception {
    CreateItemRequestDto request = easyRandom.nextObject(CreateItemRequestDto.class);

    MvcResult result = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    ItemDto response = mapper.readValue(result.getResponse().getContentAsString(), ItemDto.class);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response).hasFieldOrPropertyWithValue("type", request.getType())
        .hasFieldOrPropertyWithValue("price", request.getPrice());
    assertThat(itemRepository.findById(response.getId())).isPresent();
  }

  @Test
  @DisplayName("POSITIVE: Получаем все предметы")
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  public void getAllItemsSuccess() throws Exception {
    List<ItemEntity> itemEntities = itemRepository
        .saveAll(easyRandom.objects(ItemEntity.class, 3)
            .collect(Collectors.toList()));

    MvcResult result = mockMvc.perform(get(BASE_URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    List<ItemDto> foundItems = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<ItemDto>>() {
        });

    assertThat(foundItems).hasSize(itemEntities.size());
  }

  @Test
  @DisplayName("POSITIVE: Получаем данные о предмете по id")
  public void getItemByIdSuccess() throws Exception {

    ItemEntity itemEntity = itemRepository.save(easyRandom.nextObject(ItemEntity.class));

    mockMvc.perform(get(BASE_URL + "/" + itemEntity.getId()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(itemEntity.getId().toString())));
  }

  @Test
  @DisplayName("NEGATIVE: Получаем данные по несуществующему id")
  public void getItemByFalseIdNegative() throws Exception {
    UUID id = UUID.randomUUID();

    mockMvc.perform(get(BASE_URL + "/" + id)).
        andExpect(status().
            isNotFound())
        .andExpect(jsonPath("$.errorMessage", equalTo("Item with id " + id + " is not found")));
  }

  @Test
  @DisplayName("POSITIVE: Обновляем стоимость предмета")
  public void updateItemSuccess() throws Exception {

    ItemEntity itemEntity = itemRepository.save(easyRandom.nextObject(ItemEntity.class));

    ItemUpdateRequestDto request = new ItemUpdateRequestDto();
    request.setPrice(BigDecimal.TEN);

    mockMvc.perform(patch(BASE_URL + "/" + itemEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.id", equalTo(itemEntity.getId().toString())))
        .andExpect(jsonPath("$.type", equalTo(itemEntity.getType())))
        .andExpect(jsonPath("$.price", equalTo(request.getPrice()), BigDecimal.class));
  }
}

