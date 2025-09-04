package stugapi.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import stugapi.application.domain.model.Utlagg;
import stugapi.infrastructure.entities.UtlaggEntity;
import stugapi.infrastructure.repositories.UtlaggRepository;
import stugapi.presentation.dto.UtlaggDto;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
public class UtlaggServiceTest {

  @InjectMocks
  private UtlaggService utlaggService;

  @Mock
  private UtlaggRepository utlaggRepository;

  @Test
  void whenSaveUtlagg_thenUtlaggIsSaved() {
    UUID id = UUID.randomUUID();

    UtlaggEntity utlaggEntity = UtlaggEntity.builder()
      .id(id)
      .title("Electricity")
      .outlayDate(Instant.now())
      .description("new kitchen wiring")
      .build();

    UtlaggDto utlaggDto = UtlaggDto.fromUtlagg(Utlagg.fromUtlaggEntity(utlaggEntity).id(id).build());

    when(utlaggRepository.save(any(UtlaggEntity.class))).thenReturn(utlaggEntity);

    Utlagg utlagg = utlaggService.saveUtlagg(utlaggDto);

    assertEquals(utlaggEntity.getTitle(), utlagg.title());
    assertEquals(utlaggEntity.getDescription(), utlagg.description());
    assertEquals(utlaggEntity.getOutlayDate(), utlagg.outlayDate());
    assertEquals(utlaggEntity.getId(), utlagg.id());
  }

  @Test
  void whenDeleteExistingUtlagg_thenUtlaggIsDeleted() {
    UUID id = UUID.randomUUID();

    doNothing().when(utlaggRepository).deleteById(id);

    utlaggService.delete(id.toString());

    verify(utlaggRepository, times(1)).deleteById(id);
    verifyNoMoreInteractions(utlaggRepository);
  }

  @Test
  void whenUpdateExistingUtlagg_thenUtlaggIsUpdated() {
    UUID id = UUID.randomUUID();
    UtlaggEntity savedUtlaggEntity = UtlaggEntity.builder()
      .id(id)
      .title("Electricity")
      .outlayDate(Instant.now())
      .description("new kitchen wiring")
      .build();

    UtlaggDto updateUtlaggDto = UtlaggDto.builder()
      .id(id)
      .title("Gardening")
      .outlayDate(savedUtlaggEntity.getOutlayDate())
      .description("lawn maintenance")
      .price(1500.00)
      .build();

    UtlaggEntity updatedUtlaggEntity = UtlaggEntity.fromUtlagg(Utlagg.fromUtlaggDto(updateUtlaggDto).build()).id(id).build();

    when(utlaggRepository.findById(id)).thenReturn(java.util.Optional.of(savedUtlaggEntity));
    when(utlaggRepository.save(any(UtlaggEntity.class))).thenReturn(updatedUtlaggEntity);

    Utlagg updatedUtlagg = utlaggService.update(id, updateUtlaggDto);

    assertEquals(updatedUtlaggEntity.getTitle(), updatedUtlagg.title());
    assertEquals(updatedUtlaggEntity.getDescription(), updatedUtlagg.description());
    assertEquals(updatedUtlaggEntity.getOutlayDate(), updatedUtlagg.outlayDate());
    assertEquals(updatedUtlaggEntity.getId(), updatedUtlagg.id());
  }
}
