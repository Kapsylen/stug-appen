package stugapi.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TimeUtility {

  public static LocalDateTime parseDate(String dateTime) {
    try {
      return LocalDate.parse(dateTime).atStartOfDay();
    } catch (DateTimeParseException e) {
      // Handle invalid format gracefully
      return null; // Or throw an exception depending on requirements
    }
  }

  public static LocalDateTime parseDateTime(String dateTime) {
    try {
      return LocalDateTime.parse(dateTime);
    } catch (DateTimeParseException e) {
      // Handle invalid format gracefully
      return null; // Or throw an exception depending on requirements
    }
  }

  public static boolean isNullOrEmpty(String startTime) {
    return startTime == null || startTime.isEmpty();
  }
}
