package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.ArendeStatusEntity;
import stugapi.presentation.dto.ArendeStatusDto;

import java.time.Instant;

@Builder
public record ArendeStatus(
    String id,
    Instant timestamp,
    String message,
    String updatedBy,
    String status // Consider using an enum like 'Status' if applicable
) {
  public static ArendeStatus toArendeStatus(ArendeStatusDto arendeStatusDto, String status) {
    return ArendeStatus.builder()
      .id(arendeStatusDto.id())
      .timestamp(arendeStatusDto.timestamp())
      .message(arendeStatusDto.message())
      .updatedBy(arendeStatusDto.updatedBy())
      .status(status)
      .build();
  }

  public static ArendeStatus toArendeStatus(ArendeStatusDto arendeStatusDto) {
    return ArendeStatus.builder()
      .id(arendeStatusDto.id())
      .timestamp(arendeStatusDto.timestamp())
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
