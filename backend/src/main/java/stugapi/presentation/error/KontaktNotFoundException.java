package stugapi.presentation.error;

import jakarta.persistence.EntityNotFoundException;

public class KontaktNotFoundException extends EntityNotFoundException {
  public KontaktNotFoundException(String message) {
    super(message);
  }
}
