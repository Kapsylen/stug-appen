package stugapi.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import stugapi.application.domain.model.ArendeStatus;

import java.time.Instant;

@Builder
public record ArendeStatusDto(
    String id,
    Instant timestamp,
    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    String message,
    @NotNull(message = "UpdatedBy is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$",
      message = "UpdatedBy must contain only letters, numbers and spaces")
    @Size(min = 3, max = 30, message = "UpdatedBy must be between 3 and 30 characters")
    String updatedBy,
    @NotNull(message = "Arende status is required")
    @Pattern(regexp = "^(NEW|INVESTIGATING|IN_PROGRESS|RESOLVED|CLOSE)$", message = "Arende status must be either NEW, INVESTIGATING, IN_PROGRESS, RESOLVED or CLOSE")
    String status
) {
  public static ArendeStatusDto toArendeStatusDto (ArendeStatus arendeStatus){
    return ArendeStatusDto.builder()
      .id(arendeStatus.id())
      .timestamp(arendeStatus.timestamp())
      .message(arendeStatus.message())
      .updatedBy(arendeStatus.updatedBy())
      .status(arendeStatus.status())
      .build();
  }
}
