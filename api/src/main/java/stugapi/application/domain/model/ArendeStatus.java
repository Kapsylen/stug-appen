package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.ArendeStatusEntity;
import stugapi.presentation.dto.ArendeStatusDto;
import stugapi.utility.TimeUtility;

import java.time.LocalDateTime;

import static stugapi.utility.TimeUtility.isNullOrEmpty;
import static stugapi.utility.TimeUtility.parseDateTime;

@Builder
public record ArendeStatus(
    String id,
    LocalDateTime timestamp,
    String message,
    String updatedBy,
    String status // Consider using an enum like 'Status' if applicable
) {
  public static ArendeStatus toArendeStatus(ArendeStatusDto arendeStatusDto, String timestamp) {
    return ArendeStatus.builder()
      .id(arendeStatusDto.id())
      .timestamp(isNullOrEmpty(timestamp) ? LocalDateTime.now() : TimeUtility.parseDate(timestamp))
      .message(arendeStatusDto.message())
      .updatedBy(arendeStatusDto.updatedBy())
      .status(arendeStatusDto.status())
      .build();
  }

  public static ArendeStatus toArendeStatus(ArendeStatusEntity arendeStatusEntity) {
    return ArendeStatus.builder()
      .id(arendeStatusEntity.getId().toString())
      .timestamp(arendeStatusEntity.getTimestamp())
      .message(arendeStatusEntity.getMessage())
      .updatedBy(arendeStatusEntity.getUpdatedBy())
      .status(arendeStatusEntity.getStatus())
      .build();
  }
}
