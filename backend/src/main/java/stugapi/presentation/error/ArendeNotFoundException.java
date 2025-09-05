package stugapi.presentation.error;

import jakarta.persistence.EntityNotFoundException;

public class ArendeNotFoundException extends EntityNotFoundException {
  public ArendeNotFoundException(String message) {
    super(message);
  }
}
