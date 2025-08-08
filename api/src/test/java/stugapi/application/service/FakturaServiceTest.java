package stugapi.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import stugapi.application.domain.model.Faktura;
import stugapi.infrastructure.entities.FakturaEnhetEntity;
import stugapi.infrastructure.entities.FakturaEntity;
import stugapi.infrastructure.entities.enums.FakturaStatus;
import stugapi.infrastructure.repositories.FakturaRepository;
import stugapi.presentation.dto.FakturaDto;
import stugapi.presentation.dto.FakturaEnhetDto;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DataJpaTest
public class FakturaServiceTest {

  @InjectMocks
  private FakturaService fakturaService;

  @Mock
  private FakturaRepository fakturaRepository;

  @Test
  void whenSaveFaktura_thenFakturaIsSaved() {
    UUID id = UUID.randomUUID();
    Instant issueDate = Instant.now();
    Instant duedate = Instant.now().plus(Period.ofDays(30));
    FakturaEntity savedFakturaEntity = FakturaEntity.builder()
      .id(id)
      .invoiceNumber("STG-2025-001")
      .clientName("Summer Family Rentals")
      .issueDate(issueDate)
      .dueDate(duedate)
      .items(
        List.of(
          FakturaEnhetEntity.builder()
            .id(UUID.randomUUID())
            .description("Cottage Rental - High Season (2 weeks)")
            .quantity("14")
            .price("1200")
            .total("16800")
            .build(),
          FakturaEnhetEntity.builder()
            .id(UUID.randomUUID())
            .description("Final Cleaning Service")
            .quantity("1")
            .price("1500")
            .total("1500")
            .build()))
      .totalAmount("18300")
      .status(FakturaStatus.PAID)
      .build();

    FakturaDto fakturaDto = FakturaDto.toFakturaDto(Faktura.fromFakturaEntity(savedFakturaEntity).build());

    when(fakturaRepository.save(any(FakturaEntity.class))).thenReturn(savedFakturaEntity);

    Faktura faktura = fakturaService.saveFaktura(fakturaDto);

    assertEquals(savedFakturaEntity.getInvoiceNumber(), faktura.invoiceNumber());
    assertEquals(savedFakturaEntity.getClientName(), faktura.clientName());
    assertEquals(savedFakturaEntity.getIssueDate(), faktura.issueDate());
    assertEquals(savedFakturaEntity.getDueDate(), faktura.dueDate());
    assertEquals(savedFakturaEntity.getTotalAmount(), faktura.totalAmount());
    assertEquals(savedFakturaEntity.getStatus(), faktura.status());
    assertEquals(savedFakturaEntity.getItems().size(), faktura.items().size());
    assertEquals(savedFakturaEntity.getItems().getFirst().getDescription(), faktura.items().getFirst().description());
    assertEquals(savedFakturaEntity.getItems().getFirst().getPrice(), faktura.items().getFirst().price());
    assertEquals(savedFakturaEntity.getItems().getFirst().getQuantity(), faktura.items().getFirst().quantity());
    assertEquals(savedFakturaEntity.getItems().getFirst().getTotal(), faktura.items().getFirst().total());
    assertEquals(savedFakturaEntity.getItems().getLast().getDescription(), faktura.items().getLast().description());
    assertEquals(savedFakturaEntity.getItems().getLast().getPrice(), faktura.items().getLast().price());
    assertEquals(savedFakturaEntity.getItems().getLast().getQuantity(), faktura.items().getLast().quantity());
    assertEquals(savedFakturaEntity.getItems().getLast().getTotal(), faktura.items().getLast().total());

    assertEquals(savedFakturaEntity.getId(), UUID.fromString(faktura.id()));

    verify(fakturaRepository).save(any(FakturaEntity.class));
    verifyNoMoreInteractions(fakturaRepository);
  }

  @Test
  void whenDeleteExistingFaktura_thenFakturaIsDeleted() {
    UUID id = UUID.randomUUID();

    doNothing().when(fakturaRepository).deleteById(id);

    fakturaService.delete(id.toString());

    verify(fakturaRepository, times(1)).deleteById(id);
    verifyNoMoreInteractions(fakturaRepository);
  }

