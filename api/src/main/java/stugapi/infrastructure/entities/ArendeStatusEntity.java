package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stugapi.application.domain.model.ArendeStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "arende_statuses")
public class ArendeStatusEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private Instant timestamp;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String message;

  @Column(nullable = false)
  private String updatedBy;

  @Column(nullable = false)
  private String status;

  public static ArendeStatusEntity toArendeStatusEntity(ArendeStatus arendeStatus) {
    return ArendeStatusEntity.builder()
      .id(arendeStatus.id() != null ? UUID.fromString(arendeStatus.id()) : null)
      .timestamp(arendeStatus.timestamp() != null ? arendeStatus.timestamp() : Instant.now())
      .message(arendeStatus.message())
      .updatedBy(arendeStatus.updatedBy())
      .status(arendeStatus.status())
      .build();
  }
}
