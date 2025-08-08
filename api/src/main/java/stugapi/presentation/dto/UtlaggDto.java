package stugapi.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  String title,
  String description,
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  Instant outlayDate,
  String price
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
