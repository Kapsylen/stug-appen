package stugapi.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import stugapi.application.domain.model.Arende;
import stugapi.application.domain.model.ArendeStatus;
import stugapi.infrastructure.entities.ArendeEntity;
import stugapi.infrastructure.entities.enums.Prioritet;
import stugapi.infrastructure.entities.enums.Status;
import stugapi.infrastructure.entities.enums.Typ;
import stugapi.infrastructure.repositories.ArendeRepository;
import stugapi.presentation.dto.ArendeDto;
import stugapi.utility.TimeUtility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static stugapi.application.domain.model.Arende.*;
import static stugapi.application.domain.model.Arende.fromArendeDto;
import static stugapi.application.domain.model.ArendeStatus.toArendeStatus;
import static stugapi.infrastructure.entities.ArendeEntity.fromArende;

/**
 * Service class for managing operations related to `Arende`.
 * It provides methods to handle the lifecycle of `Arende` objects,
 * including creation, retrieval, update, and deletion.
 *
 * This class interacts with `ArendeRepository` for persistence and
 * employs mapping between domain models and data transfer objects.
 */
@Service
@AllArgsConstructor
public class ArendeService {

  private final ArendeRepository arendeRepository;

  /**
   * Saves an Arende object based on the provided ArendeDto and persists it to the database.
   *
   * @param arendeDto the data transfer object containing the information needed to create or update an Arende
   * @return the saved Arende object after mapping and persistence
   */
  public Arende saveArende(ArendeDto arendeDto) {
    ArendeEntity arendeEntity = fromArende(
      fromArendeDto(arendeDto).build())
      .build();

    return fromArendeEntity(arendeRepository
      .save(arendeEntity))
      .build();
  }

  /**
   * Deletes an entry in the repository based on its unique identifier.
   *
   * @param id the unique identifier of the entry to be deleted, represented as a string
   */
  public void deleteById(String id) {
    arendeRepository.deleteById(UUID.fromString(id));
  }

  /**
   * Updates an existing Arende entry in the repository based on its unique identifier.
   * The method finds the ArendeEntity by ID, applies updates from the provided ArendeDto,
   * and persists the changes to the database.
   *
   * @param id the unique identifier of the Arende entry to be updated
   * @param updateArende the data transfer object containing the updated information
   * @return the updated Arende object after persistence
   * @throws RuntimeException if no ArendeEntity is found with the provided ID
   */
  public Arende update(String id, ArendeDto updateArende) {
    ArendeBuilder arendeBuilder =  fromArendeEntity(arendeRepository
      .findById(UUID.fromString(id))
      .orElseThrow(RuntimeException::new));

    arendeBuilder.title(updateArende.title())
      .description(updateArende.description())
      .type(Typ.valueOf(updateArende.type()))
      .priority(Prioritet.valueOf(updateArende.priority()))
      .status(Status.valueOf(updateArende.status()))
      .reportedBy(updateArende.reportedBy())
      .assignedTo(updateArende.assignedTo())
      .location(updateArende.location())
      .estimatedCost(updateArende.estimatedCost())
      .actualCost(updateArende.actualCost())
      .startTime(TimeUtility.parseDate(updateArende.startTime()))
      .resolvedTime(TimeUtility.parseDate(updateArende.resolvedTime()))
      .resolution(updateArende.resolution())
      .requiresContractor(updateArende.requiresContractor())
      .contractorInfo(updateArende.contractorInfo())
      .updates(updateArende.updates().stream().map(a -> toArendeStatus(a, updateArende.startTime())).toList())
      .tags(updateArende.tags())
      .updatedAt(LocalDateTime.now());

    ArendeEntity arendeEntity = arendeRepository.save(ArendeEntity.fromArende(arendeBuilder.build()).build());

    return fromArendeEntity(arendeEntity).build();
  }

  /**
   * Retrieves an Arende object corresponding to the specified unique identifier.
   * This method fetches the data from the repository, converts it from the
   * entity representation to the domain model, and returns the resulting object.
   *
   * @param id the unique identifier of the Arende, represented as a string
   * @return the Arende object associated with the given identifier
   * @throws RuntimeException if no ArendeEntity is found with the provided identifier
   */
  public Arende findById(String id) {
    return fromArendeEntity(arendeRepository
      .findById(UUID.fromString(id))
      .orElseThrow(RuntimeException::new))
      .build();
  }

  /**
   * Retrieves all instances of Arende from the repository, converts them from
   * Arende entities to domain objects, and returns them as a list.
   *
   * @return a list of Arende objects retrieved and transformed from the repository data
   */
  public List<Arende> findAll() {
    return arendeRepository.findAll()
      .stream()
      .map(a -> fromArendeEntity(a).build())
      .toList();
  }

  /**
   * Deletes all records from the underlying repository.
   *
   * This method removes all entries in the associated repository, effectively clearing all data.
   * Use with caution as this operation is irreversible and will delete all stored records.
   */
  public void deleteAll() {
    arendeRepository.deleteAll();
  }

}
