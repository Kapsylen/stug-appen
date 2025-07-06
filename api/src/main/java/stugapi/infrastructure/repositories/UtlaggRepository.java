package stugapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stugapi.infrastructure.entities.UtlaggEntity;

import java.util.UUID;

public interface UtlaggRepository extends JpaRepository<UtlaggEntity, UUID> {}
