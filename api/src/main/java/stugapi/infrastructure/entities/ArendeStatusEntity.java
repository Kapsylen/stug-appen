package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stugapi.application.domain.model.ArendeStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArendeStatusEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String message;

  @Column(nullable = false)
  private String updatedBy;

  @Column(nullable = false)
  private String status;

  public static ArendeStatusEntity toArendeStatusEntity(ArendeStatus arendeStatus) {
    return ArendeStatusEntity.builder()
      .timestamp(arendeStatus.timestamp())
      .message(arendeStatus.message())
      .updatedBy(arendeStatus.updatedBy())
      .status(arendeStatus.status())
      .build();
  }
}
