package stugapi.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import stugapi.application.domain.model.Utlagg;
import stugapi.infrastructure.entities.UtlaggEntity;
import stugapi.infrastructure.repositories.UtlaggRepository;
import stugapi.presentation.dto.UtlaggDto;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing operations related to `Utlagg`.
 * Provides methods to create, read, update, and delete `Utlagg` records,
 * as well as methods to handle bulk operations on the repository.
 */
@Service
@AllArgsConstructor
@Log4j2
public class UtlaggService {

  private final UtlaggRepository utlaggRepository;

  /**
   * Saves a new Utlagg based on the given UtlaggDto.
   *
   * @param utlaggDto the data transfer object containing information to create a new Utlagg
   * @return the saved Utlagg object
   */
  public Utlagg saveUtlagg(UtlaggDto utlaggDto) {
    Utlagg utlagg = Utlagg.fromUtlaggDto(utlaggDto).build();
    return Utlagg.fromUtlaggEntity(utlaggRepository
        .save(UtlaggEntity.fromUtlagg(utlagg)
          .build()))
      .build();
  }

  /**
   * Deletes an entity with the specified identifier.
   *
   * @param id The unique identifier of the entity to delete
   */
  public void delete(String id) {
    utlaggRepository.deleteById(UUID.fromString(id));
  }

  /**
   * Updates an existing Utlagg record in the system with new data provided in the UtlaggDto.
   *
   * @param updateUtlagg The data transfer object containing updated details for the Utlagg
   * @param id The unique identifier of the existing Utlagg to be updated
   * @return The updated Utlagg domain object
   * @throws RuntimeException if the Utlagg with the provided id is not found
   */
  public Utlagg update(UUID id, UtlaggDto updateUtlagg) {
    var utlaggBuilder = Utlagg.fromUtlaggEntity(utlaggRepository
      .findById(id)
      .orElseThrow(RuntimeException::new));

    utlaggBuilder.title(updateUtlagg.title())
      .description(updateUtlagg.description())
      .outlayDate(updateUtlagg.outlayDate())
      .price(updateUtlagg.price());

    UtlaggEntity utlaggEntity = utlaggRepository.save(UtlaggEntity.fromUtlagg(utlaggBuilder.build()).build());

    return Utlagg.fromUtlaggEntity(utlaggEntity).build();
  }

  /**
   * Finds a specific Utlagg by its unique identifier.
   *
   * @param id The unique identifier of the Utlagg to be retrieved. This must be
   *           a valid UUID represented as a string.
   * @return The corresponding Utlagg object if found.
   * @throws RuntimeException if no Utlagg is found with the specified identifier.
   */
  public Utlagg find(String id) {
    return Utlagg.fromUtlaggEntity(utlaggRepository
      .findById(UUID.fromString(id))
      .orElseThrow(RuntimeException::new))
      .build();
  }

  /**
   * Retrieves all outlay records from the repository and maps them to domain objects.
   *
   * @return a list of all currently stored outlay records as {@link Utlagg} instances
   */
  public List<Utlagg> findAll() {
    return utlaggRepository.findAll()
      .stream()
      .map(u -> Utlagg
        .fromUtlaggEntity(u)
        .build())
      .toList();
  }

  /**
   * Deletes all records from the repository.
   * This operation removes all instances of the `Utlagg` entities managed by the repository.
   * Intended for use when a complete reset or cleanup of the dataset is required.
   */
  public void deleteAll() {
    utlaggRepository.deleteAll();
  }
}
