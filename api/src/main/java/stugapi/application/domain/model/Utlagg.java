package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.UtlaggEntity;
import stugapi.presentation.dto.UtlaggDto;

import java.time.Instant;
import java.util.UUID;

@Builder
public record Utlagg (
  UUID id,
  String title,
  String description,
  Instant outlayDate,
  Double price
){
  public static UtlaggBuilder fromUtlaggEntity(UtlaggEntity utlagg) {
    return Utlagg.builder()
      .id(utlagg.getId())
      .title(utlagg.getTitle())
      .description(utlagg.getDescription())
      .outlayDate(utlagg.getOutlayDate())
      .price(utlagg.getPrice());
  }

  public static UtlaggBuilder fromUtlaggDto(UtlaggDto utlaggDto) {
    return Utlagg.builder()
      .id(utlaggDto.id())
      .title(utlaggDto.title())
      .description(utlaggDto.description())
      .outlayDate(utlaggDto.outlayDate())
      .price(utlaggDto.price());
  }
}
