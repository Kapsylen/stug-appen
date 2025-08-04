package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stugapi.application.domain.model.Arende;
import stugapi.infrastructure.entities.enums.Prioritet;
import stugapi.infrastructure.entities.enums.Status;
import stugapi.infrastructure.entities.enums.Typ;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "arenden")
public class ArendeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Typ type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Prioritet priority;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false)
  private String reportedBy;

  private String assignedTo;

  @Column(nullable = false)
  private String location;

  private String estimatedCost;

  private String actualCost;

  @Column(nullable = false)
  private LocalDateTime startTime;

  private LocalDateTime resolvedTime;

  private String resolution;

  @Column(nullable = false)
  private boolean requiresContractor;

  private String contractorInfo;

  @ElementCollection
  @CollectionTable(name = "arende_tags", joinColumns = @JoinColumn(name = "arende_id"))
  @Column(name = "tag")
  private List<String> tags;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "arende_id") // This maps the relationship to the `Arende` entity
  private List<ArendeStatusEntity> updates;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = createdAt;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public static ArendeEntityBuilder fromArende(Arende arende) {
    return ArendeEntity.builder()
      .title(arende.title())
      .description(arende.description())
      .type(arende.type())
      .priority(arende.priority())
      .status(arende.status())
      .reportedBy(arende.reportedBy())
      .assignedTo(arende.assignedTo())
      .location(arende.location())
      .estimatedCost(arende.estimatedCost())
      .actualCost(arende.actualCost())
      .startTime(arende.startTime())
      .resolvedTime(arende.resolvedTime())
      .resolution(arende.resolution())
      .requiresContractor(arende.requiresContractor())
      .contractorInfo(arende.contractorInfo())
      .tags(arende.tags())
      .updates(arende.updates() != null
        ? arende.updates().stream().map(ArendeStatusEntity::toArendeStatusEntity).toList()
        : List.of()
      )
      .createdAt(arende.createdAt())
      .updatedAt(arende.updatedAt())
      .tags(arende.tags());
  }
}
