package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AssertionFailureBuilder;
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
import stugapi.application.domain.model.Arende;
import stugapi.application.domain.model.ArendeStatus;
import stugapi.application.service.ArendeService;
import stugapi.infrastructure.entities.enums.Prioritet;
import stugapi.infrastructure.entities.enums.Status;
import stugapi.infrastructure.entities.enums.Typ;
import stugapi.presentation.dto.ArendeDto;
import stugapi.presentation.dto.ArendeDto.ArendeDtoBuilder;
import stugapi.presentation.dto.ArendeStatusDto;
import stugapi.presentation.dto.ArendeStatusDto.ArendeStatusDtoBuilder;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArendeController.class)
public class ArendeControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private ArendeService arendeService;

  private ObjectMapper mapper;

  private static final String BASE_URL = "/api/v1/arende";

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  }


  @Test
  public void whenPostArende_thenCreateArende() throws Exception {

    Instant createdAt = Instant.now();
    Instant updatedAt = Instant.now();
    Instant resolvedTime = Instant.now().plus(Period.ofDays(7));

    ArendeDto input = ArendeDto.builder()
      .title("Water Leak in Bathroom")
      .description("Water leaking from pipe under sink, causing floor damage")
      .type(Typ.DAMAGE.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.NEW.name())
      .reportedBy("Lars Andersson")
      .assignedTo("Plumber AB")
      .location("Main Bathroom")
      .estimatedCost(5000.0)
      .startTime(createdAt)
      .resolvedTime(resolvedTime)
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("Plumber AB, Tel: 070-123-4567")
      .updates(List.of(
        ArendeStatusDto.builder()
          .timestamp(createdAt)
          .message(Status.INVESTIGATING.name())
          .updatedBy("Lars Andersson")
          .status(Status.IN_PROGRESS.name())
          .build(),
        ArendeStatusDto.builder()
          .timestamp(resolvedTime)
          .message(Status.CLOSED.name())
          .updatedBy("Lars Andersson")
          .status(Status.RESOLVED.name())
          .build()))
      .tags(List.of("plumbing", "water-damage", "bathroom"))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    Arende output = Arende.builder()
      .title(input.title())
      .description(input.description())
      .type(Typ.valueOf(input.type()))
      .priority(Prioritet.valueOf(input.priority()))
      .status(Status.valueOf(input.status()))
      .reportedBy(input.reportedBy())
      .assignedTo(input.assignedTo())
      .location(input.location())
      .estimatedCost(input.estimatedCost())
      .startTime(input.startTime())
      .resolvedTime(input.resolvedTime())
      .resolution(input.resolution())
      .requiresContractor(true)
      .contractorInfo(input.contractorInfo())
      .updates(input.updates().stream().map(ArendeStatus::toArendeStatus).toList())
      .tags(input.tags())
      .createdAt(input.createdAt())
      .updatedAt(input.updatedAt())
      .build();

    given(arendeService.saveArende(input)).willReturn(output);

    mvc.perform(post(BASE_URL)
        .content(mapper.writeValueAsString(input))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.title").value(input.title()))
      .andExpect(jsonPath("$.description").value(input.description()))
      .andExpect(jsonPath("$.type").value(input.type()))
      .andExpect(jsonPath("$.priority").value(input.priority()))
      .andExpect(jsonPath("$.status").value(input.status()))
      .andExpect(jsonPath("$.reportedBy").value(input.reportedBy()))
      .andExpect(jsonPath("$.assignedTo").value(input.assignedTo()))
      .andExpect(jsonPath("$.location").value(input.location()))
      .andExpect(jsonPath("$.estimatedCost").value(input.estimatedCost()))
      .andExpect(jsonPath("$.startTime").value(input.startTime().toString()))
      .andExpect(jsonPath("$.resolvedTime").value(input.resolvedTime().toString()))
      .andExpect(jsonPath("$.resolution").value(input.resolution()))
      .andExpect(jsonPath("$.requiresContractor").value(input.requiresContractor()))
      .andExpect(jsonPath("$.contractorInfo").value(input.contractorInfo()))
      .andExpect(jsonPath("$.updates[0].status").value(input.updates().getFirst().status()))
      .andExpect(jsonPath("$.updates[1].status").value(input.updates().getLast().status()))
      .andExpect(jsonPath("$.tags[0]").value(input.tags().getFirst()))
      .andExpect(jsonPath("$.tags[1]").value(input.tags().get(1)))
      .andExpect(jsonPath("$.tags[2]").value(input.tags().getLast()))
      .andExpect(jsonPath("$.createdAt").value(input.createdAt().toString()))
      .andExpect(jsonPath("$.updatedAt").value(input.updatedAt().toString()));

    verify(arendeService, times(1)).saveArende(input);

  }

  @Test
  public void whenDeleteArende_thenDeleteArende() throws Exception {
    mvc.perform(delete(BASE_URL + "/" + UUID.randomUUID())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status()
        .isNoContent());
  }

  @Test
  public void whenPutArende_thenUpdateArende() throws Exception {
    String id = UUID.randomUUID().toString();
    Instant createdAt = Instant.now();
    Instant updatedAt = Instant.now();
    Instant lastUpdatedAt = Instant.now();
    Instant resolvedTime = Instant.now().plus(Period.ofDays(7));

    ArendeDto inputUpdateArende = ArendeDto.builder()
      .title("Heating System Failure")
      .description("No heat output from radiators, temperature dropping")
      .type(Typ.UTILITY.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.IN_PROGRESS.name())
      .reportedBy("Maria Svensson")
      .assignedTo("John Doe")
      .location("Entire Cottage")
      .estimatedCost(8000.0)
      .startTime(createdAt)
      .requiresContractor(true)
      .contractorInfo("Heating Expert SE, Tel: 070-987-6543")
      .updates(List.of(ArendeStatusDto.builder()
        .updatedBy("Maria Svensson")
        .status(Status.INVESTIGATING.name())
        .message("Emergency call placed to heating specialist")
        .build()))
      .tags(List.of("heating", "urgent"))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    Arende outputUpdatedArende = Arende.builder()
      .id(id)
      .title(inputUpdateArende.title())
      .description(inputUpdateArende.description())
      .type(Typ.valueOf(inputUpdateArende.type()))
      .priority(Prioritet.valueOf(inputUpdateArende.priority()))
      .status(Status.valueOf(Status.CLOSED.name()))
      .reportedBy(inputUpdateArende.reportedBy())
      .assignedTo(inputUpdateArende.assignedTo())
      .location(inputUpdateArende.location())
      .estimatedCost(inputUpdateArende.estimatedCost())
      .actualCost(10000.0)
      .startTime(inputUpdateArende.startTime())
      .resolvedTime(resolvedTime)
      .resolution("Replaced heating system")
      .requiresContractor(inputUpdateArende.requiresContractor())
      .contractorInfo(inputUpdateArende.contractorInfo())
      .updates(List.of(ArendeStatus.builder()
          .updatedBy("Maria Svensson")
          .status(Status.INVESTIGATING.name())
          .message("Emergency call placed to heating specialist")
          .build(),
        ArendeStatus.builder()
          .updatedBy("John Doe")
          .status(Status.RESOLVED.name())
          .build(),
        ArendeStatus.builder()
          .updatedBy("John Doe")
          .status(Status.CLOSED.name())
          .build()))
      .tags(inputUpdateArende.tags())
      .createdAt(inputUpdateArende.createdAt())
      .updatedAt(lastUpdatedAt)
      .build();

    given(arendeService.update(id, inputUpdateArende)).willReturn(outputUpdatedArende);

    mvc.perform(MockMvcRequestBuilders
        .put(BASE_URL + "/{id}", id)
        .content(mapper.writeValueAsString(inputUpdateArende))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title").value(inputUpdateArende.title()))
      .andExpect(jsonPath("$.description").value(inputUpdateArende.description()))
      .andExpect(jsonPath("$.type").value(inputUpdateArende.type()))
      .andExpect(jsonPath("$.priority").value(inputUpdateArende.priority()))
      .andExpect(jsonPath("$.status").value(outputUpdatedArende.status().name()))
      .andExpect(jsonPath("$.reportedBy").value(inputUpdateArende.reportedBy()))
      .andExpect(jsonPath("$.assignedTo").value(inputUpdateArende.assignedTo()))
      .andExpect(jsonPath("$.location").value(inputUpdateArende.location()))
      .andExpect(jsonPath("$.estimatedCost").value(inputUpdateArende.estimatedCost()))
      .andExpect(jsonPath("$.actualCost").value(outputUpdatedArende.actualCost()))
      .andExpect(jsonPath("$.startTime").value(inputUpdateArende.startTime().toString()))
      .andExpect(jsonPath("$.resolvedTime").value(outputUpdatedArende.resolvedTime().toString()))
      .andExpect(jsonPath("$.resolution").value(outputUpdatedArende.resolution()))
      .andExpect(jsonPath("$.requiresContractor").value(inputUpdateArende.requiresContractor()))
      .andExpect(jsonPath("$.contractorInfo").value(inputUpdateArende.contractorInfo()))
      .andExpect(jsonPath("$.updates[0].status").value(outputUpdatedArende.updates().getFirst().status()))
      .andExpect(jsonPath("$.updates[1].status").value(outputUpdatedArende.updates().get(1).status()))
      .andExpect(jsonPath("$.updates[2].status").value(outputUpdatedArende.updates().getLast().status()))
      .andExpect(jsonPath("$.tags[0]").value(outputUpdatedArende.tags().getFirst()))
      .andExpect(jsonPath("$.tags[1]").value(outputUpdatedArende.tags().getLast()))
      .andExpect(jsonPath("$.createdAt").value(inputUpdateArende.createdAt().toString()))
      .andExpect(jsonPath("$.updatedAt").value(outputUpdatedArende.updatedAt().toString()));

    verify(arendeService, times(1)).update(id, inputUpdateArende);
  }

  @Test
  public void whenGetArende_thenReturnArende() throws Exception {
    String id = UUID.randomUUID().toString();
    Instant createdAt = Instant.now();
    Instant updatedAt = Instant.now();

    ArendeDto inputArende = ArendeDto.builder()
      .title("Heating System Failure")
      .description("No heat output from radiators, temperature dropping")
      .type(Typ.UTILITY.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.IN_PROGRESS.name())
      .reportedBy("Maria Svensson")
      .assignedTo("John Doe")
      .location("Entire Cottage")
      .estimatedCost(8000.0)
      .startTime(createdAt)
      .requiresContractor(true)
      .contractorInfo("Heating Expert SE, Tel: 070-987-6543")
      .updates(List.of(ArendeStatusDto.builder()
        .updatedBy("Maria Svensson")
        .status(Status.INVESTIGATING.name())
        .timestamp(updatedAt)
        .message("Emergency call placed to heating specialist")
        .build()))
      .tags(List.of("heating", "urgent"))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    Arende output = Arende.fromArendeDto(inputArende).build();

    given(arendeService.findById(id)).willReturn(output);

    mvc.perform(get(BASE_URL + "/{id}", id)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title").value(output.title()))
      .andExpect(jsonPath("$.description").value(output.description()))
      .andExpect(jsonPath("$.type").value(output.type().name()))
      .andExpect(jsonPath("$.priority").value(output.priority().name()))
      .andExpect(jsonPath("$.status").value(output.status().name()))
      .andExpect(jsonPath("$.reportedBy").value(output.reportedBy()))
      .andExpect(jsonPath("$.assignedTo").value(output.assignedTo()))
      .andExpect(jsonPath("$.location").value(output.location()))
      .andExpect(jsonPath("$.estimatedCost").value(output.estimatedCost()))
      .andExpect(jsonPath("$.actualCost").isEmpty())
      .andExpect(jsonPath("$.startTime").value(output.startTime().toString()))
      .andExpect(jsonPath("$.resolvedTime").isEmpty())
      .andExpect(jsonPath("$.resolution").value(output.resolution()))
      .andExpect(jsonPath("$.requiresContractor").value(true))
      .andExpect(jsonPath("$.contractorInfo").value(output.contractorInfo()))
      .andExpect(jsonPath("$.updates[0].status").value(output.updates().getFirst().status()))
      .andExpect(jsonPath("$.tags[0]").value(output.tags().getFirst()))
      .andExpect(jsonPath("$.tags[1]").value(output.tags().getLast()))
      .andExpect(jsonPath("$.createdAt").value(output.createdAt().toString()))
      .andExpect(jsonPath("$.updatedAt").value(output.updatedAt().toString()));

    verify(arendeService, times(1)).findById(id);
  }

  @Test
  public void whenGetAllArende_thenReturnAllArende() throws Exception {
    String id1 = UUID.randomUUID().toString();
    String id2 = UUID.randomUUID().toString();
    Instant createdAt = Instant.now();
    Instant updatedAt = Instant.now();
    Instant secondUpdatedAt = Instant.now();
    Instant lastUpdatedAt = Instant.now();
    Instant resolvedTime = Instant.now().plus(Period.ofDays(7));

    Arende output = Arende.builder()
      .id(id1)
      .title("Water Leak in Bathroom")
      .description("Water leaking from pipe under sink, causing floor damage")
      .type(Typ.DAMAGE)
      .priority(Prioritet.HIGH)
      .status(Status.RESOLVED)
      .reportedBy("Lars Andersson")
      .assignedTo("John Doe")
      .location("Main Bathroom")
      .estimatedCost(5000.0)
      .startTime(createdAt)
      .resolvedTime(resolvedTime)
      .resolution("Replaced damaged pipe and repaired floor")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(
        ArendeStatus.builder()
          .status(Status.INVESTIGATING.name())
          .updatedBy("Lars Andersson")
          .timestamp(updatedAt)
          .build(),
        ArendeStatus.builder()
          .status(Status.IN_PROGRESS.name())
          .updatedBy("Lars Andersson")
          .timestamp(secondUpdatedAt)
          .build(),
        ArendeStatus.builder()
          .status(Status.RESOLVED.name())
          .updatedBy("John Doe")
          .timestamp(lastUpdatedAt)
          .build()
      ))
      .tags(List.of("plumbing", "water-damage", "bathroom"))
      .createdAt(createdAt)
      .updatedAt(lastUpdatedAt)
      .build();

    Arende output2 = Arende.builder()
      .id(id2)
      .title("Heating System Failure")
      .description("No heat output from radiators, temperature dropping")
      .type(Typ.DAMAGE)
      .priority(Prioritet.HIGH)
      .status(Status.IN_PROGRESS)
      .reportedBy("Lars Andersson")
      .assignedTo("Heating Expert SE")
      .location("Entire Cottage")
      .estimatedCost(8000.0)
      .startTime(createdAt)
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(
        ArendeStatus.builder()
          .status(Status.INVESTIGATING.name())
          .message("Emergency call placed to heating specialist")
          .updatedBy("Lars Andersson")
          .timestamp(updatedAt)
          .build(),
        ArendeStatus.builder()
          .status(Status.IN_PROGRESS.name())
          .updatedBy("Lars Andersson")
          .timestamp(secondUpdatedAt)
          .build()
      ))
      .tags(List.of("heating", "urgent", "winter"))
      .createdAt(createdAt)
      .updatedAt(lastUpdatedAt)
      .build();

    given(arendeService.findAll()).willReturn(List.of(output, output2));

    mvc.perform(get(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value(output.id()))
      .andExpect(jsonPath("$[0].title").value(output.title()))
      .andExpect(jsonPath("$[0].description").value(output.description()))
      .andExpect(jsonPath("$[0].type").value(output.type().name()))
      .andExpect(jsonPath("$[0].priority").value(output.priority().name()))
      .andExpect(jsonPath("$[0].status").value(output.status().name()))
      .andExpect(jsonPath("$[0].reportedBy").value(output.reportedBy()))
      .andExpect(jsonPath("$[0].assignedTo").value(output.assignedTo()))
      .andExpect(jsonPath("$[0].location").value(output.location()))
      .andExpect(jsonPath("$[0].estimatedCost").value(output.estimatedCost()))
      .andExpect(jsonPath("$[0].startTime").value(output.startTime().toString()))
      .andExpect(jsonPath("$[0].resolvedTime").value(output.resolvedTime().toString()))
      .andExpect(jsonPath("$[0].resolution").value(output.resolution()))
      .andExpect(jsonPath("$[0].requiresContractor").value(output.requiresContractor()))
      .andExpect(jsonPath("$[0].contractorInfo").value(output.contractorInfo()))
      .andExpect(jsonPath("$[0].updates[0].status").value(output.updates().getFirst().status()))
      .andExpect(jsonPath("$[0].updates[1].status").value(output.updates().get(1).status()))
      .andExpect(jsonPath("$[0].updates[2].status").value(output.updates().getLast().status()))
      .andExpect(jsonPath("$[0].tags[0]").value(output.tags().getFirst()))
      .andExpect(jsonPath("$[0].tags[1]").value(output.tags().get(1)))
      .andExpect(jsonPath("$[0].tags[2]").value(output.tags().getLast()))
      .andExpect(jsonPath("$[0].createdAt").value(output.createdAt().toString()))
      .andExpect(jsonPath("$[0].updatedAt").value(output.updatedAt().toString()))
      .andExpect(jsonPath("$[1].id").value(output2.id()))
      .andExpect(jsonPath("$[1].title").value(output2.title()))
      .andExpect(jsonPath("$[1].description").value(output2.description()))
      .andExpect(jsonPath("$[1].type").value(output2.type().name()))
      .andExpect(jsonPath("$[1].priority").value(output2.priority().name()))
      .andExpect(jsonPath("$[1].status").value(output2.status().name()))
      .andExpect(jsonPath("$[1].reportedBy").value(output2.reportedBy()))
      .andExpect(jsonPath("$[1].assignedTo").value(output2.assignedTo()))
      .andExpect(jsonPath("$[1].location").value(output2.location()))
      .andExpect(jsonPath("$[1].estimatedCost").value(output2.estimatedCost()))
      .andExpect(jsonPath("$[1].actualCost").isEmpty())
      .andExpect(jsonPath("$[1].startTime").value(output2.startTime().toString()))
      .andExpect(jsonPath("$[1].resolvedTime").isEmpty())
      .andExpect(jsonPath("$[1].resolution").isEmpty())
      .andExpect(jsonPath("$[1].requiresContractor").value(output2.requiresContractor()))
      .andExpect(jsonPath("$[1].contractorInfo").value(output2.contractorInfo()))
      .andExpect(jsonPath("$[1].updates[0].status").value(output2.updates().getFirst().status()))
      .andExpect(jsonPath("$[1].updates[1].status").value(output2.updates().getLast().status()))
      .andExpect(jsonPath("$[1].tags[0]").value(output2.tags().getFirst()))
      .andExpect(jsonPath("$[1].tags[1]").value(output2.tags().get(1)))
      .andExpect(jsonPath("$[1].tags[2]").value(output2.tags().getLast()))
      .andExpect(jsonPath("$[1].createdAt").value(output2.createdAt().toString()))
      .andExpect(jsonPath("$[1].updatedAt").value(output2.updatedAt().toString()

      ));

    verify(arendeService, times(1)).findAll();
  }

  @Test
  public void whenDeleteAllArenden_thenReturnEmptyList() throws Exception {
    given(arendeService.findAll()).willReturn(List.of());
    mvc.perform(delete(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());
  }

  // Unhappy cases

  @Test
  void whenGetById_nonExistingArende_thenReturn404() throws Exception {
    // Given
    String nonExistingId = "non-existing-id";
    when(arendeService.findById(nonExistingId))
      .thenThrow(new EntityNotFoundException("Arende not found with id: " + nonExistingId));

    // When & Then
    mvc.perform(get(BASE_URL + "/{id}", nonExistingId))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value("404"))
      .andExpect(jsonPath("$.error").value("Not Found"))
      .andExpect(jsonPath("$.message").value("Resource not found"))
      .andExpect(jsonPath("$.details").value("Arende not found with id: " + nonExistingId))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidTitleCases")
  void whenCreate_andTitleIsNullOrBlankOrNotBetween3And100Characters_thenReturn400(String title, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
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
      Arguments.of(
        null,
        "title: Title is required"
      ),
      Arguments.of(
        "",
        "title: Title must be between 3 and 100 characters"
      ),
      Arguments.of(
        " ",
        "title: Title must be between 3 and 100 characters"
      ),
      Arguments.of(
        "ab",
        "title: Title must be between 3 and 100 characters"
      ),
      Arguments.of(
        "a" .repeat(101),
        "title: Title must be between 3 and 100 characters"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidDescriptionCases")
  void whenCreate_andDescriptionIsNullOrBlankOrExceeds1000Characters_thenReturn400(String description, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
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
      Arguments.of(
        null,
        "description: Description is required"
      ),
      Arguments.of(
        " ",
        "description: Description is required"
      ),
      Arguments.of(
        "",
        "description: Description is required"
      ),
      Arguments.of(
        "a" .repeat(1001),
        "description: Description cannot exceed 1000 characters"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidTypeCases")
  void whenCreate_andTypeNullOrEmptyOrNotMatchValidString_thenReturn400(String type, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .type(type)
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

    verify(arendeService, times(0)).saveArende(invalidInput);
  }

  private static Stream<Arguments> invalidTypeCases() {
    return Stream.of(
      Arguments.of(
        null,
        "type: Type is required"
      ),
      Arguments.of(
        " ",
        "type: Type must be either MAINTENANCE, DAMAGE, UTILITY, SECURITY, PEST, WEATHER or OTHER"
      ),
      Arguments.of(
        "",
        "type: Type must be either MAINTENANCE, DAMAGE, UTILITY, SECURITY, PEST, WEATHER or OTHER"
      ),
      Arguments.of(
        "a",
        "type: Type must be either MAINTENANCE, DAMAGE, UTILITY, SECURITY, PEST, WEATHER or OTHER"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidPriorityCases")
  void whenCreate_andPriorityIsNullOrEmptyOrNotMatchValidString_thenReturn400(String priority, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .priority(priority)
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

    verify(arendeService, times(0)).saveArende(invalidInput);
  }

  private static Stream<Arguments> invalidPriorityCases() {
    return Stream.of(
      Arguments.of(
        null,
        "priority: Priority is required"
      ),
      Arguments.of(
        " ",
        "priority: Priority must be either LOW, MEDIUM, HIGH or CRITICAL"
      ),
      Arguments.of(
        "",
        "priority: Priority must be either LOW, MEDIUM, HIGH or CRITICAL"
      ),
      Arguments.of(
        "a",
        "priority: Priority must be either LOW, MEDIUM, HIGH or CRITICAL"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidStatusCases")
  void whenCreate_andStatusIsNullOrEmptyOrNotMatchValidString_thenReturn400(String status, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .status(status)
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

    verify(arendeService, times(0)).saveArende(invalidInput);
  }

  private static Stream<Arguments> invalidStatusCases() {
    return Stream.of(
      Arguments.of(
        null,
        "status: Status is required"
      ),
      Arguments.of(
        " ",
        "status: Status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSED"
      ),
      Arguments.of(
        "",
        "status: Status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSED"
      ),
      Arguments.of(
        "a",
        "status: Status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSED"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidReportedByCases")
  void whenCreate_andReportedByIsNullOrEmptyOrNotBetween3And100Characters_thenReturn400(String reportedBy, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .reportedBy(reportedBy)
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

    verify(arendeService, times(0)).saveArende(invalidInput);
  }

  private static Stream<Arguments> invalidReportedByCases() {
    return Stream.of(
      Arguments.of(
        null,
        "reportedBy: ReportedBy is required"
      ),
      Arguments.of(
        "ab",
        "reportedBy: ReportedBy must be between 3 and 30 characters"
      ),
      Arguments.of(
        "a".repeat(31),
        "reportedBy: ReportedBy must be between 3 and 30 characters"
      ),
      Arguments.of(
        "John@Doe",
        "reportedBy: ReportedBy must contain only letters, numbers and spaces"
      ),
      Arguments.of(
        "John @ Doe",
        "reportedBy: ReportedBy must contain only letters, numbers and spaces"
      ),
      Arguments.of(
        "John-Doe",
        "reportedBy: ReportedBy must contain only letters, numbers and spaces"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidAssignedToCases")
  void whenCreate_andAssignedToyIsNullOrEmptyOrNotBetween3And100Characters_thenReturn400(String assignedTo, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .assignedTo(assignedTo)
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

    verify(arendeService, times(0)).saveArende(invalidInput);
  }

  private static Stream<Arguments> invalidAssignedToCases() {
    return Stream.of(
      Arguments.of(
        null,
        "assignedTo: AssignedTo is required"
      ),
      Arguments.of(
        "ab",
        "assignedTo: AssignedTo must be between 3 and 30 characters"
      ),
      Arguments.of(
        "a".repeat(31),
        "assignedTo: AssignedTo must be between 3 and 30 characters"
      ),
      Arguments.of(
        "John@Doe",
        "assignedTo: AssignedTo must contain only letters, numbers and spaces"
      ),
      Arguments.of(
        "John @ Doe",
        "assignedTo: AssignedTo must contain only letters, numbers and spaces"
      ),
      Arguments.of(
        "John-Doe",
        "assignedTo: AssignedTo must contain only letters, numbers and spaces"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidEstimatedCostCases")
  void whenCreate_andEstimatedCostIsInvalid_thenReturn400(Double estimatedCost, String expectedError) throws Exception {
    ArendeDto invalidInput = createArendeDtoBuilder()
      .estimatedCost(estimatedCost)
      .build();

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

  private static Stream<Arguments> invalidEstimatedCostCases() {
    return Stream.of(
      Arguments.of(null, "estimatedCost: Estimated cost is required"),
      Arguments.of(-1.0, "estimatedCost: Estimated cost must be greater than or equal to 0"),
      Arguments.of(-100.0, "estimatedCost: Estimated cost must be greater than or equal to 0"),
      Arguments.of(1000000000.0, "estimatedCost: Estimated cost cannot exceed 999,999,999.99"),
      Arguments.of(12345678901.23, "estimatedCost: Estimated cost cannot exceed 999,999,999.99")
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidMessageCases")
  void whenCreate_andMessageIsNullOrBlankOrExceeds1000Characters_thenReturn400(String message, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .updates(
        List.of(
          createArendeStatusDtoBuilder()
          .message(message)
        .build()))
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

  private static Stream<Arguments> invalidMessageCases() {
    return Stream.of(
      Arguments.of(
        null,
        "updates[0].message: Message is required"
      ),
      Arguments.of(
        " ",
        "updates[0].message: Message is required"
      ),
      Arguments.of(
        "",
        "updates[0].message: Message is required"
      ),
      Arguments.of(
        "a" .repeat(1001),
        "updates[0].message: Message cannot exceed 1000 characters"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidUpdatedByCases")
  void whenCreate_andUpdatedByIsNullOrBlankOrNotBetween3And30Characters_thenReturn400(String updatedBy, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .updates(
        List.of(
          createArendeStatusDtoBuilder()
            .updatedBy(updatedBy)
            .build()))
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

  private static Stream<Arguments> invalidUpdatedByCases() {
    return Stream.of(
      Arguments.of(
        null,
        "updates[0].updatedBy: UpdatedBy is required"
      ),
      Arguments.of(
        " ",
        "updates[0].updatedBy: UpdatedBy must be between 3 and 30 characters"
      ),
      Arguments.of(
        "ab",
        "updates[0].updatedBy: UpdatedBy must be between 3 and 30 characters"
      ),
      Arguments.of(
        "a" .repeat(101),
        "updates[0].updatedBy: UpdatedBy must be between 3 and 30 characters"
      ),
      Arguments.of(
        "John@Doe",
        "updates[0].updatedBy: UpdatedBy must contain only letters, numbers and spaces"
      ),
      Arguments.of(
        "John @ Doe",
        "updates[0].updatedBy: UpdatedBy must contain only letters, numbers and spaces"
      ),
      Arguments.of(
        "John-Doe",
        "updates[0].updatedBy: UpdatedBy must contain only letters, numbers and spaces"
      )
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidArendeStatusCases")
  void whenCreate_andArendeStatusIsNullOrEmptyOrNotMatchValidString_thenReturn400(String status, String expectedError) throws Exception {

    // Given
    ArendeDto invalidInput = createArendeDtoBuilder()
      .updates(
        List.of(
          createArendeStatusDtoBuilder()
            .status(status)
            .build()))
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

    verify(arendeService, times(0)).saveArende(invalidInput);
  }

  private static Stream<Arguments> invalidArendeStatusCases() {
    return Stream.of(
      Arguments.of(
        null,
        "updates[0].status: Arende status is required"
      ),
      Arguments.of(
        " ",
        "updates[0].status: Arende status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSE"
      ),
      Arguments.of(
        "",
        "updates[0].status: Arende status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSE"
      ),
      Arguments.of(
        "a",
        "updates[0].status: Arende status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSE"
      )
    );
  }

  @Test
  void whenUpdate_withNonExistingId_thenReturn404() throws Exception {
    // Given
    String nonExistingId = "non-existing-id";
    ArendeDto inputUpdateArende = ArendeDto.builder()
      .title("Heating System Failure")
      .description("No heat output from radiators, temperature dropping")
      .type(Typ.UTILITY.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.IN_PROGRESS.name())
      .reportedBy("Maria Svensson")
      .assignedTo("John Doe")
      .location("Entire Cottage")
      .estimatedCost(8000.0)
      .startTime(Instant.now())
      .requiresContractor(true)
      .contractorInfo("Heating Expert SE, Tel: 070-987-6543")
      .updates(List.of(ArendeStatusDto.builder()
        .updatedBy("Maria Svensson")
        .status(Status.INVESTIGATING.name())
        .timestamp(Instant.now())
        .message("Emergency call placed to heating specialist")
        .build()))
      .tags(List.of("heating", "urgent"))
      .build();
    when(arendeService.update(anyString(), any(ArendeDto.class)))
      .thenThrow(new EntityNotFoundException("Cannot update non-existing arende"));

    // When & Then
    mvc.perform(put(BASE_URL + "/{id}", nonExistingId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(inputUpdateArende)))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(404))
      .andExpect(jsonPath("$.error").value("Not Found"))
      .andExpect(jsonPath("$.message").value("Resource not found"))
      .andExpect(jsonPath("$.details").value("Cannot update non-existing arende"))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void whenDelete_withNonExistingId_thenReturn404() throws Exception {
    // Given
    String nonExistingId = "non-existing-id";
    doThrow(new EntityNotFoundException("Cannot delete non-existing arende"))
      .when(arendeService).deleteById(nonExistingId);

    // When & Then
    mvc.perform(delete(BASE_URL + "/{id}", nonExistingId))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(404))
      .andExpect(jsonPath("$.error").value("Not Found"))
      .andExpect(jsonPath("$.message").value("Resource not found"))
      .andExpect(jsonPath("$.details").value("Cannot delete non-existing arende"))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void whenInternalServerError_thenReturn500() throws Exception {
    // Given
    when(arendeService.findAll())
      .thenThrow(new RuntimeException("Unexpected server error"));

    // When & Then
    mvc.perform(get(BASE_URL))
      .andExpect(status().isInternalServerError())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(500))
      .andExpect(jsonPath("$.error").value("Internal Server Error"))
      .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
      .andExpect(jsonPath("$.details").value("Unexpected server error"))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void whenMethodArgumentNotValid_thenReturn400() throws Exception {
    // Given
    String emptyJson = "{}";
    // When & Then
    mvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(emptyJson)) // Empty JSON to trigger validation error
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status").value(400))
      .andExpect(jsonPath("$.error").value("Bad Request"))
      .andExpect(jsonPath("$.message").value("Validation failed"))
      .andExpect(jsonPath("$.timestamp").exists());
  }

  private ArendeDtoBuilder createArendeDtoBuilder() {

    return ArendeDto.builder()
      .title("Heating System Failure")
      .description("No heat output from radiators, temperature dropping")
      .type(Typ.UTILITY.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.IN_PROGRESS.name())
      .reportedBy("Maria Svensson")
      .assignedTo("John Doe")
      .location("Entire Cottage")
      .estimatedCost(8000.00)
      .startTime(Instant.now())
      .requiresContractor(true)
      .contractorInfo("Heating Expert SE, Tel: 070-987-6543")
      .updates(List.of(ArendeStatusDto.builder()
        .updatedBy("Maria Svensson")
        .status(Status.INVESTIGATING.name())
        .timestamp(Instant.now())
        .message("Emergency call placed to heating specialist")
        .build()))
      .tags(List.of("heating", "urgent"))
      .createdAt(Instant.now())
      .updatedAt(Instant.now());
  }

  private ArendeStatusDtoBuilder createArendeStatusDtoBuilder() {
    return ArendeStatusDto.builder()
      .timestamp(Instant.now())
      .message("message")
      .status(Status.NEW.name())
      .updatedBy("updatedBy");
  }
}
