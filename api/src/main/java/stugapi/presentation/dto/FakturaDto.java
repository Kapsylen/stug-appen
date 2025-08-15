package stugapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Client name is required")
    @Size(min = 2, max = 100, message = "Client name must be between 2 and 100 characters")
    String clientName,

    @NotNull(message = "Issue date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant issueDate,

    @NotNull(message = "Due date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant dueDate,

    @NotNull(message = "Items list cannot be null")
    @Size(min = 1, message = "At least one item is required")
    @Valid
    List<FakturaEnhetDto> items,

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be greater than or equal to 0")
    @DecimalMax(value = "999999999.99", message = "Total amount cannot exceed 999,999,999.99")
    @Digits(integer = 9, fraction = 2, message = "Total amount must have at most 9 digits before decimal point and 2 decimal places")
    Double totalAmount,

    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(PAID|OVERDUE|SENT|DRAFT)$", message = "Status must be either PAID, OVERDUE, SENT or DRAFT")
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
