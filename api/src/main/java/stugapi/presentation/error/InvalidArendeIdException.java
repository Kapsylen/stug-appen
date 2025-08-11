package stugapi.presentation.error;

public class InvalidArendeIdException extends RuntimeException{
  public InvalidArendeIdException(String message, Throwable cause) {
    super(message, cause);
  }
}
