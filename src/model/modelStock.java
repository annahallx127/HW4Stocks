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
import java.util.Arrays;

public class modelStock implements Stock {
  private final String symbol;
  private final ArrayList<String> apiInfo;

  // TODO: useful methods LocalDate.minus and LocalDate.plus for date checking
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
   * Creates a {@code BufferedReader} of 1024 bytes to read the API information file.
   * The API information file is always in the location {@code ~/src/data/sym.csv}, where sym is
   * this {@code Stock}'s symbol.
   *
   * @return A {@code BufferedReader} to read the API information file for the stock.
   * @throws FileNotFoundException if the API information file for the stock is not found.
   */
  private BufferedReader getBufferedReader() throws FileNotFoundException {
    File file = new File("src/data/" + this.symbol + ".csv");
    return new BufferedReader(new FileReader(file), 1024);
  }

  @Override
  public double gainedValue(String dateStart, String dateEnd) throws IllegalArgumentException {
    double change = 0.0;

    if (!isValidDate(dateStart, dateEnd)) {
      throw new IllegalArgumentException("Invalid dates.");
    }

    for (int i = 0; i < apiInfo.size(); i++) {
      if (apiInfo.get(i).contains(dateStart)) {
        for (int j = i; j < apiInfo.size(); j++) {
          if (apiInfo.get(j).contains(dateEnd)) {
            change = Double.parseDouble(apiInfo.get(j).split(",")[4])
                    - Double.parseDouble(apiInfo.get(i).split(",")[4]);
            break;
          }
        }
      }
    }
    
    return change;
  }

  @Override
  public double getMovingAverage(int days, String date) throws IllegalArgumentException {
    double sum = 0;

    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Invalid date.");
    }

    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }

    for (int i = 0; i < apiInfo.size(); i++) {
      if (apiInfo.get(i).contains(date)) {
        for (int j = 1; j <= days; j++) {
          sum += Double.parseDouble(apiInfo.get(i + j).split(",")[4]);
        }
        break;
      }
    }

    return sum / days;
  }
  
  @Override
  public String getCrossovers(String dateStart, String dateEnd, int days) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    StringBuilder ret = new StringBuilder();

    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }

    if (!isValidDate(dateStart, dateEnd)) {
      throw new IllegalArgumentException("Invalid date.");
    }

    for (int i = 0; i < apiInfo.size(); i++) {
      if (apiInfo.get(i).contains(dateStart)) {
        for (int j = i; j < apiInfo.size(); j++) {
          String[] split = apiInfo.get(j).split(",");
          // if current close price > x day average
          if (Double.parseDouble(split[4]) > this.getMovingAverage(days, split[0])) {
            ret.append(split[4]).append(System.lineSeparator());
          }
          if (apiInfo.get(j).contains(dateEnd)) {
            break;
          }
        }
      }
    }

//    finish and abstract this
//    for (int i = 0; i < apiInfo.size(); i++) {
//      if (apiInfo.get(i).contains(dateStart)) {
//        int counter = i + 1;
//        String toParse = apiInfo.get(i).split(",")[0];
//        String nextParse = apiInfo.get(counter).split(",")[0];
//        LocalDate parseStart = LocalDate.parse(toParse, formatter);
//        LocalDate parseNext = LocalDate.parse(nextParse, formatter);
//        // next date - this date != 1
//        if (!parseNext.minusDays(1).isEqual(parseStart)) {
//
//        }
//      }
//    }
    return ret.toString();
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

    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Date must be in a valid format.");
    }

    StringBuilder sb = new StringBuilder();

    for (String s : apiInfo) {
      if (s.contains(date)) {
        String[] split = s.split(",");
        sb.append("Stock: " + this.symbol + System.lineSeparator());
        sb.append("Date: " + split[0] + System.lineSeparator());
        sb.append("Open: " + split[1] + System.lineSeparator());
        sb.append("High: " + split[2] + System.lineSeparator());
        sb.append("Low: " + split[3] + System.lineSeparator());
        sb.append("Close: " + split[4] + System.lineSeparator());
        sb.append("Volume: " + split[5] + System.lineSeparator());
        break;
      }
    }

    return sb.toString();
  }

  @Override
  public boolean isValidDate(String dateStr) {
    if (dateStr == null) {
      return false;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    if (!apiInfo.toString().contains(dateStr)) {
      throw new IllegalArgumentException("Date must be a valid market day.");
    }

    try {
      LocalDate date = LocalDate.parse(dateStr, formatter);
      return !date.isAfter(LocalDate.now());
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  private boolean isValidDate(String dateStart, String dateEnd) {
    if (dateStart == null || dateEnd == null) {
      return false;
    }

    if (!apiInfo.toString().contains(dateStart) || !apiInfo.toString().contains(dateEnd)) {
      throw new IllegalArgumentException("Dates must be valid market days.");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    try {
      LocalDate sDate = LocalDate.parse(dateStart, formatter);
      LocalDate eDate = LocalDate.parse(dateEnd, formatter);
      return !eDate.isAfter(sDate) && !eDate.isAfter(LocalDate.now());
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}