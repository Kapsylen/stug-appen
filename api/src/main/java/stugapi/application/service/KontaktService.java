package stugapi.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import stugapi.application.domain.model.Kontakt;
import stugapi.application.domain.model.Kontakt.KontaktBuilder;
import stugapi.infrastructure.entities.KontaktEntity;
import stugapi.infrastructure.repositories.KontaktRepository;
import stugapi.presentation.dto.KontaktDto;
import stugapi.presentation.error.KontaktNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Service class for handling CRUD operations related to Kontakt entities.
 * Provides functionality to save, update, delete, and retrieve Kontakt data.
 */
@Service
@AllArgsConstructor
public class KontaktService {

  private final KontaktRepository kontaktRepository;

  /**
   * Saves a Kontakt entity by converting a KontaktDto into appropriate domain and persistence entities.
   * The method ensures the provided data is transformed and persisted correctly.
   *
   * @param kontaktDto The Data Transfer Object containing the information of the contact to be saved
   * @return The saved Kontakt object populated with data from the persistence layer
   */
  public Kontakt saveKontakt(KontaktDto kontaktDto) {
    return Kontakt.fromKontaktEntity(kontaktRepository
      .save(KontaktEntity.fromKontakt(Kontakt.fromKontaktDto(kontaktDto)
        .build())
        .build()))
      .build();
  }

  /**
   * Deletes a Kontakt entity with the specified identifier.
   *
   * @param id The unique identifier of the Kontakt to delete. This must be a valid UUID represented as a string.
   */
  public void delete(String id) {
    kontaktRepository.findById(UUID.fromString(id))
      .orElseThrow(() -> new KontaktNotFoundException("No Kontakt found with ID: " + id));
    kontaktRepository.deleteById(UUID.fromString(id));
  }

  /**
   * Updates an existing Kontakt entity using the provided identifier and update data.
   * The method retrieves the Kontakt entity associated with the given id, applies
   * the updates from the KontaktDto, and saves the changes to the repository.
   *
   * @param id The unique identifier of the Kontakt to update. This must be a valid UUID represented as a string.
   * @param updateKontakt The Data Transfer Object containing the updated information for the Kontakt entity.
   * @return The updated Kontakt object reflecting the changes applied and persisted.
   * @throws RuntimeException if no Kontakt entity is found with the specified id.
   */
  public Kontakt update(String id, KontaktDto updateKontakt) {
    KontaktBuilder kontaktBuilder = Kontakt.fromKontaktEntity(kontaktRepository
      .findById(UUID.fromString(id))
      .orElseThrow(RuntimeException::new));

    kontaktBuilder.name(updateKontakt.name())
      .company(updateKontakt.company())
      .category(updateKontakt.category())
      .phone(updateKontakt.phone())
      .email(updateKontakt.email())
      .address(updateKontakt.address())
      .notes(updateKontakt.notes());

    KontaktEntity kontaktEntity = kontaktRepository.save(KontaktEntity.fromKontakt(kontaktBuilder.build()).build());

    return Kontakt.fromKontaktEntity(kontaktEntity).build();
  }

  /**
   * Retrieves a Kontakt entity by its unique identifier.
   * The method fetches the Kontakt instance from the repository,
   * converts it into a domain model, and returns it. If no entity
   * is found with the given identifier, an exception is thrown.
   *
   * @param id The unique identifier of the Kontakt to find. This must be a valid UUID represented as a string.
   * @return The Kontakt object corresponding to the provided identifier.
   * @throws RuntimeException if no Kontakt entity is found with the specified identifier.
   */
  public Kontakt find(String id) {
    return Kontakt.fromKontaktEntity(kontaktRepository
      .findById(UUID.fromString(id))
      .orElseThrow(() -> new KontaktNotFoundException("No Kontakt found with ID: " + id)))
      .build();
  }

  /**
   * Retrieves all Kontakt entities from the repository, converts them into the domain model,
   * and returns a list of these Kontakt objects.
   *
   * @return A list of Kontakt objects representing all entities in the repository.
   */
  public List<Kontakt> findAll() {
    return kontaktRepository.findAll()
      .stream()
      .map(k -> Kontakt.fromKontaktEntity(k).build())
      .toList();
  }

  /**
   * Deletes all Kontakt entities from the repository.
   * This operation removes all entries available in the persistence layer
   * for the Kontakt domain model.
   */
  public void deleteAll() {
    kontaktRepository.deleteAll();
  }
}
