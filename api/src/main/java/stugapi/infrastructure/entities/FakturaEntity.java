package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import stugapi.application.domain.model.Faktura;
import stugapi.application.domain.model.FakturaEnhet;
import stugapi.infrastructure.entities.enums.FakturaStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static stugapi.infrastructure.entities.FakturaEnhetEntity.toFakturaEnhetEntity;

@Entity
@Table(name = "fakturor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FakturaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "invoice_number", nullable = false, unique = true)
  private String invoiceNumber;

  @Column(name = "client_name", nullable = false)
  private String clientName;

  @Column(name = "issue_date", nullable = false)
  private Instant issueDate;

  @Column(name = "due_date", nullable = false)
  private Instant dueDate;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "item_id", referencedColumnName = "id")
  private List<FakturaEnhetEntity> items = new ArrayList<>();

  @Column(name = "total_amount", nullable = false)
  private Double totalAmount;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private FakturaStatus status;

  @PrePersist
  protected void onCreate() {
    issueDate = Instant.now();
  }

  public static FakturaEntityBuilder fromFaktura(Faktura faktura) {
    return FakturaEntity.builder()
      .id(faktura.id() != null ? UUID.fromString(faktura.id()) : null)
      .invoiceNumber(faktura.invoiceNumber() != null ? faktura.invoiceNumber() : "Invoice-number-" + Instant.now())
      .clientName(faktura.clientName())
      .issueDate(faktura.issueDate())
      .dueDate(faktura.dueDate())
      .items(faktura.items() != null
        ? faktura.items().stream().map(FakturaEnhetEntity::toFakturaEnhetEntity).toList() : List.of())
      .totalAmount(subTotalAmount(faktura.items() != null ? faktura.items() : List.of()))
      .status(faktura.status());
  }

  private static Double subTotalAmount(List<FakturaEnhet> fakturaEnheter) {
    return fakturaEnheter.stream().mapToDouble(FakturaEnhet::total).sum();
  }
}
