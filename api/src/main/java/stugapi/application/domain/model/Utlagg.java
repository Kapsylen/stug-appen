package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.UtlaggEntity;
import stugapi.presentation.dto.UtlaggDto;

@Builder
public record Utlagg (
  String id,
  String title,
  String description,
  String createDate,
  String price
){
  public static UtlaggBuilder fromUtlaggEntity(UtlaggEntity utlagg) {
    return Utlagg.builder()
      .id(utlagg.getId().toString())
      .title(utlagg.getTitle())
      .description(utlagg.getDescription())
      .createDate(utlagg.getCreateDate())
      .price(utlagg.getPrice());
  }

  public static UtlaggBuilder fromUtlaggDto(UtlaggDto utlaggDto) {
    return Utlagg.builder()
      .title(utlaggDto.title())
      .description(utlaggDto.description())
      .createDate(utlaggDto.createDate())
      .price(utlaggDto.price());
  }
}
