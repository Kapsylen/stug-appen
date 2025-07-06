package stugapi.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import stugapi.application.domain.model.Utlagg;
import stugapi.infrastructure.entities.UtlaggEntity;
import stugapi.infrastructure.repositories.UtlaggRepository;
import stugapi.presentation.dto.UtlaggDto;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UtlaggService {

  private final UtlaggRepository utlaggRepository;

  public void save(UtlaggDto utlaggDto) {
    Utlagg utlagg = Utlagg.fromUtlaggDto(utlaggDto).build();
    utlaggRepository.save(UtlaggEntity.fromUtlagg(utlagg).build());
  }

  public void delete(String id) {
    utlaggRepository.deleteById(UUID.fromString(id));
  }

  public Utlagg update(UtlaggDto updateUtlagg, String id) {
    var utlagg = Utlagg.fromUtlaggEntity(utlaggRepository
      .findById(UUID.fromString(id))
      .orElseThrow(RuntimeException::new));

    return utlagg.title(updateUtlagg.title())
      .description(updateUtlagg.description())
      .createDate(updateUtlagg.createDate())
      .price(updateUtlagg.price())
      .build();
  }

  public Utlagg find(String id) {
    return Utlagg.fromUtlaggEntity(utlaggRepository
      .findById(UUID.fromString(id))
      .orElseThrow(RuntimeException::new))
      .build();
  }

  public List<Utlagg> findAll() {
    return utlaggRepository.findAll()
      .stream()
      .map(u -> Utlagg
        .fromUtlaggEntity(u)
        .build())
      .toList();
  }
}
