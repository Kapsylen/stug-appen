package stugapi.presentation.dto;

import lombok.Builder;
import stugapi.application.domain.model.Arende;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ArendeDto(
    String id,
    String title,
    String description,
    String type,
    String priority,
    String status,
    String reportedBy,
    String assignedTo,
    String location,
    String estimatedCost,
    String actualCost,
    String startTime,
    String resolvedTime,
    String resolution,
    boolean requiresContractor,
    String contractorInfo,
    List<ArendeStatusDto> updates,
    List<String> tags,
    String createdAt,
    String updatedAt
) {
  public static ArendeDtoBuilder toArendeDtoBuilder(Arende arende) {
    return ArendeDto.builder()
      .id(arende.id())
      .title(arende.title())
      .description(arende.description())
      .type(arende.type().name())
      .priority(arende.priority().name())
      .status(arende.status().name())
      .reportedBy(arende.reportedBy())
      .assignedTo(arende.assignedTo())
      .location(arende.location())
      .estimatedCost(arende.estimatedCost())
      .actualCost(arende.actualCost())
      .startTime(arende.startTime() != null ? arende.startTime().toString() : LocalDateTime.now().toString())
      .resolvedTime(arende.resolvedTime() != null ? arende.resolvedTime().toString() : null)
      .resolution(arende.resolution())
      .requiresContractor(arende.requiresContractor())
      .contractorInfo(arende.contractorInfo())
      .updates(arende.updates() != null
        ? arende.updates().stream().map(ArendeStatusDto::toArendeStatusDto).toList()
        : List.of())
      .tags(arende.tags() != null ? arende.tags() : List.of()
      )
      .tags(arende.tags())
      .createdAt(arende.createdAt() != null ? arende.createdAt().toString() : LocalDateTime.now().toString())
      .updatedAt(arende.updatedAt() != null ? arende.updatedAt().toString() : LocalDateTime.now().toString());
  }
}
