package stugapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
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

  @Test
  public void whenPostArende_thenCreateArende() throws Exception {

    ObjectMapper mapper = new ObjectMapper();

    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now().plusDays(7);
    LocalDateTime resolvedTime = LocalDateTime.now().plusDays(7);

    ArendeDto input = ArendeDto.builder()
      .title("Test title")
      .description("Test description")
      .type(Typ.DAMAGE.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.CLOSED.name())
      .reportedBy("tester")
      .assignedTo("tester")
      .location("location")
      .estimatedCost("5000")
      .startTime(createdAt.toString())
      .resolvedTime(resolvedTime.toString())
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(ArendeStatusDto.builder().message("INVESTIGATING").build(), ArendeStatusDto.builder().message("CLOSED").build()))
      .tags(List.of("", ""))
      .createdAt(createdAt.toString())
      .updatedAt(updatedAt.toString())
      .build();

    Arende output = Arende.builder()
      .title("Test title")
      .description("Test description")
      .type(Typ.DAMAGE)
      .priority(Prioritet.HIGH)
      .status(Status.CLOSED)
      .reportedBy("tester")
      .assignedTo("tester")
      .location("location")
      .estimatedCost("5000")
      .startTime(createdAt)
      .resolvedTime(resolvedTime)
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(ArendeStatus.builder().status("INVESTIGATING").build(), ArendeStatus.builder().status("CLOSED").build()))
      .tags(List.of("", ""))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    given(arendeService.saveArende(input)).willReturn(output);

    mvc.perform(MockMvcRequestBuilders
        .post("/api/v1/arende")
        .content(mapper.writeValueAsBytes(input))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("DAMAGE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CLOSED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.reportedBy").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.location").value("location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedCost").value("5000"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolvedTime").value(resolvedTime.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolution").value("resolution"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.requiresContractor").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.contractorInfo").value("contructorinfo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[0].status").value("INVESTIGATING"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[1].status").value("CLOSED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(updatedAt.toString()));

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
    ObjectMapper mapper = new ObjectMapper();
    var id = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now().plusDays(7);
    LocalDateTime resolvedTime = LocalDateTime.now().plusDays(7);
    LocalDateTime startTime = createdAt;

    ArendeDto inputUpdateArende = ArendeDto.builder()
      .title("Test title")
      .description("Test description")
      .type(Typ.DAMAGE.name())
      .priority(Prioritet.HIGH.name())
      .status(Status.CLOSED.name())
      .reportedBy("tester")
      .assignedTo("tester")
      .location("location")
      .estimatedCost("5000")
      .startTime(startTime.toString())
      .resolvedTime(resolvedTime.toString())
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(ArendeStatusDto.builder().message("INVESTIGATING").build(), ArendeStatusDto.builder().message("CLOSED").build()))
      .tags(List.of("", ""))
      .createdAt(startTime.toString())
      .updatedAt(updatedAt.toString())
      .build();

    Arende outputUpdatedArende = Arende.builder()
      .id(id)
      .title("Test title")
      .description("Test description")
      .type(Typ.DAMAGE)
      .priority(Prioritet.HIGH)
      .status(Status.CLOSED)
      .reportedBy("tester")
      .assignedTo("tester")
      .location("location")
      .estimatedCost("5000")
      .startTime(startTime)
      .resolvedTime(resolvedTime)
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(ArendeStatus.builder().status("INVESTIGATING").build(), ArendeStatus.builder().status("CLOSED").build()))
      .tags(List.of("", ""))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    given(arendeService.update(id.toString(), inputUpdateArende)).willReturn(outputUpdatedArende);

    mvc.perform(MockMvcRequestBuilders
        .put("/api/v1/arende/{id}", id)
        .content(mapper.writeValueAsBytes(inputUpdateArende))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("DAMAGE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CLOSED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.reportedBy").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.location").value("location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedCost").value("5000"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(startTime.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolvedTime").value(resolvedTime.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolution").value("resolution"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.requiresContractor").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.contractorInfo").value("contructorinfo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[0].status").value("INVESTIGATING"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[1].status").value("CLOSED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(updatedAt.toString()));
  }

  @Test
  public void whenGetArende_thenReturnArende() throws Exception {
    var id = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now().plusDays(7);
    LocalDateTime resolvedTime = LocalDateTime.now().plusDays(7);
    LocalDateTime startTime = createdAt;

    Arende output = Arende.builder()
      .title("Test title")
      .description("Test description")
      .type(Typ.DAMAGE)
      .priority(Prioritet.HIGH)
      .status(Status.CLOSED)
      .reportedBy("tester")
      .assignedTo("tester")
      .location("location")
      .estimatedCost("5000")
      .startTime(startTime)
      .resolvedTime(resolvedTime)
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(ArendeStatus.builder().status("INVESTIGATING").build(), ArendeStatus.builder().status("CLOSED").build()))
      .tags(List.of("", ""))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    given(arendeService.findById(id)).willReturn(output);

    mvc.perform(MockMvcRequestBuilders
        .get("/api/v1/arende/{id}", id)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("DAMAGE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CLOSED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.reportedBy").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.location").value("location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.estimatedCost").value("5000"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(startTime.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolvedTime").value(resolvedTime.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.resolution").value("resolution"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.requiresContractor").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.contractorInfo").value("contructorinfo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[0].status").value("INVESTIGATING"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updates[1].status").value("CLOSED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[0]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.tags[1]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(updatedAt.toString()));

    verify(arendeService).findById(id);
  }

  @Test
  public void whenGetAllArende_thenReturnAllArende() throws Exception {
    String id1 = UUID.randomUUID().toString();
    String id2 = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now().plusDays(7);
    LocalDateTime resolvedTime = LocalDateTime.now().plusDays(7);
    //LocalDateTime startTime = createdAt;

    Arende output = Arende.builder()
      .id(id1)
      .title("Test title")
      .description("Test description")
      .type(Typ.DAMAGE)
      .priority(Prioritet.HIGH)
      .status(Status.INVESTIGATING)
      .reportedBy("tester")
      .assignedTo("tester")
      .location("location")
      .estimatedCost("5000")
      .startTime(createdAt)
      .resolvedTime(resolvedTime)
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(ArendeStatus.builder().status("INVESTIGATING").build()))
      .tags(List.of("", ""))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    Arende output2 = Arende.builder()
      .id(id2)
      .title("Test title")
      .description("Test description")
      .type(Typ.DAMAGE)
      .priority(Prioritet.HIGH)
      .status(Status.RESOLVED)
      .reportedBy("tester")
      .assignedTo("tester")
      .location("location")
      .estimatedCost("5000")
      .startTime(createdAt)
      .resolvedTime(resolvedTime)
      .resolution("resolution")
      .requiresContractor(true)
      .contractorInfo("contructorinfo")
      .updates(List.of(ArendeStatus.builder().status("INVESTIGATING").build(), ArendeStatus.builder().status("RESOLVED").build()))
      .tags(List.of("", ""))
      .createdAt(createdAt)
      .updatedAt(updatedAt)
      .build();

    given(arendeService.findAll()).willReturn(List.of(output, output2));

    mvc.perform(MockMvcRequestBuilders
        .get("/api/v1/arende")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(id1))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("DAMAGE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].priority").value("HIGH"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("INVESTIGATING"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].reportedBy").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].assignedTo").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].location").value("location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].estimatedCost").value("5000"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].startTime").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].resolvedTime").value(resolvedTime.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].resolution").value("resolution"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].requiresContractor").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].contractorInfo").value("contructorinfo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].updates[0].status").value("INVESTIGATING"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].tags[0]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].tags[1]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].updatedAt").value(updatedAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(id2))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Test title"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Test description"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].type").value("DAMAGE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].priority").value("HIGH"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value("RESOLVED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].reportedBy").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].assignedTo").value("tester"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].location").value("location"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].estimatedCost").value("5000"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].startTime").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].resolvedTime").value(resolvedTime.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].resolution").value("resolution"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].requiresContractor").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].contractorInfo").value("contructorinfo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].updates[0].status").value("INVESTIGATING"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].updates[1].status").value("RESOLVED"))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].tags[0]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].tags[1]").value(""))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].createdAt").value(createdAt.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].updatedAt").value(updatedAt.toString()));

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
