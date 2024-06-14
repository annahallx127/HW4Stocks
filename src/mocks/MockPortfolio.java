package mocks;

import model.PlotInterval;
import model.Stock;
import model.Portfolio;
import model.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock implementation of the Portfolio interface used primarily for testing the functionality
 * of portfolio management without interacting with actual stock market data or external systems.
 * This class provides a simplified environment to simulate the addition and removal of stocks,
 * portfolio valuation, and other operations like rebalancing and transaction recording.
 */
public class MockPortfolio implements Portfolio {
  private String name;
  private Map<Stock, Double> stocks = new HashMap<>();
  private List<Transaction> transactions = new ArrayList<>();

  /**
   * Constructs a MockPortfolio with a specified name.
   *
   * @param name the name of the portfolio
   */
  public MockPortfolio(String name) {
    this.name = name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Map<Stock, Double> getStocks() {
    return new HashMap<>(stocks);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void add(Stock s, double shares, String date) {
    if (shares <= 0) throw new IllegalArgumentException("Shares must be positive.");
    stocks.put(s, stocks.getOrDefault(s, 0.0) + shares);
    transactions.add(new MockTransaction(LocalDate.parse(date), s, shares, "buy"));
  }

  @Override
  public void remove(Stock s, double shares, String date) {
    Double currentShares = stocks.get(s);
    if (currentShares == null || currentShares < shares) {
      throw new IllegalArgumentException("Not enough shares to sell.");
    }
    stocks.put(s, currentShares - shares);
    transactions.add(new MockTransaction(LocalDate.parse(date), s, shares, "sell"));
  }

  @Override
  public double valueOfPortfolio(String date) {
    return stocks.entrySet().stream()
            .mapToDouble(e -> e.getKey().getPriceOnDate(date) * e.getValue())
            .sum();
  }

  @Override
  public boolean isValidDateForPortfolio(String date) {
    LocalDate parsedDate = LocalDate.parse(date);
    return !(parsedDate.getDayOfWeek().getValue() == 6 || parsedDate.getDayOfWeek().getValue() == 7);
  }

  @Override
  public String getValueDistribution(String date) {
    double totalValue = valueOfPortfolio(date);
    StringBuilder distribution = new StringBuilder();
    stocks.forEach((stock, shares) -> {
      double value = stock.getPriceOnDate(date) * shares;
      double percentage = value / totalValue * 100;
      distribution.append(stock.toString()).append(": $").append(String.format("%.2f", value))
              .append(" (").append(String.format("%.2f", percentage)).append("%)\n");
    });
    return distribution.toString();
  }

  @Override
  public String getCompositionAtDate(String date) {
    StringBuilder composition = new StringBuilder();
    stocks.forEach((stock, shares) -> composition.append(stock.toString())
            .append(": ").append(shares)
            .append(" shares\n"));
    return composition.toString();
  }

  @Override
  public String plot(String dateStart, String dateEnd, PlotInterval scale) {
    return "Plot from " + dateStart + " to " + dateEnd + " with interval " + scale;
  }

  @Override
  public void reBalancePortfolio(String reBalanceDate, Map<Stock, Integer> targetWeights) {
    double totalValue = valueOfPortfolio(reBalanceDate);

    targetWeights.forEach((stock, weight) -> {
      double targetValue = totalValue * weight / 100.0;
      double currentShares = stocks.getOrDefault(stock, 0.0);
      double stockPrice = stock.getPriceOnDate(reBalanceDate);
      double targetShares = targetValue / stockPrice;

      if (targetShares > currentShares) {
        stocks.put(stock, targetShares);
      } else {
        stocks.put(stock, targetShares);
      }
    });

    stocks.keySet().retainAll(targetWeights.keySet());
  }


  @Override
  public void savePortfolio(String date) {
    System.out.println("Portfolio saved on " + date + " with current stock holdings.");
  }

  @Override
  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
  }

  @Override
  public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
  }

  @Override
  public String toString() {
    if (stocks.isEmpty()) {
      return "The portfolio is empty!";
    }
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
      sb.append(entry.getKey().toString()).append(": ").append(entry.getValue())
              .append(" shares\n");
    }
    return sb.toString();
  }
}

