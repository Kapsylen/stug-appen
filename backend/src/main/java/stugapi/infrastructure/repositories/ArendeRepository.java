package stugapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stugapi.infrastructure.entities.ArendeEntity;

import java.util.UUID;
@Repository
public interface ArendeRepository extends JpaRepository<ArendeEntity, UUID> {}
