package stugapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import stugapi.application.domain.model.Arende;
import java.time.Instant;
import java.util.List;

@Builder
public record ArendeDto(
    String id,
    @NotNull(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title,
    @NotNull(message = "Description is required")
    @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
    String description,
    @NotNull(message = "Type is required")
    @Pattern(regexp = "^(MAINTENANCE|DAMAGE|UTILITY|SECURITY|PEST|WEATHER|OTHER)$", message = "Type must be either MAINTENANCE, DAMAGE, UTILITY, SECURITY, PEST, WEATHER or OTHER")
    String type,
    @NotNull(message = "Priority is required")
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", message = "Priority must be either LOW, MEDIUM, HIGH or CRITICAL")
    String priority,
    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(NEW|INVESTIGATING|IN_PROGRESS|RESOLVED|CLOSED)$", message = "Status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSED")
    String status,
    @NotNull(message = "ReportedBy is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$",
      message = "ReportedBy must contain only letters, numbers and spaces")
    @Size(min = 3, max = 30, message = "ReportedBy must be between 3 and 30 characters")
    String reportedBy,
    @NotNull(message = "AssignedTo is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$",
    message = "AssignedTo must contain only letters, numbers and spaces")
    @Size(min = 3, max = 30, message = "AssignedTo must be between 3 and 30 characters")
    String assignedTo,
    String location,
    @NotNull(message = "Estimated cost is required")
    @DecimalMin(value = "0.0", message = "Estimated cost must be greater than or equal to 0")
    @DecimalMax(value = "999999999.99", message = "Estimated cost cannot exceed 999,999,999.99")
    Double estimatedCost,
    @DecimalMin(value = "0.0", message = "Actual cost must be greater than or equal to 0")
    @DecimalMax(value = "999999999.99", message = "Actual cost cannot exceed 999,999,999.99")
    @Digits(integer = 9, fraction = 2, message = "Actual cost must have at most 9 digits before decimal point and 2 decimal places")
    Double actualCost,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant startTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant resolvedTime,
    String resolution,
    boolean requiresContractor,
    String contractorInfo,
    @Valid
    @NotNull(message = "Updates list cannot be null")
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
