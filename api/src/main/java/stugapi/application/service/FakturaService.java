package stugapi.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import stugapi.application.domain.model.Faktura;
import stugapi.application.domain.model.Faktura.FakturaBuilder;
import stugapi.application.domain.model.FakturaEnhet;
import stugapi.infrastructure.entities.FakturaEnhetEntity;
import stugapi.infrastructure.entities.FakturaEntity;
import stugapi.infrastructure.repositories.FakturaEnhetRepository;
import stugapi.infrastructure.repositories.FakturaRepository;
import stugapi.presentation.dto.FakturaDto;
import stugapi.presentation.dto.FakturaEnhetDto;
import stugapi.presentation.error.FakturaNotFoundException;

import java.util.List;
import java.util.UUID;

import static stugapi.application.domain.model.Faktura.fromFakturaDto;
import static stugapi.application.domain.model.Faktura.fromFakturaEntity;
import static stugapi.infrastructure.entities.FakturaEntity.fromFaktura;

/**
 * The FakturaService class provides methods for managing `Faktura` entities.
 * This includes operations such as creating, updating, retrieving, and deleting invoices.
 * The service acts as a layer between the domain model and the persistence layer.
 */
@Service
@AllArgsConstructor
public class FakturaService {

  private final FakturaRepository fakturaRepository;
  private final FakturaEnhetRepository fakturaEnhetRepository;

  /**
   * Saves a Faktura entity by transforming a FakturaDto into the domain model and then
   * persisting it in the repository. The saved entity is converted back to a Faktura object.
   *
   * @param fakturaDto the FakturaDto object containing the data to be saved
   * @return the saved Faktura object
   */

  public Faktura saveFaktura(FakturaDto fakturaDto) {
   Faktura faktura =  fromFakturaEntity(fakturaRepository
      .save(fromFaktura(fromFakturaDto(fakturaDto)
        .build())
        .build()))
      .build();
   return faktura;

  }

  /**
   * Deletes a Faktura entity with the specified identifier.
   *
   * @param id the unique identifier of the Faktura to delete, represented as a string
   */
  public void delete(String id) {
    fakturaRepository.deleteById(UUID.fromString(id));
  }

  /**
   * Updates an existing Faktura entity in the repository with the given data.
   *
   * @param updateFaktura a FakturaDto containing the updated details of the invoice
   * @param id the ID of the Faktura to update
   * @return the updated Faktura object
   */
  public Faktura update(String id, FakturaDto updateFaktura) {
    FakturaBuilder fakturaBuilder = fromFakturaEntity(fakturaRepository
      .findById(UUID.fromString(id))
      .orElseThrow(RuntimeException::new));

    fakturaBuilder.invoiceNumber(updateFaktura.invoiceNumber())
      .clientName(updateFaktura.clientName())
      .issueDate(updateFaktura.issueDate())
      .dueDate(updateFaktura.dueDate())
      .totalAmount(updateFaktura.totalAmount());

    FakturaEntity fakturaEntity = fakturaRepository.save(FakturaEntity.fromFaktura(fakturaBuilder.build()).build());

    return fromFakturaEntity(fakturaEntity).build();
  }

  /**
   * Finds and retrieves a Faktura instance based on the provided identifier.
   *
   * @param id the unique identifier of the Faktura to retrieve
   * @return the Faktura instance corresponding to the provided identifier
   * @throws RuntimeException if the Faktura with the given identifier is not found
   */
  public Faktura find(String id) {
    return fromFakturaEntity(fakturaRepository
      .findById(UUID.fromString(id))
      .orElseThrow(() ->  new FakturaNotFoundException(id)))
      .build();
  }

  /**
   * Retrieves all invoices from the repository, converts them to domain models, and returns them as a list.
   *
   * @return a list of Faktura objects representing all invoices stored in the repository
   */
  public List<Faktura> findAll() {
    return fakturaRepository.findAll()
      .stream()
      .map(faktura ->
        Faktura.fromFakturaEntity(faktura).build())
      .toList();
  }

  /**
   * Deletes all records from the repository.
   * This method removes all instances of `Faktura` entities managed by the repository.
   * It is intended for use when a complete cleanup or reset of the dataset is required.
   */
  public void deleteAll() {
    fakturaRepository.deleteAll();
  }
}
