package stugapi.infrastructure.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stugapi.application.domain.model.Utlagg;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "utlagg")
public class UtlaggEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;
  String title;
  String description;
  Instant outlayDate;
  Double price;

  public static UtlaggEntityBuilder fromUtlagg(Utlagg utlagg) {
    return UtlaggEntity.builder()
      .title(utlagg.title())
      .description(utlagg.description())
      .outlayDate(utlagg.outlayDate())
      .price(utlagg.price());
  }
}
