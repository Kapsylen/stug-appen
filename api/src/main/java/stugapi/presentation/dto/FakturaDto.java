package stugapi.presentation.dto;

import lombok.Builder;
import stugapi.application.domain.model.Faktura;

import java.util.List;

@Builder
public record FakturaDto(
    String id,
    String invoiceNumber,
    String clientName,
    String issueDate,
    String dueDate,
    List<FakturaEnhetDto> items,
    String totalAmount,
    String status
) {
  public static FakturaDtoBuilder toFakturaDtoBuilder(Faktura faktura) {
    return FakturaDto.builder()
      .id(faktura.id())
      .invoiceNumber(faktura.invoiceNumber())
      .clientName(faktura.clientName())
      .issueDate(faktura.issueDate())
      .dueDate(faktura.dueDate())
      .totalAmount(faktura.totalAmount())
      .status(faktura.status().toString());
  }

  public static FakturaDto toFakturaDto(Faktura faktura) {
    return toFakturaDtoBuilder(faktura).build();
  }
}
