package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import stugapi.application.domain.model.Faktura;
import stugapi.infrastructure.entities.enums.FakturaStatus;

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
    private String issueDate;

    @Column(name = "due_date", nullable = false)
    private String dueDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private List<FakturaEnhetEntity> items = new ArrayList<>();

    @Column(name = "total_amount", nullable = false)
    private String totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FakturaStatus status;

    public static FakturaEntityBuilder fromFaktura(Faktura faktura) {
        return FakturaEntity.builder()
                .invoiceNumber("Invoice-number-" + LocalDateTime.now())
                .clientName(faktura.clientName())
                .issueDate(faktura.issueDate())
                .dueDate(faktura.dueDate())
                .items(faktura.items() != null
                  ? faktura.items().stream().map(FakturaEnhetEntity::toFakturaEnhetEntity).toList(): List.of())
                .totalAmount(faktura.totalAmount())
                .status(faktura.status());
    }
}
