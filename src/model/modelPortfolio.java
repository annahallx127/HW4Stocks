package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class modelPortfolio implements Portfolio {
  private final String name;
  private final HashMap<Stock, Integer> stocks;

  public modelPortfolio(String name) {
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
  public double valueOfPortfolio(String date) {
    double value = 0.0;
    for (Map.Entry<Stock, Integer> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      int shares = entry.getValue();
      double stockPrice = stock.getPriceOnDate(date);
      value += stockPrice * shares;
    }
    return value;
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
