package stugapi.presentation.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import stugapi.application.domain.model.Kontakt;

/**
 * Data Transfer Object (DTO) representing a contact.
 *
 * @param id Unique identifier for the contact
 * @param name Name of the contact person
 * @param company Company or organization name
 * @param category Contact category or classification
 * @param phone Contact phone number
 * @param email Contact email address
 * @param address Physical or mailing address
 * @param notes Additional notes or comments about the contact
 * @param status Current status of the contact (Active/Inactive)
 */
@Builder
public record KontaktDto(
    String id,
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[\\p{L}\\s.-]+$", message = "Name can only contain letters, spaces, dots, and hyphens")
    String name,
    @NotBlank(message = "Company is required")
    String company,
    String category,
    @Pattern(regexp = "^$|^\\+?[0-9\\s-()]{8,20}$", message = "Phone number can only contain numbers, spaces, hyphens, parentheses, and optional '+' prefix")
    String phone,
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 254, message = "Email must not exceed 254 characters")
    String email,
    String address,
    String notes,
    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Status must be either ACTIVE or INACTIVE")
    String status
) {
  public static KontaktDtoBuilder toKontaktDtoBuilder(Kontakt kontakt) {
    return KontaktDto.builder()
      .id(kontakt.id())
      .name(kontakt.name())
      .company(kontakt.company())
      .category(kontakt.category())
      .phone(kontakt.phone())
      .email(kontakt.email())
      .address(kontakt.address())
      .notes(kontakt.notes())
      .status(kontakt.status().name());
  }

  public static KontaktDto fromkontakt(Kontakt kontakt) {
    return toKontaktDtoBuilder(kontakt).build();
  }
}
