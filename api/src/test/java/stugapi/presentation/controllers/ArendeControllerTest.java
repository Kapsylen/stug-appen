package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import stugapi.application.domain.model.Arende;
import stugapi.application.domain.model.ArendeStatus;
import stugapi.application.service.ArendeService;
import stugapi.infrastructure.entities.enums.Prioritet;
import stugapi.infrastructure.entities.enums.Status;
import stugapi.infrastructure.entities.enums.Typ;
import stugapi.presentation.dto.ArendeDto;
import stugapi.presentation.dto.ArendeStatusDto;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(ArendeController.class)
public class ArendeControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockitoBean
  private ArendeService arendeService;

  private ObjectMapper mapper;

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
      .estimatedCost("5000")
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
          .build(),
        ArendeStatusDto.builder()
          .timestamp(resolvedTime)
          .message(Status.CLOSED.name())
          .updatedBy("Lars Andersson")
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

    mvc.perform(MockMvcRequestBuilders
        .post("/api/v1/arende")
        .content(mapper.writeValueAsString(input))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(input.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(input.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(input.type()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(input.priority()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(input.status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.reportedBy").value(input.reportedBy()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value(input.assignedTo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.location").value(input.location()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedCost").value(input.estimatedCost()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(input.startTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolvedTime").value(input.resolvedTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolution").value(input.resolution()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.requiresContractor").value(input.requiresContractor()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.contractorInfo").value(input.contractorInfo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[0].status").value(input.updates().getFirst().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[1].status").value(input.updates().getLast().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0]").value(input.tags().getFirst()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1]").value(input.tags().get(1)))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[2]").value(input.tags().getLast()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(input.createdAt().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(input.updatedAt().toString()));

    verify(arendeService).saveArende(input);

  }

  @Test
  public void whenDeleteArende_thenDeleteArende() throws Exception {
    mvc.perform(MockMvcRequestBuilders
        .delete("/api/v1/arende/12345678-1234-1234-1234-123456789012")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }

  @Test
  public void whenPutArende_thenUpdateArende() throws Exception {
    var id = UUID.randomUUID().toString();
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
      .estimatedCost("8000")
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
      .actualCost("10000")
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
        .put("/api/v1/arende/{id}", id)
        .content(mapper.writeValueAsString(inputUpdateArende))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(inputUpdateArende.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(inputUpdateArende.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(inputUpdateArende.type()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(inputUpdateArende.priority()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(outputUpdatedArende.status().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.reportedBy").value(inputUpdateArende.reportedBy()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value(inputUpdateArende.assignedTo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.location").value(inputUpdateArende.location()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedCost").value(inputUpdateArende.estimatedCost()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.actualCost").value(outputUpdatedArende.actualCost()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(inputUpdateArende.startTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolvedTime").value(outputUpdatedArende.resolvedTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolution").value(outputUpdatedArende.resolution()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.requiresContractor").value(inputUpdateArende.requiresContractor()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.contractorInfo").value(inputUpdateArende.contractorInfo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[0].status").value(outputUpdatedArende.updates().getFirst().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[1].status").value(outputUpdatedArende.updates().get(1).status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[2].status").value(outputUpdatedArende.updates().getLast().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0]").value(outputUpdatedArende.tags().getFirst()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1]").value(outputUpdatedArende.tags().getLast()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(inputUpdateArende.createdAt().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(outputUpdatedArende.updatedAt().toString()));
  }

  @Test
  public void whenGetArende_thenReturnArende() throws Exception {
    var id = UUID.randomUUID().toString();
    Instant createdAt = Instant.now();
    Instant updatedAt = Instant.now();
    Instant resolvedTime = Instant.now().plus(Period.ofDays(7));

    ArendeDto inputArende = ArendeDto.builder()
      .title("Heating System Failure")
      .description("No heat output from radiators, temperature dropping")
      .type(Typ.UTILITY.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.IN_PROGRESS.name())
      .reportedBy("Maria Svensson")
      .assignedTo("John Doe")
      .location("Entire Cottage")
      .estimatedCost("8000")
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

    mvc.perform(MockMvcRequestBuilders
        .get("/api/v1/arende/{id}", id)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(output.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(output.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(output.type().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value(output.priority().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(output.status().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.reportedBy").value(output.reportedBy()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value(output.assignedTo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.location").value(output.location()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedCost").value(output.estimatedCost()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.actualCost").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(output.startTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolvedTime").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolution").value(output.resolution()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.requiresContractor").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.contractorInfo").value(output.contractorInfo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[0].status").value(output.updates().getFirst().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0]").value(output.tags().getFirst()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1]").value(output.tags().getLast()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(output.createdAt().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(output.updatedAt().toString()));

    verify(arendeService).findById(id);
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
      .estimatedCost("5000")
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
      .estimatedCost("8000")
      .startTime(createdAt)
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
          .build()
      ))
      .tags(List.of("heating", "urgent", "winter"))
      .createdAt(createdAt)
      .updatedAt(lastUpdatedAt)
      .build();

    given(arendeService.findAll()).willReturn(List.of(output, output2));

    mvc.perform(MockMvcRequestBuilders
        .get("/api/v1/arende")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(output.id()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(output.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(output.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value(output.type().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].priority").value(output.priority().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(output.status().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].reportedBy").value(output.reportedBy()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].assignedTo").value(output.assignedTo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].location").value(output.location()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].estimatedCost").value(output.estimatedCost()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].startTime").value(output.startTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].resolvedTime").value(output.resolvedTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].resolution").value(output.resolution()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].requiresContractor").value(output.requiresContractor()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].contractorInfo").value(output.contractorInfo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].updates[0].status").value(output.updates().getFirst().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].updates[1].status").value(output.updates().get(1).status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].updates[2].status").value(output.updates().getLast().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].tags[0]").value(output.tags().getFirst()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].tags[1]").value(output.tags().get(1)))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].tags[2]").value(output.tags().getLast()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").value(output.createdAt().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].updatedAt").value(output.updatedAt().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(output2.id()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(output2.title()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(output2.description()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].type").value(output2.type().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].priority").value(output2.priority().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value(output2.status().name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].reportedBy").value(output2.reportedBy()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].assignedTo").value(output2.assignedTo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].location").value(output2.location()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].estimatedCost").value(output2.estimatedCost()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].actualCost").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].startTime").value(output2.startTime().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].resolvedTime").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].resolution").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].requiresContractor").value(output2.requiresContractor()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].contractorInfo").value(output2.contractorInfo()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].updates[0].status").value(output2.updates().getFirst().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].updates[1].status").value(output2.updates().getLast().status()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].tags[0]").value(output2.tags().getFirst()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].tags[1]").value(output2.tags().get(1)))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].tags[2]").value(output2.tags().getLast()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].createdAt").value(output2.createdAt().toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].updatedAt").value(output2.updatedAt().toString()

      ));

    verify(arendeService).findAll();
  }

  @Test
  public void whenDeleteAllArenden_thenReturnEmptyList() throws Exception {
    mvc.perform(MockMvcRequestBuilders
        .delete("/api/v1/arende")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status()
        .isNoContent());
  }
}
