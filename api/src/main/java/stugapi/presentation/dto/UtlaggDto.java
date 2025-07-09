package stugapi.presentation.dto;

import lombok.Builder;
import stugapi.application.domain.model.Utlagg;

@Builder
public record UtlaggDto(
  String id,
  String title,
  String description,
  String outlayDate,
  String price
) {
  public static UtlaggDtoBuilder utlaggDtoBuilder(Utlagg utlagg) {
    return UtlaggDto.builder()
      .id(utlagg.id())
      .title(utlagg.title())
      .description(utlagg.description())
      .outlayDate(utlagg.outlayDate())
      .price(utlagg.price());
  }

  public static UtlaggDto fromUtlagg(Utlagg utlagg) {
    return UtlaggDto.utlaggDtoBuilder(utlagg).build();
  }
}