  @Test
  void whenUpdateExistingFaktura_thenFakturaIsUpdated() {
    UUID id = UUID.randomUUID();
    Instant issueDate = Instant.now();
    Instant duedate = Instant.now().plus(Period.ofDays(30));
    FakturaEntity savedFakturaEntity = FakturaEntity.builder()
      .id(id)
      .clientName("Summer Family Rentals")
      .issueDate(issueDate)
      .dueDate(duedate)
      .items(
        List.of(
          FakturaEnhetEntity.builder()
            .id(UUID.randomUUID())
            .description("Cottage Rental - High Season (2 weeks)")
            .quantity("14")
            .price("1200")
            .total("16800")
            .build(),
          FakturaEnhetEntity.builder()
            .id(UUID.randomUUID())
            .description("Final Cleaning Service")
            .quantity("1")
            .price("1500")
            .total("1500")
            .build()))
      .totalAmount("18300")
      .status(FakturaStatus.SENT)
      .build();

    FakturaDto updateFakturaDto = FakturaDto.builder()
      .id(id.toString())
      .clientName("Summer Family Rentals")
      .issueDate(issueDate)
      .dueDate(duedate)
      .items(
        List.of(
          FakturaEnhetDto.builder()
            .id(UUID.randomUUID().toString())
            .description("Cottage Rental - High Season (2 weeks)")
            .quantity("14")
            .price("1200")
            .total("16800")
            .build(),
          FakturaEnhetDto.builder()
            .id(UUID.randomUUID().toString())
            .description("Final Cleaning Service")
            .quantity("2")
            .price("1500")
            .total("3000")
            .build()))
      .totalAmount("19800")
      .status(FakturaStatus.PAID.name())
      .build();

    FakturaEntity updatedFakturaEntity = FakturaEntity.fromFaktura(Faktura.fromFakturaDto(updateFakturaDto).build()).build();

    // Given

    when(fakturaRepository.findById(id)).thenReturn(java.util.Optional.of(savedFakturaEntity));
    when(fakturaRepository.save(any(FakturaEntity.class))).thenReturn(updatedFakturaEntity);

    // When

    Faktura updatedFaktura = fakturaService.update(id.toString(), updateFakturaDto);

    // Then

    assertNotNull(updatedFakturaEntity.getInvoiceNumber());
    assertEquals(updatedFakturaEntity.getClientName(), updatedFaktura.clientName());
    assertEquals(updatedFakturaEntity.getIssueDate(), updatedFaktura.issueDate());
    assertEquals(updatedFakturaEntity.getDueDate(), updatedFaktura.dueDate());
    assertEquals(updatedFakturaEntity.getTotalAmount(), updatedFaktura.totalAmount());
    assertEquals(updatedFakturaEntity.getStatus(), updatedFaktura.status());
    assertEquals(updatedFakturaEntity.getItems().size(), updatedFaktura.items().size());
    assertEquals(updatedFakturaEntity.getItems().getFirst().getDescription(), updatedFaktura.items().getFirst().description());
    assertEquals(updatedFakturaEntity.getItems().getFirst().getPrice(), updatedFaktura.items().getFirst().price());
    assertEquals(updatedFakturaEntity.getItems().getFirst().getQuantity(), updatedFaktura.items().getFirst().quantity());
    assertEquals(updatedFakturaEntity.getItems().getFirst().getTotal(), updatedFaktura.items().getFirst().total());
    assertEquals(updatedFakturaEntity.getItems().getLast().getDescription(), updatedFaktura.items().getLast().description());
    assertEquals(updatedFakturaEntity.getItems().getLast().getPrice(), updatedFaktura.items().getLast().price());
    assertEquals(updatedFakturaEntity.getItems().getLast().getQuantity(), updatedFaktura.items().getLast().quantity());
    assertEquals(updatedFakturaEntity.getItems().getLast().getTotal(), updatedFaktura.items().getLast().total());

    verify(fakturaRepository, times(1)).findById(id);
    verify(fakturaRepository, times(1)).save(any(FakturaEntity.class));
    verifyNoMoreInteractions(fakturaRepository);
  }

}
