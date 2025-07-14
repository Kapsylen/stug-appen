package stugapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stugapi.infrastructure.entities.ArendeEntity;

import java.util.UUID;

public interface ArendeRepository extends JpaRepository<ArendeEntity, UUID> {}
