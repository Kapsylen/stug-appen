package stugapi.infrastructure.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import stugapi.application.domain.model.Utlagg;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "utlagg")
public class UtlaggEntity {

  @Id
  UUID id;
  String title;
  String description;
  String createDate;
  String price;

  public static UtlaggEntityBuilder fromUtlagg(Utlagg utlagg) {
    return UtlaggEntity.builder()
      .id(UUID.fromString(utlagg.id()))
      .title(utlagg.title())
      .description(utlagg.description())
      .createDate(utlagg.createDate())
      .price(utlagg.price());
  }
}
