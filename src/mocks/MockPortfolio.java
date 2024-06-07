package mocks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import model.Portfolio;
import model.Stock;

/**
 * A mock implementation of the Portfolio interface for testing purposes.
 * This class simulates the behavior of a real portfolio, managing a collection
 * of stocks and their associated share counts. It provides methods to add and
 * remove stocks, calculate the portfolio's value on a specific date, and validate dates.
 */
public class MockPortfolio implements Portfolio {

  private final String name;

  private final Map<Stock, Integer> stocks = new HashMap<>();

  public MockPortfolio(String name) {
    this.name = name;
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
    stocks.put(s, stocks.getOrDefault(s, 0) + shares);
  }

  @Override
  public void remove(Stock s, int shares) {
    int currentShares = stocks.getOrDefault(s, 0);
    if (currentShares < shares) {
      throw new IllegalArgumentException("Cannot remove more shares than present.");
    } else if (currentShares == shares) {
      stocks.remove(s);
    } else {
      stocks.put(s, currentShares - shares);
    }
  }

  @Override
  public double valueOfPortfolio(String date) {
    return stocks.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPriceOnDate(date) * entry.getValue())
            .sum();
  }

  @Override
  public String toString() {
    if (stocks.isEmpty()) {
      return "The portfolio is empty!";
    }

    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Stock, Integer> entry : stocks.entrySet()) {
      sb.append(entry.getKey().toString()).append(": ").append(entry.getValue()).append(" shares")
              .append(System.lineSeparator());
    }
    return sb.toString();
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

      throw new IllegalArgumentException("Date must be a valid market day.");
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
