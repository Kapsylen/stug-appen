package stugapi.presentation.error;

import jakarta.persistence.EntityNotFoundException;

public class UtlaggNotFoundException extends EntityNotFoundException {
  public UtlaggNotFoundException(String message) {
    super(message);
  }
}
