package stugapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import stugapi.application.domain.model.Arende;

import java.time.Instant;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant startTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant resolvedTime,
    String resolution,
    boolean requiresContractor,
    String contractorInfo,
    List<ArendeStatusDto> updates,
    List<String> tags,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant updatedAt
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
      .startTime(arende.startTime())
      .resolvedTime(arende.resolvedTime())
      .resolution(arende.resolution())
      .requiresContractor(arende.requiresContractor())
      .contractorInfo(arende.contractorInfo())
      .updates(arende.updates() != null
        ? arende.updates().stream().map(ArendeStatusDto::toArendeStatusDto).toList()
        : List.of())
      .tags(arende.tags() != null ? arende.tags() : List.of()
      )
      .tags(arende.tags())
      .createdAt(arende.createdAt())
      .updatedAt(arende.updatedAt());
  }
}
