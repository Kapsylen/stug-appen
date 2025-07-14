package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.ArendeEntity;
import stugapi.infrastructure.entities.enums.Prioritet;
import stugapi.infrastructure.entities.enums.Status;
import stugapi.infrastructure.entities.enums.Typ;
import stugapi.presentation.dto.ArendeDto;

import java.time.LocalDateTime;
import java.util.List;

import static stugapi.utility.TimeUtility.parseDateTime;

@Builder
public record Arende(
  String id,
  String title,
  String description,
  Typ type,
  Prioritet priority,
  Status status,
  String reportedBy,
  String assignedTo,
  String location,
  String estimatedCost,
  String actualCost,
  LocalDateTime startTime,
  LocalDateTime resolvedTime,
  String resolution,
  boolean requiresContractor,
  String contractorInfo,
  List<ArendeStatus> updates,
  List<String> tags,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {

  public static ArendeBuilder fromArendeDto(ArendeDto arendeDto) {
    return Arende.builder()
      .title(arendeDto.title())
      .description(arendeDto.description())
      .type(Typ.valueOf(arendeDto.type()))
      .priority(Prioritet.valueOf(arendeDto.priority()))
      .status(Status.valueOf(arendeDto.status()))
      .reportedBy(arendeDto.reportedBy())
      .assignedTo(arendeDto.assignedTo())
      .location(arendeDto.location())
      .estimatedCost(arendeDto.estimatedCost())
      .actualCost(arendeDto.actualCost())
      .startTime(parseDateTime(arendeDto.startTime()))
      .resolvedTime(parseDateTime(arendeDto.resolvedTime()))
      .resolution(arendeDto.resolution())
      .requiresContractor(arendeDto.requiresContractor())
      .contractorInfo(arendeDto.contractorInfo())
      .updates(arendeDto.updates().stream().map(ArendeStatus::toArendeStatus).toList())
      .tags(arendeDto.tags())
      .createdAt(parseDateTime(arendeDto.createdAt()))
      .updatedAt(parseDateTime(arendeDto.updatedAt()));
  }

  public static ArendeBuilder fromArendeEntity(ArendeEntity arendeEntity) {
    return Arende.builder()
      .id(arendeEntity.getId().toString())
      .title(arendeEntity.getTitle())
      .description(arendeEntity.getDescription())
      .priority(arendeEntity.getPriority())
      .status(arendeEntity.getStatus())
      .reportedBy(arendeEntity.getReportedBy())
      .assignedTo(arendeEntity.getAssignedTo())
      .location(arendeEntity.getLocation())
      .estimatedCost(arendeEntity.getLocation())
      .actualCost(arendeEntity.getActualCost())
      .startTime(arendeEntity.getStartTime())
      .resolvedTime(arendeEntity.getResolvedTime())
      .resolution(arendeEntity.getResolution())
      .requiresContractor(arendeEntity.isRequiresContractor())
      .contractorInfo(arendeEntity.getContractorInfo())
      .updates(arendeEntity.getUpdates().stream().map(ArendeStatus::toArendeStatus).toList())
      .tags(arendeEntity.getTags());
  }
}
