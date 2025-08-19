package stugapi.presentation.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import stugapi.application.domain.model.FakturaEnhet;

@Builder
public record FakturaEnhetDto(
    String id,
    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    String description,
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    @Max(value = 9999999999L, message = "Quantity must have at most 10 digits")
    Integer quantity,
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    @DecimalMax(value = "999999999.99", message = "Price cannot exceed 999,999,999.99")
    @Digits(integer = 9, fraction = 2, message = "Price must have at most 9 digits before decimal point and 2 decimal places")
    Double price,
    Double total
) {
  public static FakturaEnhetDto toFaktureEnhet(FakturaEnhet item) {
    return FakturaEnhetDto.builder()
      .id(item.id())
      .description(item.description())
      .quantity(item.quantity())
      .price(item.price())
      .total(item.quantity() * item.price())
      .build();
  }
}
