package stugapi.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TimeUtility {

  public static LocalDateTime parseDateTime(String dateTime) {
    try {
      return dateTime != null ? LocalDateTime.parse(dateTime) : null;
    } catch (DateTimeParseException e) {
      // Handle invalid format gracefully
      return null; // Or throw an exception depending on requirements
    }
  }
}
