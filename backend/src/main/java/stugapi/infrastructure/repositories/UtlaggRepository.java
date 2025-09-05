package stugapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stugapi.infrastructure.entities.UtlaggEntity;

import java.util.UUID;

@Repository
public interface UtlaggRepository extends JpaRepository<UtlaggEntity, UUID> {}
