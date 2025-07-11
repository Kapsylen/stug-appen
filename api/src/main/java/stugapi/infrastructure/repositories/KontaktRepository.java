package stugapi.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stugapi.infrastructure.entities.KontaktEntity;

import java.util.UUID;

@Repository
public interface KontaktRepository extends JpaRepository<KontaktEntity, UUID> {}
