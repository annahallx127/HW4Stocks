package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a stock in the stock investment program. A stock has a symbol and information about
 * its prices and volumes on different dates. The stock object is responsible for calculating the
 * gain or loss of the stock, the moving average of the stock, and the crossovers of the stock. It
 * can also validate dates and check if a date is a market day, throwing exceptions if the date is
 * invalid.
 */
public class ModelStock implements Stock {
  private final String symbol;
  private final ArrayList<String> apiInfo;
  private final Set<String> validSymbols;

  /**
   * Constructs a modelStock object with the specified ticker symbol.
   * Initializes the apiInfo list and reads stock data from a file,
   * removing the header line.
   *
   * @param symbol the ticker of the specified stock
   */
  public ModelStock(String symbol) {
    this.symbol = symbol;
    this.apiInfo = new ArrayList<>();
    String csvFile = "src/data/listing_status.csv";
    validSymbols = parseValidSymbols(csvFile);

    readFile();

    // Remove the header line
    this.apiInfo.remove(0);
  }

  private void readFile() {
    try (FileReader fileReader = new FileReader("src/data/api/" + this.symbol + ".csv");
         BufferedReader br = new BufferedReader(fileReader)) {
      while (br.ready()) {
        apiInfo.add(br.readLine());
      }
      if (this.apiInfo.isEmpty()) {
        File file = new File("src/data/api/" + this.symbol + ".csv");
        file.delete();
        throw new IllegalArgumentException("No results found for stock " + symbol + ".");
      }
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
  }

  @Override
  public double gainedValue(String dateStart, String dateEnd) throws IllegalArgumentException {
    double change = 0.0;

    if (!isValidDate(dateStart, dateEnd)) {
      throw new IllegalArgumentException("Invalid dates.");
    }

    dateStart = getNearestMarketDate(dateStart);
    dateEnd = getNearestMarketDate(dateEnd);

    for (int i = 0; i < apiInfo.size(); i++) {
      if (apiInfo.get(i).contains(dateEnd)) {
        for (int j = i; j < apiInfo.size(); j++) {
          if (apiInfo.get(j).contains(dateStart)) {
            change = Double.parseDouble(apiInfo.get(i).split(",")[4])
                    - Double.parseDouble(apiInfo.get(j).split(",")[4]);
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

    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }

    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Invalid date.");
    }

    date = getNearestMarketDate(date);

    for (int i = 0; i < apiInfo.size(); i++) {
      if (apiInfo.get(i).contains(date)) {
        // normalize number of days to the maximum
        if (days > apiInfo.size() - i) {
          days = apiInfo.size() - i;
        }
        for (int j = 0; j < days && j + i < apiInfo.size(); j++) {
          sum += Double.parseDouble(apiInfo.get(i + j).split(",")[4]);
        }
        break;
      }
    }
    return sum / days;
  }

  @Override
  public String getCrossovers(String dateStart, String dateEnd, int days) {
    StringBuilder ret = new StringBuilder();

    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }

    if (!isValidDate(dateStart, dateEnd)) {
      throw new IllegalArgumentException("Invalid dates.");
    }

    dateStart = getNearestMarketDate(dateStart);
    dateEnd = getNearestMarketDate(dateEnd);

    for (int i = 0; i < apiInfo.size(); i++) {
      if (apiInfo.get(i).contains(dateEnd)) {
        for (int j = i; j < apiInfo.size(); j++) {
          String[] split = apiInfo.get(j).split(",");
          // if current close price > x day average
          if (Double.parseDouble(split[4]) > this.getMovingAverage(days, split[0])) {
            // append date
            ret.append(split[0]).append(System.lineSeparator());
          }
          if (apiInfo.get(j).contains(dateStart)) {
            break;
          }
        }
      }
    }
    if (ret.length() == 0) {
      return ret.append("None!").append(System.lineSeparator()).toString();
    }
    return ret.toString();
  }

  // toString prints out stock values on a given date
  @Override
  public String toString(String date) {
    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null.");
    }

    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Invalid date.");
    }

    date = getNearestMarketDate(date);

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
  public String toString() {
    return this.symbol;
  }

  @Override
  public boolean isValidDate(String dateStr) {
    if (dateStr == null) {
      return false;
    }

    LocalDate date;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    try {
      date = LocalDate.parse(dateStr, formatter);
      if (date.isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Date cannot be in the future.");
      }
      if (!apiInfo.toString().contains(date.toString())) {
        dateStr = getNearestMarketDate(dateStr);
      }
      return !date.isAfter(LocalDate.now());
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  private boolean isValidDate(String dateStart, String dateEnd) {
    if (dateStart == null || dateEnd == null) {
      return false;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    try {
      LocalDate sDate = LocalDate.parse(dateStart, formatter);
      LocalDate eDate = LocalDate.parse(dateEnd, formatter);
      if (sDate.isAfter(LocalDate.now()) || eDate.isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Dates cannot be in the future.");
      }
      if (!apiInfo.toString().contains(sDate.toString())) {
        dateStart = getNearestMarketDate(dateStart);
      }
      if (!apiInfo.toString().contains(eDate.toString())) {
        dateEnd = getNearestMarketDate(dateEnd);
      }
      return eDate.isAfter(sDate) && !eDate.isAfter(LocalDate.now());
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  @Override
  public double getPriceOnDate(String date) {
    double price = 0.0;

    date = getNearestMarketDate(date);

    if (!isValidDate(date)) {
      throw new IllegalArgumentException("Invalid date.");
    }

    for (String entry : apiInfo) {
      if (entry.contains(date)) {
        String[] split = entry.split(",");
        price = Double.parseDouble(split[4]);
      }
    }
    return price;
  }

  /**
   * Gets the nearest market date to the given date.
   * If the given date is a weekend, it returns the previous market day.
   *
   * @param dateStr the date to check.
   * @return the nearest previous market date as a string.
   */
  private String getNearestMarketDate(String dateStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate date = LocalDate.parse(dateStr, formatter);

    while (!apiInfo.toString().contains(date.toString())) {
      if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
        date = date.minusDays(1);
      } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
        date = date.minusDays(2);
      } else {
        date = date.minusDays(1);
      }
      if (date.isBefore(LocalDate.parse(apiInfo.get(apiInfo.size() - 1).split(",")[0]))) {
        throw new IllegalArgumentException("No valid market dates available.");
      }
    }

    return date.toString();
  }

  public static Set<String> parseValidSymbols(String filePath) {
    Set<String> validSymbols = new HashSet<>();
    String line;

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      line = br.readLine();

      while ((line = br.readLine()) != null) {
        String[] columns = line.split(",");
        String symbol = columns[0].trim();
        if (!symbol.isEmpty()) {
          validSymbols.add(symbol);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return validSymbols;
  }

  @Override
  public void isValidSymbol(String symbol) throws IllegalArgumentException {
    if (!validSymbols.contains(symbol)) {
      throw new IllegalArgumentException("Invalid Symbol.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelStock modelStock = (ModelStock) o;
    return symbol.equals(modelStock.symbol);
  }

  @Override
  public int hashCode() {
    return symbol.hashCode();
  }

  @Override
  public String plot(String dateStart, String dateEnd, PlotInterval interval) {
    return "";
  }
}
