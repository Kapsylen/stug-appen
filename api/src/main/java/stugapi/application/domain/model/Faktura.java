package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.FakturaEntity;
import stugapi.infrastructure.entities.enums.FakturaStatus;
import stugapi.presentation.dto.FakturaDto;
import stugapi.presentation.dto.FakturaEnhetDto;

import java.time.Instant;
import java.util.List;

@Builder
public record Faktura(
  String id,
  String invoiceNumber,
  String clientName,
  Instant issueDate,
  Instant dueDate,
  List<FakturaEnhet> items,
  Double totalAmount,
  FakturaStatus status
) {
  public static FakturaBuilder fromFakturaDto(FakturaDto fakturaDto) {
    return Faktura.builder()
      .id(fakturaDto.id())
      .invoiceNumber(fakturaDto.invoiceNumber())
      .clientName(fakturaDto.clientName())
      .issueDate(fakturaDto.issueDate())
      .dueDate(fakturaDto.dueDate())
      .items(fakturaDto.items() != null ? fakturaDto.items()
        .stream()
        .map(FakturaEnhet::toFakturaEnhet)
        .toList() : List.of()
      )
      .status(FakturaStatus.valueOf(fakturaDto.status()));
  }

  public static FakturaBuilder fromFakturaEntity(FakturaEntity fakturaEntity) {
    return Faktura.builder()
      .id(fakturaEntity.getId().toString())
      .invoiceNumber(fakturaEntity.getInvoiceNumber())
      .clientName(fakturaEntity.getClientName())
      .issueDate(fakturaEntity.getIssueDate())
      .dueDate(fakturaEntity.getDueDate())
      .items(fakturaEntity.getItems() != null ? fakturaEntity.getItems()
        .stream()
        .map(FakturaEnhet::toFakturaEnhet)
        .toList() : List.of())
      .totalAmount(fakturaEntity.getTotalAmount())
      .status(fakturaEntity.getStatus());
  }
}
