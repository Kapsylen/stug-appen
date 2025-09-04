package stugapi.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import stugapi.application.domain.model.Kontakt;
import stugapi.infrastructure.entities.KontaktEntity;
import stugapi.infrastructure.repositories.KontaktRepository;
import stugapi.presentation.dto.KontaktDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
public class KontaktServiceTest {

  @InjectMocks
  private KontaktService kontaktService;

  @Mock
  private KontaktRepository kontaktRepository;

  @Test
  public void whenSaveKontakt_thenKontaktIsSaved() {
    UUID id = UUID.randomUUID();

    KontaktEntity kontaktEntity = KontaktEntity.builder()
      .id(id)
      .name("Erik Andersson")
      .category("Electrician")
      .phone("+46-70-123-4567")
      .email("erik@anderssonelectrical.se")
      .address("Verkstadsgatan 12, 12345 Stockholm")
      .notes("Licensed Electrician, Solar Panel and EV Charging Installation certified. Rate: 650 SEK/h. Rating: 4.8/5")
      .status(KontaktEntity.Status.ACTIVE)
      .build();

    KontaktDto kontaktDto = KontaktDto.fromkontakt(Kontakt.fromKontaktEntity(kontaktEntity).build());

    when(kontaktRepository.save(any(KontaktEntity.class))).thenReturn(kontaktEntity);

    Kontakt kontakt = kontaktService.saveKontakt(kontaktDto);

    assertEquals(kontaktEntity.getName(), kontakt.name());
    assertEquals(kontaktEntity.getCategory(), kontakt.category());
    assertEquals(kontaktEntity.getPhone(), kontakt.phone());
    assertEquals(kontaktEntity.getEmail(), kontakt.email());
    assertEquals(kontaktEntity.getAddress(), kontakt.address());

    verify(kontaktRepository, times(1)).save(any(KontaktEntity.class));
    verifyNoMoreInteractions(kontaktRepository);
  }

  @Test
  public void whenDeleteExistingKontakt_thenKontaktIsDeleted() {
    UUID id = UUID.randomUUID();

    doNothing().when(kontaktRepository).deleteById(id);

    kontaktService.delete(id.toString());

    verify(kontaktRepository, times(1)).deleteById(id);
    verifyNoMoreInteractions(kontaktRepository);

  }

  @Test
  public void whenUpdateExistingKontakt_thenKontaktIsUpdated() {
    UUID id = UUID.randomUUID();

    KontaktEntity savedKontaktEntity = KontaktEntity.builder()
      .id(id)
      .name("Erik Andersson")
      .category("Electrician")
      .phone("+46-70-123-4567")
      .email("erik@anderssonelectrical.se")
      .address("Verkstadsgatan 12, 12345 Stockholm")
      .notes("Licensed Electrician, Solar Panel and EV Charging Installation certified. Rate: 650 SEK/h. Rating: 4.8/5")
      .status(KontaktEntity.Status.ACTIVE)
      .build();

    KontaktDto updateFakturaDto = KontaktDto.builder()
      .id(id)
      .name("Erik Andersson")
      .category("Electrician")
      .phone("+46-70-123-4567")
      .email("erik@anderssonelectrical.se")
      .address("Verkstadsgatan 12, 12345 Stockholm")
      .notes("Licensed Electrician, Solar Panel and EV Charging Installation certified. Rate: 650 SEK/h. Rating: 4.8/5")
      .status(KontaktEntity.Status.INACTIVE.name())
      .build();

    KontaktEntity updatedKontaktEntity = KontaktEntity.fromKontakt(Kontakt.fromKontaktDto(updateFakturaDto).build()).build();

    when(kontaktRepository.findById(id)).thenReturn(java.util.Optional.of(savedKontaktEntity));
    when(kontaktRepository.save(any(KontaktEntity.class))).thenReturn(updatedKontaktEntity);
    Kontakt updatedKontakt = kontaktService.update(id.toString(), updateFakturaDto);

    assertEquals(updatedKontaktEntity.getName(), updatedKontakt.name());
    assertEquals(updatedKontaktEntity.getCategory(), updatedKontakt.category());
    assertEquals(updatedKontaktEntity.getPhone(), updatedKontakt.phone());
    assertEquals(updatedKontaktEntity.getEmail(), updatedKontakt.email());
    assertEquals(updatedKontaktEntity.getAddress(), updatedKontakt.address());
    assertEquals(updatedKontaktEntity.getNotes(), updatedKontakt.notes());
    assertEquals(updatedKontaktEntity.getStatus().name(), updatedKontakt.status().name());

    verify(kontaktRepository, times(1)).findById(id);
    verify(kontaktRepository, times(1)).save(any(KontaktEntity.class));
  }

}
