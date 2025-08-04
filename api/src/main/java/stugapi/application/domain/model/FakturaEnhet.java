package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.FakturaEnhetEntity;
import stugapi.presentation.dto.FakturaEnhetDto;

@Builder
public record FakturaEnhet(
    String id,
    String description,
    String quantity,
    String price,
    String total
) {

  public static FakturaEnhetBuilder toFakturaEnhetBuilder(FakturaEnhetDto enhet) {
    return FakturaEnhet.builder()
      .description(enhet.description())
      .quantity(enhet.quantity())
      .price(enhet.price())
      .total(enhet.total());
  }

  public static FakturaEnhet toFakturaEnhet(FakturaEnhetDto enhet) {
    return FakturaEnhet.builder()
      .description(enhet.description())
      .quantity(enhet.quantity())
      .price(enhet.price())
      .total(enhet.total())
      .build();
  }

  public static FakturaEnhet toFakturaEnhet(FakturaEnhetEntity entity) {
    return FakturaEnhet.builder()
      .id(entity.getId().toString())
      .description(entity.getDescription())
      .quantity(entity.getQuantity())
      .price(entity.getPrice())
      .total(entity.getTotal())
      .build();
  }
}
