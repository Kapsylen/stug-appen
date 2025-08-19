package stugapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import stugapi.application.domain.model.Utlagg;

import java.time.Instant;

/**
 * Represents a data transfer object for "Utlagg" (an outlay or expense).
 * This class serves as a structured representation of the details of an expense
 * and provides functionality to map a domain model Utlagg to an UtlaggDto.
 *
 * Fields:
 * - id: Unique identifier for the expense.
 * - title: Title or name of the expense.
 * - description: Detailed description of the expense.
 * - outlayDate: Date when the expense occurred.
 * - price: Cost or value of the expense.
 *
 * Features:
 * - Utilizes the Lombok @Builder annotation to enable the creation of instances in a flexible manner.
 * - Includes static utility methods to facilitate conversion from Utlagg domain objects.
 */
@Builder
public record UtlaggDto(
  String id,
  @NotNull(message = "Title is required")
  @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
  String title,
  @NotNull(message = "Description is required")
  @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
  String description,
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @NotNull(message = "Outlay date is required")
  @PastOrPresent(message = "Outlay date cannot be in the future")
  Instant outlayDate,
  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
  @DecimalMax(value = "999999999.99", message = "Price cannot exceed 999,999,999.99")
  @Digits(integer = 9, fraction = 2, message = "Price must have at most 9 digits before decimal point and 2 decimal places")
  Double price
) {
  public static UtlaggDtoBuilder toUtlaggDtoBuilder(Utlagg utlagg) {
    return UtlaggDto.builder()
      .id(utlagg.id())
      .title(utlagg.title())
      .description(utlagg.description())
      .outlayDate(utlagg.outlayDate())
      .price(utlagg.price());
  }

  public static UtlaggDto fromUtlagg(Utlagg utlagg) {
    return UtlaggDto.toUtlaggDtoBuilder(utlagg).build();
  }
}
