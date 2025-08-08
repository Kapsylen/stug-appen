package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stugapi.application.domain.model.Kontakt;

import java.util.UUID;

@Entity
@Table(name = "kontakter")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KontaktEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String company;

    private String category;

    private String phone;

    @Column(unique = true)
    private String email;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }

    public static KontaktEntityBuilder fromKontakt(Kontakt kontakt) {
        return KontaktEntity.builder()
                .id(kontakt.id() != null ? UUID.fromString(kontakt.id()) : null)
                .name(kontakt.name())
                .company(kontakt.company())
                .category(kontakt.category())
                .phone(kontakt.phone())
                .email(kontakt.email())
                .address(kontakt.address())
                .notes(kontakt.notes())
                .status(Status.valueOf(kontakt.status().name()));
    }
}
