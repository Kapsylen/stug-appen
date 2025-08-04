package stugapi.application.domain.model;

import lombok.Builder;
import stugapi.infrastructure.entities.KontaktEntity;
import stugapi.presentation.dto.KontaktDto;

@Builder
public record Kontakt(
    String id,
    String name,
    String company,
    String category,
    String phone,
    String email,
    String address,
    String notes,
    KontaktStatus status
) {

    /**
     * Creates a builder instance from a KontaktDto
     *
     * @param kontaktDto The DTO to convert
     * @return A builder instance populated with DTO data
     */
    public static KontaktBuilder fromKontaktDto(KontaktDto kontaktDto) {
        return Kontakt.builder()
            .name(kontaktDto.name())
            .company(kontaktDto.company())
            .category(kontaktDto.category())
            .phone(kontaktDto.phone())
            .email(kontaktDto.email())
            .address(kontaktDto.address())
            .notes(kontaktDto.notes())
            .status(KontaktStatus.fromString(kontaktDto.status()));
    }

  public static KontaktBuilder fromKontaktEntity(KontaktEntity kontaktEntity) {
    return Kontakt.builder()
      .id(kontaktEntity.getId().toString())
      .company(kontaktEntity.getCompany())
      .category(kontaktEntity.getCategory())
      .phone(kontaktEntity.getPhone())
      .email(kontaktEntity.getEmail())
      .address(kontaktEntity.getAddress())
      .notes(kontaktEntity.getNotes())
      .status(KontaktStatus.valueOf(kontaktEntity.getStatus().toString()));
  }

  public enum KontaktStatus {
        ACTIVE, INACTIVE;

        public static KontaktStatus fromString(String status) {
            return valueOf(status.toUpperCase());
        }
    }
}
