package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stugapi.application.domain.model.FakturaEnhet;

import java.util.UUID;

@Entity
@Table(name = "faktura_enhet")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FakturaEnhetEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String quantity;

  @Column(nullable = false)
  private String price;

  @Column(nullable = false)
  private String total;

  public static FakturaEnhetEntity toFakturaEnhetEntity(FakturaEnhet item) {
    return FakturaEnhetEntity.builder()
      .description(item.description())
      .quantity(item.quantity())
      .price(item.price())
      .total(item.total())
      .build();
  }
}
