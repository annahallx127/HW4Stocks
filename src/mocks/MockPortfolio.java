package mocks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.PlotScale;
import model.Portfolio;
import model.Stock;
import model.Transaction;

/**
 * A mock implementation of the Portfolio interface for testing purposes.
 * This class simulates the behavior of a real portfolio, managing a collection
 * of stocks and their associated share counts. It provides methods to add and
 * remove stocks, calculate the portfolio's value on a specific date, and validate dates.
 */
public class MockPortfolio implements Portfolio {

  private String name;

  private final Map<Stock, Double> stocks;

  /**
   * Constructs a new MockPortfolio with the specified name.
   * Initializes the portfolio with the given name.
   *
   * @param name the name of the portfolio
   */
  public MockPortfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }
  @Override
  public Map<Stock, Double> getStocks() {
    return Map.copyOf(stocks);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void add(Stock s, double shares, String date) {
    stocks.put(s, stocks.getOrDefault(s, 0.0) + shares);
  }

  @Override
  public void remove(Stock s, double shares, String date) {
    double currentShares = stocks.getOrDefault(s, 0.0);
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
    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
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

  @Override
  public String getValueDistribution(String date) {
    return "";
  }

  @Override
  public String getCompositionAtDate(String date) {
    return "";
  }

  @Override
  public String plotPerformanceOverTime(String startDate, String endDate, PlotScale scale) {
    return "";
  }

  @Override
  public void reBalancePortfolio(String reBalanceDate, Map<Stock, Integer> weightsOfStocks) {

  }

  @Override
  public void savePortfolio(String date) {

  }

  @Override
  public void addTransaction(Transaction transaction) {

  }

  @Override
  public List<Transaction> getTransactions() {
    return List.of();
  }
}
