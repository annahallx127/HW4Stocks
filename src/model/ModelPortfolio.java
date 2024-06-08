package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a portfolio in the stock investment program. Each portfolio has a name and manages a
 * collection of stocks, allowing for the addition, removal, and valuation of stocks within it.
 * The portfolio ensures that stocks can only be added with a positive number of shares, and it
 * enforces rules for removing stocks, such as not allowing more shares to be removed than
 * are present.
 * The portfolio also contains methods to calculate its total value on a given date,
 * adjusting for weekends, and validates whether a date is valid for checking stock prices.
 */
public class ModelPortfolio implements Portfolio {
  private final String name;
  private final HashMap<Stock, Integer> stocks;

  /**
   * Constructs a new modelPortfolio with the specified name.
   * Initializes an empty collection of stocks within the portfolio that will later be filled.
   *
   * @param name the name of the portfolio specified by the user
   */
  public ModelPortfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
  }

  @Override
  public Map<Stock, Integer> getStocks() {
    return Map.copyOf(stocks);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void add(Stock s, int shares) {
    if (shares <= 0) {
      throw new IllegalArgumentException("Shares added must be one or more.");
    }
    stocks.put(s, stocks.getOrDefault(s, 0) + shares);
  }

  @Override
  public void remove(Stock s, int shares) throws IllegalArgumentException {
    if (!stocks.containsKey(s)) {
      throw new IllegalArgumentException("Cannot remove a stock that doesn't exist.");
    }

    int currentShares = stocks.get(s);
    if (shares > currentShares) {
      throw new IllegalArgumentException("Cannot remove more shares than "
              + "the number of shares present.");
    }

    if (currentShares - shares == 0) {
      stocks.remove(s);
    } else {
      stocks.put(s, currentShares - shares);
    }
  }

  @Override
  public String toString() {
    if (stocks.isEmpty()) {
      return "The portfolio is empty!";
    }

    StringBuilder ret = new StringBuilder();
    for (Map.Entry<Stock, Integer> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      int shares = entry.getValue();
      ret.append(stock.toString()).append(": ")
              .append(shares).append(" shares").append(System.lineSeparator());
    }

    return ret.toString();
  }

  private LocalDate getValidMarketDateWeekend(String dateStr) {
    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    DayOfWeek dayOfWeek = date.getDayOfWeek();

    if (dayOfWeek == DayOfWeek.SATURDAY) {
      date = date.minusDays(1);
    } else if (dayOfWeek == DayOfWeek.SUNDAY) {
      date = date.minusDays(2);
    }

    if (date.getMonthValue() == 12 && date.getDayOfMonth() == 25) {
      date = date.minusDays(3);
    }

    if (date.getMonthValue() == 1 && date.getDayOfMonth() == 1) {
      date = date.minusDays(3);
    }

    return date;
  }

  @Override
  public double valueOfPortfolio(String date) {
    LocalDate validDate = getValidMarketDateWeekend(date);

    double value = 0.0;
    for (Map.Entry<Stock, Integer> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      int shares = entry.getValue();
      double stockPrice = stock.getPriceOnDate(validDate.toString());
      value += stockPrice * shares;
    }
    return value;
  }

  @Override
  public boolean isValidDateForPortfolio(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    try {
      LocalDate parsedDate = LocalDate.parse(date, formatter);
      if (parsedDate.isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Date cannot be in the future.");
      }

      for (Stock stock : stocks.keySet()) {
        if (stock.isValidDate(date)) {
          return true;
        }
      }

    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }
}