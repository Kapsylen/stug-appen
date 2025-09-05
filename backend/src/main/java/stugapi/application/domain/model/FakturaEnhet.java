package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.FakturaEnhetEntity;
import stugapi.presentation.dto.FakturaEnhetDto;

import java.util.UUID;

@Builder
public record FakturaEnhet(
  UUID id,
  String description,
  Integer quantity,
  Double price,
  Double total
) {

  public static FakturaEnhet toFakturaEnhet(FakturaEnhetDto enhet) {
    return FakturaEnhet.builder()
      .id(enhet.id())
      .description(enhet.description())
      .quantity(enhet.quantity())
      .price(enhet.price())
      .total(enhet.quantity() * enhet.price())
      .build();
  }

  public static FakturaEnhet toFakturaEnhet(FakturaEnhetEntity entity) {
    return FakturaEnhet.builder()
      .id(entity.getId())
      .description(entity.getDescription())
      .quantity(entity.getQuantity())
      .price(entity.getPrice())
      .total(entity.getTotal())
      .build();
  }
}
