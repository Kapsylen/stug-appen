package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.method.P;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import stugapi.application.domain.model.Utlagg;
import stugapi.application.service.UtlaggService;
import stugapi.presentation.dto.UtlaggDto;
import stugapi.presentation.dto.UtlaggDto.UtlaggDtoBuilder;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UtlaggController.class)
public class UtlaggControllerTest {

  public static final String BASE_URL = "/api/v1/utlagg";
  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private UtlaggService utlaggService;

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  }

  @Test
  public void whenPostUtlagg_thenCreateUtlagg() throws Exception {

    Instant outlayDate = Instant.now();
    UtlaggDto inputUtlagg = createUtlaggDtoBuilder()
      .outlayDate(outlayDate)
      .build();

    given(utlaggService.saveUtlagg(inputUtlagg)).willReturn(Utlagg.fromUtlaggDto(inputUtlagg).build());

    mvc.perform(MockMvcRequestBuilders
        .post(BASE_URL)
        .content(mapper.writeValueAsString(inputUtlagg))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(inputUtlagg.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("description").value(inputUtlagg.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value(inputUtlagg.price()));

    verify(utlaggService).saveUtlagg(inputUtlagg);
  }


  @Test
  public void whenDeleteUtlagg_thenDeleteUtlagg() throws Exception {
    mvc.perform(MockMvcRequestBuilders
        .delete(BASE_URL + "/" + UUID.randomUUID())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status()
        .isNoContent());
  }

  @Test
  public void whenGetUtlagg_thenReturnUtlagg() throws Exception {
    Instant outlayDate = Instant.now();
    String id = UUID.randomUUID().toString();
    UtlaggDto inputUtlagg = createUtlaggDtoBuilder()
      .id(id)
      .outlayDate(outlayDate)
      .build();

    given(utlaggService.find(any())).willReturn(Utlagg.fromUtlaggDto(inputUtlagg).build());

    mvc.perform(MockMvcRequestBuilders
        .get(BASE_URL + "/{id}", id)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(inputUtlagg.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("description").value(inputUtlagg.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value(inputUtlagg.price()));
  }

  @Test
  public void whenGetAllUtlagg_thenReturnAllUtlagg() throws Exception {
    Instant outlayDate = Instant.now();
    Utlagg utlagg1 = Utlagg.fromUtlaggDto(createUtlaggDtoBuilder()
      .outlayDate(outlayDate)
      .build()).build();

    Instant outlayDate2 = Instant.now().plus(Period.ofDays(10));
    Utlagg utlagg2 =
      Utlagg.fromUtlaggDto(createUtlaggDtoBuilder()
          .title("Test title 2")
          .description("Test description 2")
          .outlayDate(outlayDate2)
          .price(2000.00).build())
        .build();

    List<Utlagg> utlagg = List.of(utlagg1, utlagg2);

    given(utlaggService.findAll()).willReturn(utlagg);

    mvc.perform(MockMvcRequestBuilders
        .get(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(utlagg1.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(utlagg1.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(utlagg1.price()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(utlagg2.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(utlagg2.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].outlayDate").value(outlayDate2.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(utlagg2.price()));

    verify(utlaggService).findAll();
  }

  @Test
  public void whenDeleteAllUtlagg_thenNoContentIsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders
        .delete(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status()
        .isNoContent());

    verify(utlaggService).deleteAll();
  }

  @Test
  public void whenPutUtlagg_thenUpdateUtlagg() throws Exception {
    Instant outlayDate = Instant.now();
    Double price = 2000.00;
    String id = UUID.randomUUID().toString();
    UtlaggDto inputUtlagg = createUtlaggDtoBuilder().build();

    Utlagg outputUtlagg = Utlagg.fromUtlaggDto(inputUtlagg)
      .id(id)
      .outlayDate(outlayDate)
      .price(price)
      .build();

    given(utlaggService.update(id, inputUtlagg)).willReturn(outputUtlagg);

    mvc.perform(MockMvcRequestBuilders
        .put(BASE_URL + "/{id}", id)
        .content(mapper.writeValueAsBytes(inputUtlagg))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(outputUtlagg.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("description").value(outputUtlagg.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("outlayDate").value(outlayDate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("price").value(outputUtlagg.price()));

    verify(utlaggService).update(id, inputUtlagg);
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidTitleCases")
  public void whenPostUtlagg_thenInvalidTitle(String title, String expectedError) throws Exception {
    // Given

    UtlaggDto invalidInput = createUtlaggDtoBuilder()
      .title(title)
      .build();

    // When & Then
    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private static Stream<Arguments> invalidTitleCases() {
    return Stream.of(
      Arguments.of(null, "title: Title is required"),
      Arguments.of("", "title: Title must be between 2 and 100 characters"),
      Arguments.of(" ", "title: Title must be between 2 and 100 characters"),
      Arguments.of("a".repeat(256), "title: Title must be between 2 and 100 characters")
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidDescriptionCases")
  public void whenPostUtlagg_thenInvalidDescription(String description, String expectedError) throws Exception {
    // Given

    UtlaggDto invalidInput = createUtlaggDtoBuilder()
      .description(description)
      .build();

    // When & Then
    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private static Stream<Arguments> invalidDescriptionCases() {
    return Stream.of(
      Arguments.of(null, "description: Description is required"),
      Arguments.of("", "description: Description must be between 2 and 1000 characters"),
      Arguments.of(" ", "description: Description must be between 2 and 1000 characters"),
      Arguments.of("a".repeat(1001), "description: Description must be between 2 and 1000 characters")
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidOutlayDateCases")
  public void whenPostUtlagg_thenInvalidOutlayDate(Instant outlayDate, String expectedError) throws Exception {
    // Given

    UtlaggDto invalidInput = createUtlaggDtoBuilder()
      .outlayDate(outlayDate)
      .build();

    // When & Then
    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private static Stream<Arguments> invalidOutlayDateCases() {
    return Stream.of(
      Arguments.of(null, "outlayDate: Outlay date is required"),
      Arguments.of(Instant.now().plus(Period.ofDays(1)), "outlayDate: Outlay date cannot be in the future")
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPriceCases")
  public void whenPostUtlagg_thenInvalidPrice(Double price, String expectedError) throws Exception {
    // Given

    UtlaggDto invalidInput = createUtlaggDtoBuilder()
      .price(price)
      .build();

    // When & Then
    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(invalidInput)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.details").value(expectedError))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private static Stream<Arguments> invalidPriceCases() {
    return Stream.of(
      Arguments.of(null, "price: Price is required"),
      Arguments.of(-1.00, "price: Price must be greater than or equal to 0"),
      Arguments.of(-100.00, "price: Price must be greater than or equal to 0")
    );
  }

  private static UtlaggDtoBuilder createUtlaggDtoBuilder() {
    return UtlaggDto.builder()
      .title("Test title")
      .description("Test description")
      .outlayDate(Instant.now())
      .price(1000.00);
  }
}
