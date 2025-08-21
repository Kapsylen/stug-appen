package stugapi.presentation.dto;

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
    @NotNull(message = "Message is required")
    @Size(min= 3, max = 1000, message = "Message must be between 3 and 1000 characters")
    String message,
    @NotNull(message = "UpdatedBy is required")
    @Pattern(regexp = "^[a-zA-Z0-9 ]{3,20}$",
      message = "UpdatedBy must contain only letters, numbers, spaces and between 3 and 20 characters long.")
    String updatedBy,
    String status
) {
  public static ArendeStatusDto toArendeStatusDto (ArendeStatus arendeStatus){
    return ArendeStatusDto.builder()
      .id(arendeStatus.id())
      .timestamp(Instant.now())
      .message(arendeStatus.message())
      .updatedBy(arendeStatus.updatedBy())
      .status(arendeStatus.status())
      .build();
  }
}
