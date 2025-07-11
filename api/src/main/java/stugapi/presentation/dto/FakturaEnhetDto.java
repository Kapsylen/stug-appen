package stugapi.presentation.dto;

import lombok.Builder;
import stugapi.application.domain.model.FakturaEnhet;

@Builder
public record FakturaEnhetDto(
    String id,
    String description,
    String quantity,
    String price,
    String total
) {
  public static FakturaEnhetDto toFaktureEnhet(FakturaEnhet item) {
    return FakturaEnhetDto.builder()
      .id(item.id())
      .description(item.description())
      .quantity(item.quantity())
      .price(item.price())
      .total(item.total())
      .build();
  }
}
