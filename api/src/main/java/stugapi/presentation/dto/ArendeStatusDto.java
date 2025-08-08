package stugapi.presentation.dto;

import lombok.Builder;
import stugapi.application.domain.model.ArendeStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
public record ArendeStatusDto(
    String id,
    Instant timestamp,
    String message,
    String updatedBy,
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
