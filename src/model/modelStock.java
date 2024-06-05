package model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class modelStock implements Stock {
  private final String symbol;
  private final ArrayList<String> apiInfo;

  protected modelStock(String symbol, String apiInfo) {
    this.symbol = symbol;
    this.apiInfo = new ArrayList<>();

    // Split the .csv file along newlines
    this.apiInfo.addAll(Arrays.asList(apiInfo.split(System.lineSeparator())));
    // Remove the header line
    this.apiInfo.remove(0);
  }

  public double gainedValue(String dateStart, String dateEnd) throws IllegalArgumentException {
    if (dateStart == null || dateEnd == null) {
      throw new IllegalArgumentException("Dates cannot be null.");
    }

    try {
      LocalDate start = LocalDate.parse(dateStart);
      LocalDate end = LocalDate.parse(dateEnd);

      if (start.isAfter(end)) {
        throw new IllegalArgumentException("Start date must be before end date.");
      }
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Dates must be in a valid format.");
    }

    boolean foundStart = false;
    boolean foundEnd = false;
    for (String line : apiInfo) {
      if (line.equals(dateStart)) {
        foundStart = true;
      } else if (line.equals(dateEnd)) {
        foundEnd = true;
      }
    }

    // TODO: figure out how to go back multiple days to find the last valid date
    // if we didn't find the start date or end date, we need to go back a day
    if (!foundStart || !foundEnd) {
      throw new IllegalArgumentException("Did not find the start date or end date of the stock");
    }

    return 0.0;
  }

  // TODO: Implement this
  @Override
  public double getChange(String dateStart, String dateEnd) {
    if (dateStart == null || dateEnd == null) {
      throw new IllegalArgumentException("Dates cannot be null.");
    }

    try {
      LocalDate start = LocalDate.parse(dateStart);
      LocalDate end = LocalDate.parse(dateEnd);

      if (start.isAfter(end)) {
        throw new IllegalArgumentException("Start date must be before end date.");
      }
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Dates must be in a valid format.");
    }

    // if not valid date, or if dateEnd is not after dateStart, use date class
    return 0.0;
  }

  // TODO: Implement this
  @Override
  public double getMovingAverage(int days, String date) throws IllegalArgumentException {
    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }

    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null.");
    }

    try {
      LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Date must be in a valid format.");
    }

    return 0.0;
  }

  // TODO: Implement this
  @Override
  public String[] getCrossovers(String dateStart, String dateEnd, int days) {
    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }

    if (dateStart == null || dateEnd == null) {
      throw new IllegalArgumentException("dates cannot be null.");
    }

    try {
      LocalDate start = LocalDate.parse(dateStart);
      LocalDate end = LocalDate.parse(dateEnd);

      if (start.isAfter(end)) {
        throw new IllegalArgumentException("start date must be before end date.");
      }
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("dates must be in a valid format.");
    }

    return new String[0];
  }

  @Override
  public String getSymbol() {
    return symbol;
  }

  // toString prints out stock values on a given date
  @Override
  public String toString(String date) {
    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null.");
    }

    try {
      LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Date must be in a valid format.");
    }

    return "";
  }


}