package stugapi.presentation.dto;

import ch.qos.logback.classic.spi.LoggingEventVO;
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
    Long id,
    String name,
    String company,
    String category,
    String phone,
    String email,
    String address,
    String notes,
    String status
) {
  public static KontaktDtoBuilder toKontaktDtoBuilder(Kontakt kontakt) {
    return KontaktDto.builder()
      .name(kontakt.name())
      .company(kontakt.company())
      .category(kontakt.category())
      .phone(kontakt.category())
      .address(kontakt.address())
      .notes(kontakt.notes())
      .status(kontakt.status().name());
  }

  public static KontaktDto fromkontakt(Kontakt kontakt) {
    return toKontaktDtoBuilder(kontakt).build();
  }
}
