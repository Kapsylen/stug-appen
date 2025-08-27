package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import stugapi.application.domain.model.Faktura;
import stugapi.application.domain.model.FakturaEnhet;
import stugapi.application.service.FakturaService;
import stugapi.infrastructure.entities.enums.FakturaStatus;
import stugapi.presentation.dto.FakturaDto;
import stugapi.presentation.dto.FakturaDto.FakturaDtoBuilder;
import stugapi.presentation.dto.FakturaEnhetDto;
import stugapi.presentation.dto.FakturaEnhetDto.FakturaEnhetDtoBuilder;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FakturaController.class)
class FakturaControllerTest {

  public static final String BASE_URL = "/api/v1/faktura";
  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private FakturaService fakturaService;

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  }

  @Test
  void whenPostFaktura_thenCreateFaktura() throws Exception {

    Instant issueDate = Instant.now();
    Instant duedate = Instant.now().plus(Period.ofDays(30));

    FakturaDto input = createFakturaDtoBuilder()
      .issueDate(issueDate)
      .dueDate(duedate)
      .build();

    Faktura output = Faktura.builder()
      .id(UUID.randomUUID().toString())
      .invoiceNumber(input.invoiceNumber())
      .clientName(input.clientName())
      .dueDate(input.dueDate())
      .items(List.of(
        FakturaEnhet.builder()
          .id(UUID.randomUUID().toString())
          .description(input.items().getFirst().description())
          .price(input.items().getFirst().price())
          .quantity(input.items().getFirst().quantity())
          .total(input.items().getFirst().total())
          .build()
      ))
      .totalAmount(input.totalAmount())
      .status(input.status() == null ? FakturaStatus.PAID : FakturaStatus.valueOf(input.status().toUpperCase()))
      .build();

    given(fakturaService.saveFaktura(input)).willReturn(output);

    mvc.perform(MockMvcRequestBuilders
      .post(BASE_URL)
      .content(mapper.writeValueAsString(input))
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(duedate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].price").value(1000.00))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].quantity").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].total").value(1000.00))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(1000.00));

    verify(fakturaService).saveFaktura(input);
  }



  @Test
  void whenDeleteFaktura_thenDeleteFaktura() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete(BASE_URL + "/12345678-1234-1234-1234-123456789012")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  void whenGetFaktura_thenReturnFaktura() throws Exception {
    var id = UUID.randomUUID().toString();
    Instant duedate = Instant.now().plus(Period.ofDays(30));
    var output = Faktura.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .dueDate(duedate)
      .totalAmount(1000.00)
      .status(FakturaStatus.PAID)
      .build();

    given(fakturaService.find(id)).willReturn(output);

    mvc.perform(MockMvcRequestBuilders
      .get(BASE_URL + "/{id}", id)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(duedate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(1000.00));

    verify(fakturaService).find(id);
  }

  @Test
  public void whenGetAllFaktura_thenReturnAllFaktura() throws Exception {
    Instant duedate = Instant.now().plus(Period.ofDays(30));
    Instant dueDate2 = Instant.now().plus(Period.ofDays(90));
    var faktura1 = Faktura.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .dueDate(duedate)
      .totalAmount(1000.00)
      .status(FakturaStatus.PAID)
      .build();

    var faktura2 = Faktura.builder()
      .invoiceNumber("Test invoice number2")
      .clientName("Test client name")
      .dueDate(dueDate2)
      .totalAmount(2000.00)
      .status(FakturaStatus.SENT)
      .build();

    var fakturor = List.of(faktura1, faktura2);

    given(fakturaService.findAll()).willReturn(fakturor);

    mvc.perform(MockMvcRequestBuilders
      .get(BASE_URL)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].issueDate").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].dueDate").value(duedate.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].invoiceNumber").value("Test invoice number2"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].issueDate").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].dueDate").value(dueDate2.toString()));

    verify(fakturaService).findAll();
  }

  @Test
  public void whenDeleteAllFaktura_thenNoContentIsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders
      .delete(BASE_URL + "/" + UUID.randomUUID())
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  public void whenPutFaktura_thenUpdateFaktura() throws Exception {
    Instant dueDate = Instant.now().plus(Period.ofDays(30));
    var id = UUID.randomUUID().toString();
    FakturaDto inputUpdateFaktura = FakturaDto.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .dueDate(dueDate)
      .items(List.of(
        createArendeEnhetBuilder()
          .build()
      ))
      .totalAmount(1000.00)
      .status("SENT")
      .build();

    Faktura outputUpdatedFaktura = Faktura.fromFakturaDto(inputUpdateFaktura)
      .id(id)
      .build();

    given(fakturaService.update(id, inputUpdateFaktura)).willReturn(outputUpdatedFaktura);

    mvc.perform(MockMvcRequestBuilders
      .put(BASE_URL + "/{id}", id)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString(inputUpdateFaktura)))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.invoiceNumber").value("Test invoice number"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.clientName").value("Test client name"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.issueDate").isNotEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.dueDate").value(outputUpdatedFaktura.dueDate().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalAmount").value(1000.00));

    verify(fakturaService).update(id, inputUpdateFaktura);
  }

  @Test
  public void whenGetById_nonExistingFaktura_thenReturn404() throws Exception {

    String nonExistingId = "non-existing-id";

    when(fakturaService.find(nonExistingId))
      .thenThrow(new EntityNotFoundException("Faktura not found with id: " + nonExistingId));

    mvc.perform(MockMvcRequestBuilders
      .get(BASE_URL + "/{id}", nonExistingId))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value("404"))
      .andExpect(jsonPath("$.error").value("Not Found"))
      .andExpect(jsonPath("$.message").value("Resource not found"))
      .andExpect(jsonPath("$.details").value("Faktura not found with id: " + nonExistingId))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidClientNameCases")
  void whenCreate_andClientNameIsNullOrBlankOrNotBetween3And100Characters_thenReturn400(String clientName, String expectedError) throws Exception {

    // Given
    FakturaDto invalidInput = createFakturaDtoBuilder()
      .clientName(clientName)
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

  private static Stream<Arguments> invalidClientNameCases() {
    return Stream.of(
      Arguments.of(
        null,
        "clientName: Client name is required"
      ),
      Arguments.of(
        "",
        "clientName: Client name must be between 2 and 100 characters"
      ),
      Arguments.of(
        " ",
        "clientName: Client name must be between 2 and 100 characters"
      ),
      Arguments.of(
        "a",
        "clientName: Client name must be between 2 and 100 characters"
      ),
      Arguments.of(
        "a" .repeat(101),
        "clientName: Client name must be between 2 and 100 characters"
      )
    );
  }

  @Test
  void whenCreate_dueDateIsNull_thenReturn400() throws Exception {
    // Given
    FakturaDto invalidInput = createFakturaDtoBuilder()
      .dueDate(null)
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
      .andExpect(jsonPath("$.details").value("dueDate: Due date is required"))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidFakturaEnhetDescriptionCases")
  void whenCreate_itemsDescriptionIsNullOrEmptyOrNotMatchValidString_thenReturn400(String description, String expectedError) throws Exception {

    // Given
    FakturaDto invalidInput = createFakturaDtoBuilder()
      .items(List.of(
        createArendeEnhetBuilder()
          .description(description)
          .build()
      )).build();

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

    verify(fakturaService, times(0)).saveFaktura(invalidInput);
  }

  private static Stream<Arguments> invalidFakturaEnhetDescriptionCases() {
    return Stream.of(
      Arguments.of(
        null,
        "items[0].description: Description is required"
      ),
      Arguments.of(
        " ",
        "items[0].description: Description is required"
      ),
      Arguments.of(
        "a".repeat(256),
        "items[0].description: Description must be between 1 and 255 characters"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidFakturaEnhetQuantityCases")
  void whenCreate_andFakturaIsNullOrEmptyOrNotMatchValidString_thenReturn400(Integer quantity, String expectedError) throws Exception {

    // Given
    FakturaDto invalidInput = createFakturaDtoBuilder()
      .items(List.of(
        createArendeEnhetBuilder()
          .quantity(quantity)
          .build()
      )).build();

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

    verify(fakturaService, times(0)).saveFaktura(invalidInput);
  }

  private static Stream<Arguments> invalidFakturaEnhetQuantityCases() {
    return Stream.of(
      Arguments.of(null, "items[0].quantity: Quantity is required"),
      Arguments.of(0, "items[0].quantity: Quantity must be greater than 0"),
      Arguments.of(-1, "items[0].quantity: Quantity must be greater than 0")
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidFakturaEnhetPriceCases")
  void whenCreate_andFakturaIsNullOrEmptyOrNotMatchValidPrice_thenReturn400(Double price, String expectedError) throws Exception {
    // Given
    FakturaDto invalidInput = createFakturaDtoBuilder()
      .items(List.of(
        createArendeEnhetBuilder()
          .price(price)
          .build()
      )).build();

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

  private static Stream<Arguments> invalidFakturaEnhetPriceCases() {
    return Stream.of(
      Arguments.of(null, "items[0].price: Price is required"),
      Arguments.of(-1.0, "items[0].price: Price must be greater than or equal to 0"),
      Arguments.of(-100.0, "items[0].price: Price must be greater than or equal to 0")
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidTypeCases")
  void whenCreate_andStatusNullOrEmptyOrNotMatchValidString_thenReturn400(String status, String expectedError) throws Exception {

    // Given
    FakturaDto invalidInput = createFakturaDtoBuilder()
      .status(status)
      .items(List.of(
        createArendeEnhetBuilder()
          .build()
      )).build();

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

    verify(fakturaService, times(0)).saveFaktura(invalidInput);
  }

  private static Stream<Arguments> invalidTypeCases() {
    return Stream.of(
      Arguments.of(
        null,
        "status: Status is required"
      ),
      Arguments.of(
        " ",
        "status: Status must be either PAID, OVERDUE, SENT or DRAFT"
      ),
      Arguments.of(
        "",
        "status: Status must be either PAID, OVERDUE, SENT or DRAFT"
      ),
      Arguments.of(
        "a",
        "status: Status must be either PAID, OVERDUE, SENT or DRAFT"
      )
    );
  }

  private static FakturaDtoBuilder createFakturaDtoBuilder() {
    return FakturaDto.builder()
      .invoiceNumber("Test invoice number")
      .clientName("Test client name")
      .issueDate(Instant.now())
      .dueDate(Instant.now())
      .items(List.of(
        createArendeEnhetBuilder().build()
      ))
      .totalAmount(1000.00)
      .status(FakturaStatus.SENT.name());
  }

  private static FakturaEnhetDtoBuilder createArendeEnhetBuilder() {
    return FakturaEnhetDto
      .builder()
      .description("Test description")
      .price(1000.00)
      .quantity(1)
      .total(1000.00);
  }
}
