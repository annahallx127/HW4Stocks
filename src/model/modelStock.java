package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class modelStock implements Stock {
  private final String symbol;
  private final ArrayList<String> apiInfo;

  protected modelStock(String symbol) {
    this.symbol = symbol;
    this.apiInfo = new ArrayList<>();
    readFile();

    // Remove the header line - "timestamp,open,high,low,close,volume"
    this.apiInfo.remove(0);
  }

  private void readFile() {
    try {
      BufferedReader br = getBufferedReader();
      while (br.ready()) {
        apiInfo.add(br.readLine());
      }
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
  }

  /**
   * Creates a bufferedReader of 1024 bytes to read the API information file.
   * The API information file is always in the location "src/data/symbol.csv", where symbol is this
   * stock's symbol.
   *
   * @return A BufferedReader to read the API information file for the stock.
   * @throws FileNotFoundException if the API information file for the stock is not found.
   */
  private BufferedReader getBufferedReader() throws FileNotFoundException {
    File file = new File("src/data/" + this.symbol + ".csv");
    return new BufferedReader(new FileReader(file), 1024);
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

  public boolean isValidDate(String dateStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    try {
      LocalDate date = LocalDate.parse(dateStr, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}