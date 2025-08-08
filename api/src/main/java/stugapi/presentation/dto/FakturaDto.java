package stugapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import stugapi.application.domain.model.Faktura;
import stugapi.application.domain.model.FakturaEnhet;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a data transfer object for an invoice (Faktura).
 * This class encapsulates the details of an invoice and includes functionality
 * to convert a domain model Faktura to a FakturaDto.
 *
 * Fields:
 * - id: Unique identifier of the invoice.
 * - invoiceNumber: Invoice number associated with the invoice.
 * - clientName: Name of the client related to the invoice.
 * - issueDate: Date when the invoice was issued.
 * - dueDate: Due date of the invoice.
 * - items: List of items (FakturaEnhetDto) associated with the invoice.
 * - totalAmount: Total amount of the invoice.
 * - status: The current status of the invoice as a string.
 *
 * This record uses the Lombok @Builder annotation to facilitate object creation.
 */
@Builder
public record FakturaDto(
    String id,
    String invoiceNumber,
    String clientName,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant issueDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant dueDate,
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
      .items(faktura.items() != null
        ? faktura.items().stream()
        .map(FakturaEnhetDto::toFaktureEnhet)
        .toList()
        : List.of())
      .totalAmount(faktura.totalAmount())
      .status(faktura.status().toString());
  }

  public static FakturaDto toFakturaDto(Faktura faktura) {
    return toFakturaDtoBuilder(faktura).build();
  }
}
