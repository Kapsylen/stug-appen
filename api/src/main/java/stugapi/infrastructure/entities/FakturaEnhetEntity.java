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
  private Integer quantity;

  @Column(nullable = false)
  private Double price;

  @Column(nullable = false)
  private Double total;

  public static FakturaEnhetEntity toFakturaEnhetEntity(FakturaEnhet item) {
    return FakturaEnhetEntity.builder()
      .id(item.id() != null ? UUID.fromString(item.id()) : null)
      .description(item.description())
      .quantity(item.quantity())
      .price(item.price())
      .total(item.quantity() * item.price())
      .build();
  }
}
