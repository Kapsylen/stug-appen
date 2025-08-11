package stugapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import stugapi.application.domain.model.Arende;

import java.time.Instant;
import java.util.List;

@Builder
public record ArendeDto(
    String id,
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title,
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    String description,
    @Pattern(regexp = "^(MAINTENANCE|DAMAGE|UTILITY|SECURITY|PEST|WEATHER|OTHER)$", message = "Type must be either MAINTENANCE, DAMAGE, UTILITY, SECURITY, PEST, WEATHER or OTHER")
    String type,
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", message = "Priority must be either LOW, MEDIUM, HIGH or CRITICAL")
    String priority,
    @Pattern(regexp = "^(NEW|INVESTIGATING|IN_PROGRESS|RESOLVED|CLOSED)$", message = "Status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSED")
    String status,
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$",
      message = "Reported by must contain only letters, numbers and spaces")
    @Size(min = 3, max = 100, message = "AssignedTo must be between 3 and 100 characters")
    String reportedBy,
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$",
    message = "Reported by must contain only letters, numbers and spaces")
    @Size(min = 3, max = 100, message = "AssignedTo must be between 3 and 100 characters")
    String assignedTo,
    String location,
    @Min(value = 0, message = "Estimated cost cannot be negative")
    @Max(value = 999999999, message = "Estimated cost is too high")
    Integer estimatedCost,
    @Min(value = 0, message = "Estimated cost cannot be negative")
    @Max(value = 999999999, message = "Estimated cost is too high")
    Integer actualCost,
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
