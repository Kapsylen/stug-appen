package stugapi.presentation.error;

import jakarta.persistence.EntityNotFoundException;

public class FakturaNotFoundException extends EntityNotFoundException {
  public FakturaNotFoundException(String message) {super(message);
  }
}
