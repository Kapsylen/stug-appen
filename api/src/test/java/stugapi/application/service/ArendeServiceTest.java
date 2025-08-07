package stugapi.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import stugapi.application.domain.model.Arende;
import stugapi.application.domain.model.ArendeStatus;
import stugapi.infrastructure.entities.ArendeEntity;
import stugapi.infrastructure.entities.ArendeStatusEntity;
import stugapi.infrastructure.entities.enums.Prioritet;
import stugapi.infrastructure.entities.enums.Status;
import stugapi.infrastructure.entities.enums.Typ;
import stugapi.infrastructure.repositories.ArendeRepository;
import stugapi.presentation.dto.ArendeDto;
import stugapi.presentation.dto.ArendeStatusDto;
import stugapi.utility.TimeUtility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DataJpaTest
public class ArendeServiceTest {

  @InjectMocks
  private ArendeService arendeService;

  @Mock
  private ArendeRepository arendeRepository;

  @Test
  void whenSaveArende_thenArendeIsSaved() {
    UUID id = UUID.randomUUID();


    ArendeStatusDto arendeStatus1 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Plumber contacted, arriving tomorrow morning")
      .status("in_progress")
      .updatedBy("Test Reporter")
      .build();
    ArendeStatusDto arendeStatus2 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Repair completed, water pressure tested")
      .updatedBy("Plumber AB")
      .status("resolved")
      .build();

    LocalDateTime startTime = LocalDateTime.now();

    ArendeEntity savedArendeEntity = ArendeEntity.builder()
      .id(id)
      .title("Water Leak in Bathroom")
      .description("Water leaking from pipe under sink, causing floor damage")
      .type(Typ.DAMAGE)
      .priority(Prioritet.MEDIUM)
      .status(Status.NEW)
      .reportedBy("Test Reporter")
      .assignedTo("Plumber AB")
      .location("Main bathroom")
      .estimatedCost("1000")
      .startTime(startTime)
      .resolvedTime(null)
      .resolution("Replaced damaged pipe and repaired floor")
      .requiresContractor(true)
      .contractorInfo("Plumber AB, Tel: 070-123-4567")
      .updates(List.of(ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus1, null)), ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus2, null))))
      .tags(List.of("plumbing", "water-damage", "bathroom"))
      .createdAt(startTime)
      .build();

    Mockito.when(arendeRepository.save(any(ArendeEntity.class))).thenReturn(savedArendeEntity);

    ArendeDto newArendeDto = ArendeDto.toArendeDtoBuilder(Arende.fromArendeEntity(savedArendeEntity).build()).build();

    Arende savedArende = arendeService.saveArende(newArendeDto);

    assertEquals(savedArendeEntity.getTitle(), savedArende.title());
    assertEquals(savedArendeEntity.getDescription(), savedArende.description());
    assertEquals(savedArendeEntity.getType(), savedArende.type());
    assertEquals(savedArendeEntity.getPriority(), savedArende.priority());
    assertEquals(savedArendeEntity.getStatus(), savedArende.status());
    assertEquals(savedArendeEntity.getReportedBy(), savedArende.reportedBy());
    assertEquals(savedArendeEntity.getAssignedTo(), savedArende.assignedTo());
    assertEquals(savedArendeEntity.getLocation(), savedArende.location());
    assertEquals(savedArendeEntity.getEstimatedCost(), savedArende.estimatedCost());
    assertEquals(savedArendeEntity.getActualCost(), savedArende.actualCost());
    assertEquals(savedArendeEntity.getStartTime(), savedArende.startTime());
    assertEquals(savedArendeEntity.getResolvedTime(), savedArende.resolvedTime());
    assertEquals(savedArendeEntity.getResolution(), savedArende.resolution());
    assertEquals(savedArendeEntity.isRequiresContractor(), savedArende.requiresContractor());
    assertEquals(savedArendeEntity.getContractorInfo(), savedArende.contractorInfo());
    assertEquals(savedArendeEntity.getUpdates().size(), savedArende.updates().size());
    assertEquals(savedArendeEntity.getTags(), savedArende.tags());
    assertEquals(savedArendeEntity.getCreatedAt(), savedArende.createdAt());
    assertEquals(savedArendeEntity.getUpdatedAt(), savedArende.updatedAt());

    verify(arendeRepository, times(1)).save(any(ArendeEntity.class));
  }

  @Test
  void whenSaveArende_whenStartTimeIsSet_startTimeWillBeSetToCurrentTimeAndArendeIsSaved() {
    String startTime = LocalDate.now().toString();

    ArendeStatusDto arendeStatus1 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .timestamp(startTime)
      .message("Plumber contacted, arriving tomorrow morning")
      .status("in_progress")
      .updatedBy("Test Reporter")
      .build();
    ArendeStatusDto arendeStatus2 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Repair completed, water pressure tested")
      .updatedBy("Plumber AB")
      .status("resolved")
      .build();

    ArendeDto arendeDto = ArendeDto.builder()
      .type(Typ.DAMAGE.name())
      .priority(Prioritet.MEDIUM.name())
      .status(Status.NEW.name())
      .startTime(startTime)
      .updates(List.of(arendeStatus1, arendeStatus2))
      .build();

    Mockito.when(arendeRepository.save(any(ArendeEntity.class))).thenReturn(ArendeEntity.builder()
      .id(UUID.randomUUID())
        .updates(List.of(ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus1, null)), ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus2, null))))
      .build());

    arendeService.saveArende(arendeDto);

    verify(arendeRepository, times(1)).save(any(ArendeEntity.class));


  }

  @Test
  void whenDeleteExistingArende_thenArendeIsDeleted() {
    UUID id = UUID.randomUUID();

    ArendeEntity savedArendeEntity = ArendeEntity.builder()
      .id(id)
      .build();

    Mockito.when(arendeRepository.findById(any(UUID.class))).thenReturn(Optional.of(savedArendeEntity));

    arendeService.deleteById(id.toString());

    verify(arendeRepository, times(1)).deleteById(any(UUID.class));
  }

  @Test
  void update_InvalidId_ShouldThrowException() {
    // Arrange
    String invalidId = UUID.randomUUID().toString();
    ArendeDto updateDto = ArendeDto.builder()
      .title("Updated Title")
      .build();

    Mockito.when(arendeRepository.findById(UUID.fromString(invalidId))).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> arendeService.update(invalidId, updateDto));

    verify(arendeRepository, times(0)).save(any(ArendeEntity.class));
  }

  @Test
  void whenUpdateArende_thenArendeIsUpdated() {
    UUID id = UUID.randomUUID();
    ArendeStatusDto arendeStatus1 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Plumber contacted, arriving tomorrow morning")
      .status("in_progress")
      .updatedBy("Test Reporter")
      .build();
    ArendeStatusDto arendeStatus2 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Repair completed, water pressure tested")
      .updatedBy("Plumber AB")
      .status("resolved")
      .build();

    LocalDate startDate = LocalDate.now();
    LocalDateTime startTime = startDate.atStartOfDay();

    ArendeEntity savedArendeEntity = ArendeEntity.builder()
      .id(id)
      .title("Water Leak in Bathroom")
      .description("Water leaking from pipe under sink, causing floor damage")
      .type(Typ.DAMAGE)
      .priority(Prioritet.MEDIUM)
      .status(Status.NEW)
      .reportedBy("Test Reporter")
      .assignedTo("Plumber AB")
      .location("Main bathroom")
      .estimatedCost("1000")
      .startTime(startTime)
      .resolvedTime(startTime.plusDays(7))
      .resolution("Replaced damaged pipe and repaired floor")
      .requiresContractor(true)
      .contractorInfo("Plumber AB, Tel: 070-123-4567")
      .updates(List.of(ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus1, null))))
      .tags(List.of("plumbing", "water-damage", "bathroom"))
      .createdAt(LocalDateTime.now().minusDays(10))
      .updatedAt(LocalDateTime.now().minusDays(10))
      .build();

    ArendeDto updateArendeDto = ArendeDto.builder()
      .id(id.toString())
      .title(savedArendeEntity.getTitle())
      .description(savedArendeEntity.getDescription())
      .type(savedArendeEntity.getType().name())
      .priority(savedArendeEntity.getPriority().name())
      .status(savedArendeEntity.getStatus().name())
      .reportedBy("Plumber AB")
      .assignedTo(savedArendeEntity.getAssignedTo())
      .location(savedArendeEntity.getLocation())
      .estimatedCost(savedArendeEntity.getEstimatedCost())
      .actualCost("2000")
      .startTime(startDate.toString())
      .resolvedTime(startDate.plusDays(7).toString())
      .resolution(savedArendeEntity.getResolution())
      .requiresContractor(true)
      .contractorInfo(savedArendeEntity.getContractorInfo())
      .updates(List.of(arendeStatus1, arendeStatus2))
      .tags(savedArendeEntity.getTags())
      .build();


    ArendeEntity updatedArendeEntity = ArendeEntity.fromArende(Arende.fromArendeDto(updateArendeDto).build()).build();

    // Given

    Mockito.when(arendeRepository.findById(id)).thenReturn(Optional.of(savedArendeEntity));
    Mockito.when(arendeRepository.save(any(ArendeEntity.class))).thenReturn(updatedArendeEntity);

    // When

    Arende updatedArende = arendeService.update(id.toString(), updateArendeDto);

    // Then

    assertEquals(updateArendeDto.title(), updatedArende.title());
    assertEquals(updateArendeDto.description(), updatedArende.description());
    assertEquals(updateArendeDto.type(), updatedArende.type().name());
    assertEquals(updateArendeDto.priority(), updatedArende.priority().name());
    assertEquals(updateArendeDto.status(), updatedArende.status().name());
    assertEquals(updateArendeDto.estimatedCost(), updatedArende.estimatedCost());
    assertEquals(updateArendeDto.actualCost(), updatedArende.actualCost());
    assertEquals(updateArendeDto.reportedBy(), updatedArende.reportedBy());
    assertEquals(updateArendeDto.assignedTo(), updatedArende.assignedTo());
    assertEquals(updateArendeDto.location(), updatedArende.location());
    assertEquals(TimeUtility.parseDate(LocalDate.now().toString()), updatedArende.startTime());
    assertEquals(TimeUtility.parseDate(LocalDate.now().plusDays(7).toString()), updatedArende.resolvedTime());
    assertEquals(updateArendeDto.resolution(), updatedArende.resolution());
    assertEquals(updateArendeDto.requiresContractor(), updatedArende.requiresContractor());
    assertEquals(updateArendeDto.contractorInfo(), updatedArende.contractorInfo());
    assertEquals(updateArendeDto.updates().size(), updatedArende.updates().size());
    assertEquals(updateArendeDto.tags(), updatedArende.tags());
    assertNotNull(updatedArende.createdAt());
    assertNotNull(updatedArende.updatedAt());

    verify(arendeRepository, times(1)).save(any(ArendeEntity.class));
  }

  @Test
  void whenFindArendeById_thenArendeIsReturned() {
    UUID id = UUID.randomUUID();

    ArendeStatusDto arendeStatus1 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Plumber contacted, arriving tomorrow morning")
      .status("in_progress")
      .updatedBy("Test Reporter")
      .build();

    ArendeEntity savedArendeEntity = ArendeEntity.builder()
      .id(id)
      .title("Water Leak in Bathroom")
      .description("Water leaking from pipe under sink, causing floor damage")
      .type(Typ.DAMAGE)
      .priority(Prioritet.MEDIUM)
      .status(Status.NEW)
      .reportedBy("Test Reporter")
      .assignedTo("Plumber AB")
      .location("Main bathroom")
      .estimatedCost("1000")
      .resolvedTime(null)
      .resolution("Replaced damaged pipe and repaired floor")
      .requiresContractor(true)
      .contractorInfo("Plumber AB, Tel: 070-123-4567")
      .updates(List.of(ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus1, null))))
      .tags(List.of("plumbing", "water-damage", "bathroom"))
      .build();

    // Given

    Mockito.when(arendeRepository.save(any(ArendeEntity.class))).thenReturn(savedArendeEntity);
    Mockito.when(arendeRepository.findById(id)).thenReturn(Optional.of(savedArendeEntity));

    // When

    Arende foundArende = arendeService.findById(id.toString());

    // Then

    assertNotNull(foundArende);
    verify(arendeRepository, times(1)).findById(id);
  }

  @Test
  void whenFindArendeById_thenArendeNotFound() {
    UUID id = UUID.randomUUID();
    Mockito.when(arendeRepository.findById(id)).thenReturn(Optional.empty());
    assertThrows(RuntimeException.class, () -> arendeService.findById(id.toString()));
    verify(arendeRepository, times(1)).findById(id);
  }

  @Test
  void whenFindAllArende_thenArendeListIsReturned() {

    ArendeStatusDto arendeStatus1Arende1 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Plumber contacted, arriving tomorrow morning")
      .status("in_progress")
      .updatedBy("Test Reporter")
      .build();
    ArendeStatusDto arendeStatus2Arende1 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Repair completed, water pressure tested")
      .updatedBy("Plumber AB")
      .status("resolved")
      .build();

    ArendeStatusDto arendeStatus1Arende2 = ArendeStatusDto.builder()
      .id(UUID.randomUUID().toString())
      .message("Emergency call placed to heating specialist")
      .status("in_progress")
      .updatedBy("Test Reporter")
      .build();

    ArendeEntity savedArendeEntity1 = ArendeEntity.builder()
      .id(UUID.randomUUID())
      .title("Water Leak in Bathroom")
      .description("Water leaking from pipe under sink, causing floor damage")
      .type(Typ.DAMAGE)
      .priority(Prioritet.MEDIUM)
      .status(Status.NEW)
      .reportedBy("Test Reporter")
      .assignedTo("Plumber AB")
      .location("Main bathroom")
      .estimatedCost("1000")
      .resolvedTime(null)
      .resolution("Replaced damaged pipe and repaired floor")
      .requiresContractor(true)
      .contractorInfo("Plumber AB, Tel: 070-123-4567")
      .updates(List.of(ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus1Arende1, null)), ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus2Arende1, null))))
      .tags(List.of("plumbing", "water-damage", "bathroom"))
      .build();

    ArendeEntity savedArendeEntity2 = ArendeEntity.builder()
      .id(UUID.randomUUID())
      .title("Heating System Failure")
      .description("No heat output from radiators, temperature dropping")
      .type(Typ.UTILITY)
      .priority(Prioritet.CRITICAL)
      .status(Status.NEW)
      .reportedBy("Test Reporter")
      .assignedTo("Heating Expert SE")
      .location("Entire Cottage")
      .estimatedCost("8000")
      .resolvedTime(null)
      .resolution("Replaced damaged pipe and repaired floor")
      .requiresContractor(true)
      .contractorInfo("Heating Expert SE, Tel: 070-987-6543")
      .updates(List.of(ArendeStatusEntity.toArendeStatusEntity(ArendeStatus.toArendeStatus(arendeStatus1Arende2, null))))
      .tags(List.of("heating", "urgent", "winter"))
      .build();


    List<ArendeEntity> arendeList = List.of(savedArendeEntity1, savedArendeEntity2);

    Mockito.when(arendeRepository.findAll()).thenReturn(arendeList);

    List<Arende> foundArendeList = arendeService.findAll();

    assertNotNull(foundArendeList);
    assertAll(
      () -> assertEquals(savedArendeEntity1.getTitle(), foundArendeList.getFirst().title()),
      () -> assertEquals(savedArendeEntity1.getDescription(), foundArendeList.getFirst().description()),
      () -> assertEquals(savedArendeEntity1.getType(), foundArendeList.getFirst().type()),
      () -> assertEquals(savedArendeEntity1.getPriority(), foundArendeList.getFirst().priority()),
      () -> assertEquals(savedArendeEntity1.getStatus(), foundArendeList.getFirst().status()),
      () -> assertEquals(savedArendeEntity1.getReportedBy(), foundArendeList.getFirst().reportedBy()),
      () -> assertEquals(savedArendeEntity1.getAssignedTo(), foundArendeList.getFirst().assignedTo()),
      () -> assertEquals(savedArendeEntity1.getLocation(), foundArendeList.getFirst().location()),
      () -> assertEquals(savedArendeEntity1.getEstimatedCost(), foundArendeList.getFirst().estimatedCost()),
      () -> assertEquals(savedArendeEntity1.getResolvedTime(), foundArendeList.getFirst().resolvedTime()),
      () -> assertEquals(savedArendeEntity1.getResolution(), foundArendeList.getFirst().resolution()),
      () -> assertTrue(foundArendeList.getFirst().requiresContractor()),
      () -> assertEquals(savedArendeEntity1.getContractorInfo(), foundArendeList.getFirst().contractorInfo()),
      () ->
        assertAll(
          () -> assertEquals(savedArendeEntity1.getUpdates().getFirst().getUpdatedBy(), foundArendeList.getFirst().updates().getFirst().updatedBy()),
          () -> assertEquals(savedArendeEntity1.getUpdates().getFirst().getMessage(), foundArendeList.getFirst().updates().getFirst().message()),
          () -> assertEquals(savedArendeEntity1.getUpdates().getFirst().getTimestamp(), foundArendeList.getFirst().updates().getFirst().timestamp()),
          () -> assertEquals(savedArendeEntity1.getUpdates().getFirst().getStatus(), foundArendeList.getFirst().updates().getFirst().status()),
          () -> assertEquals(savedArendeEntity1.getUpdates().getLast().getUpdatedBy(), foundArendeList.getFirst().updates().getLast().updatedBy()),
          () -> assertEquals(savedArendeEntity1.getUpdates().getLast().getMessage(), foundArendeList.getFirst().updates().getLast().message()),
          () -> assertEquals(savedArendeEntity1.getUpdates().getLast().getTimestamp(), foundArendeList.getFirst().updates().getLast().timestamp()),
          () -> assertEquals(savedArendeEntity1.getUpdates().getLast().getStatus(), foundArendeList.getFirst().updates().getLast().status())
        ),
      () -> assertAll(
        () -> assertEquals(savedArendeEntity2.getTags().getFirst(), foundArendeList.getLast().tags().getFirst())),
      () -> assertEquals(savedArendeEntity2.getTitle(), foundArendeList.getLast().title()),
      () -> assertEquals(savedArendeEntity2.getDescription(), foundArendeList.getLast().description()),
      () -> assertEquals(savedArendeEntity2.getType(), foundArendeList.getLast().type()),
      () -> assertEquals(savedArendeEntity2.getPriority(), foundArendeList.getLast().priority()),
      () -> assertEquals(savedArendeEntity2.getStatus(), foundArendeList.getLast().status()),
      () -> assertEquals(savedArendeEntity2.getReportedBy(), foundArendeList.getLast().reportedBy()),
      () -> assertEquals(savedArendeEntity2.getAssignedTo(), foundArendeList.getLast().assignedTo()),
      () -> assertEquals(savedArendeEntity2.getLocation(), foundArendeList.getLast().location()),
      () -> assertEquals(savedArendeEntity2.getEstimatedCost(), foundArendeList.getLast().estimatedCost()),
      () -> assertEquals(savedArendeEntity2.getResolvedTime(), foundArendeList.getLast().resolvedTime()),
      () -> assertEquals(savedArendeEntity2.getResolution(), foundArendeList.getLast().resolution()),
      () -> assertTrue(foundArendeList.getLast().requiresContractor()),
      () -> assertEquals(savedArendeEntity2.getContractorInfo(), foundArendeList.getLast().contractorInfo()),
      () ->
        assertAll(
          () -> assertEquals(savedArendeEntity2.getUpdates().getFirst().getUpdatedBy(), foundArendeList.getLast().updates().getFirst().updatedBy()),
          () -> assertEquals(savedArendeEntity2.getUpdates().getFirst().getMessage(), foundArendeList.getLast().updates().getFirst().message()),
          () -> assertEquals(savedArendeEntity2.getUpdates().getFirst().getTimestamp(), foundArendeList.getLast().updates().getFirst().timestamp()),
          () -> assertEquals(savedArendeEntity2.getUpdates().getFirst().getStatus(), foundArendeList.getLast().updates().getFirst().status())
        ),
      () -> assertAll(
        () -> assertEquals(savedArendeEntity2.getTags().getFirst(), foundArendeList.getLast().tags().getFirst()))
    );

    assertEquals(2, foundArendeList.size());
    verify(arendeRepository, times(1)).findAll();
  }
}
