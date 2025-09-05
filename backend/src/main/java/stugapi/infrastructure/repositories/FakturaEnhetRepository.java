package stugapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stugapi.infrastructure.entities.FakturaEnhetEntity;

import java.util.UUID;
@Repository
public interface FakturaEnhetRepository extends JpaRepository<FakturaEnhetEntity, UUID> {}
