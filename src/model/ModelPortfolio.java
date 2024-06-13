package model;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.PortfolioWriter;

import static model.PlotInterval.DAYS;

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
  private String name;
  private final Map<Stock, Double> stocks;
  private final List<Transaction> transactions;


  /**
   * Constructs a new modelPortfolio with the specified name.
   * Initializes an empty collection of stocks within the portfolio that will later be filled.
   *
   * @param name the name of the portfolio specified by the user
   */
  public ModelPortfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
    this.transactions = new ArrayList<>();
  }

  @Override
  public Map<Stock, Double> getStocks() {
    return Map.copyOf(stocks);
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  // add can only add a whole number!!!  only when rebalancing can it be fractional
  @Override
  public void add(Stock s, double shares, String date) {
    if (shares <= 0) {
      throw new IllegalArgumentException("Shares must be greater than zero.");
    }

    LocalDate intendedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);


    // Check if the transaction date is legal
    if (!transactions.isEmpty() && intendedDate.isBefore(transactions.get(transactions.size() - 1).getDate())) {
      throw new IllegalArgumentException("Transaction date cannot be before the latest transaction date.");
    }

    Transaction transaction = new ModelTransaction(intendedDate, s, shares, "buy");
    addTransaction(transaction);
  }

  // removing can also only be in whole numbers
  // add a clause to round up if its .5 and above, round down if .4 and down?
  // how to deal with this when rebalancing, bc it can do fractional shares
  // piazza says can allow to sell fractional shares
  @Override
  public void remove(Stock s, double shares, String date) throws IllegalArgumentException {
    if (shares <= 0) {
      throw new IllegalArgumentException("Shares removed must be greater than zero.");
    }
    LocalDate intendedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);


    // Ensure no transaction is before the latest transaction
    if (!transactions.isEmpty() && intendedDate.isBefore(transactions.get(transactions.size() - 1).getDate())) {
      throw new IllegalArgumentException("Transaction date cannot be before the latest transaction date.");
    }

    double currentShares = stocks.getOrDefault(s, 0.0);
    if (shares > currentShares) {
      throw new IllegalArgumentException("Cannot remove more shares than the number of shares present.");
    }

    Transaction transaction = new ModelTransaction(intendedDate, s, shares, "sell");
    addTransaction(transaction);
  }

  @Override
  public String toString() {
    if (stocks.isEmpty()) {
      return "The portfolio is empty!";
    }

    StringBuilder ret = new StringBuilder();
    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      double shares = entry.getValue();
      ret.append(stock.toString()).append(": ")
              .append(shares).append(" shares").append(System.lineSeparator());
    }

    return ret.toString();
  }

  private LocalDate getValidMarketDateWeekend(String dateStr) {
    LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    DayOfWeek dayOfWeek = date.getDayOfWeek();

    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Date cannot be in the future.");
    }

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

    if (!isValidDateForPortfolio(date)) {
      throw new IllegalArgumentException("Cannot check portfolio value on weekend, please enter a market date");
    }
    LocalDate newDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    LocalDate validDate = getValidMarketDateWeekend(date);


    // This map will hold the effective shares of each stock up to the valid date.
    Map<Stock, Double> effectiveShares = new HashMap<>();

    // Aggregate shares from all transactions up to and including the valid date.
    for (Transaction transaction : transactions) {
      if (!transaction.getDate().isAfter(validDate)) {
        Stock stock = transaction.getStock();
        double shares = transaction.getShares();
        if ("sell".equalsIgnoreCase(transaction.getType())) {
          shares = -shares;  // Negate shares if the transaction is a sell.
        }
        effectiveShares.merge(stock, shares, Double::sum);
      }
    }

    double totalValue = 0.0;
    // Calculate the total value based on the aggregated shares.
    for (Map.Entry<Stock, Double> entry : effectiveShares.entrySet()) {
      Stock stock = entry.getKey();
      double shares = entry.getValue();
      if (shares > 0) {  // Only calculate value for positive shares.
        double stockPrice = stock.getPriceOnDate(validDate.toString());
        totalValue += stockPrice * shares;
      }
    }

    return totalValue;
  }


  @Override
  public boolean isValidDateForPortfolio(String date) {
    try {
      LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
      if (parsedDate.isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Date cannot be in the future.");
      }

      for (Stock stock : stocks.keySet()) {
        if (!stock.isValidDate(date)) {
          return false;
        }
      }

    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

  @Override
  public String getValueDistribution(String date) {
    LocalDate validDate = getValidMarketDateWeekend(date);

    try {
      isValidDateForPortfolio(validDate.toString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return "Invalid date for portfolio operations: " + validDate;
    }

    double totalValue = 0.0;
    Map<Stock, Double> stockValues = new HashMap<>();

    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      double shares = entry.getValue();
      double stockPrice = stock.getPriceOnDate(validDate.toString());
      double stockValue = shares * stockPrice;
      stockValues.put(stock, stockValue);
      totalValue += stockValue;
    }

    if (totalValue == 0) {
      return "The value of the portfolio is 0 at this date, it is empty";
    }

    Map<String, String> distribution = new HashMap<>();
    for (Map.Entry<Stock, Double> entry : stockValues.entrySet()) {
      double stockValue = entry.getValue();
      double percentage = stockValue / totalValue * 100;
      distribution.put(entry.getKey().toString(), String.format("$%.2f (%.2f%%)", stockValue, percentage));
    }

    StringBuilder result = new StringBuilder("Total Portfolio Value: ").append(String.format("$%.2f\n", totalValue));
    for (Map.Entry<String, String> entry : distribution.entrySet()) {
      result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    return result.toString();
  }

  @Override
  public String getCompositionAtDate(String date) {
    LocalDate validDate = getValidMarketDateWeekend(date);

    if (!transactions.isEmpty() && validDate.isBefore(transactions.get(0).getDate())) {
      return "No transactions have been made in this portfolio yet.";
    }

    Map<Stock, Double> portfolioComposition = new HashMap<>();
    for (Transaction transaction : transactions) {
      if (!transaction.getDate().isAfter(validDate)) {
        Stock stock = transaction.getStock();
        double shares = transaction.getShares();
        if (transaction.getType().equalsIgnoreCase("buy")) {
          portfolioComposition.put(stock, portfolioComposition.getOrDefault(stock, 0.0) + shares);
        } else if (transaction.getType().equalsIgnoreCase("sell")) {
          double currentShares = portfolioComposition.getOrDefault(stock, 0.0);
          portfolioComposition.put(stock, currentShares - shares);
          if (portfolioComposition.get(stock) <= 0) {
            portfolioComposition.remove(stock);
          }
        }
      }
    }

    StringBuilder result = new StringBuilder();
    for (Map.Entry<Stock, Double> entry : portfolioComposition.entrySet()) {
      result.append(entry.getKey().toString()).append(": ")
              .append(entry.getValue()).append(" shares").append(System.lineSeparator());
    }

    return result.toString();
  }

  // TODO: put this in an extended class after refactoring
  // check for valid date in view or here?
  // plots the asterisk
  @Override
  public String plot(String dateStart, String dateEnd, PlotInterval scale) {
//    int asterisks = 1;
//    LocalDate validStartDate = getValidMarketDateWeekend(dateStart);
//    LocalDate validEndDate = getValidMarketDateWeekend(dateEnd);
//    StringBuilder sb = new StringBuilder();
//    sb.append("Performance of Portfolio: ").append(name).append(" from ")
//            .append(dateEnd).append(" to ").append(dateStart)
//            .append(System.lineSeparator()).append(System.lineSeparator());
//    try {
//      isValidDateForPortfolio(validStartDate.toString());
//      isValidDateForPortfolio(validEndDate.toString());
//    } catch (IllegalArgumentException e) {
//      System.out.println(e.getMessage());
//    }
//
//    switch (scale) {
//      case DAYS:
//        // 7 - number of valid days
//        for (int i = 0; i < 7; i++) {
//          LocalDate currentDate = validStartDate.minusDays(i);
//          if (currentDate.isBefore(validEndDate)) {
//            double totalValue = valueOfPortfolio(currentDate.toString());
//            sb.append(currentDate).append(": ").append("*".repeat(DAYS.scale(totalValue)))
//                    .append(System.lineSeparator());
//          }
//        }
//        sb.append("Scale: * = $").append(DAYS.scale()).append(System.lineSeparator());
//      case WEEKS:
//      case MONTHS:
//      case YEARS:
//      case FIVE_YEARS:
//      case TEN_YEARS:
//      default:
//        return "Invalid scale.";
//    }
//
//    return sb.toString();
    return "";
  }


  @Override
  public void reBalancePortfolio(String reBalanceDate, Map<Stock, Integer> targetWeights) {
    LocalDate intendedDate = LocalDate.parse(reBalanceDate, DateTimeFormatter.ISO_LOCAL_DATE);

    if (intendedDate.getDayOfWeek() == DayOfWeek.SATURDAY || intendedDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
      throw new IllegalArgumentException("This program does not support re-balancing a" +
              " portfolio on a non market date! Please enter a valid date.");
    }
    double totalValue = valueOfPortfolio(reBalanceDate);

    Map<Stock, Double> currentValues = new HashMap<>();
    stocks.forEach((stock, shares) -> {
      double stockPrice = stock.getPriceOnDate(reBalanceDate);
      currentValues.put(stock, shares * stockPrice);
    });

    for (Map.Entry<Stock, Integer> entry : targetWeights.entrySet()) {
      Stock stock = entry.getKey();
      int targetWeight = entry.getValue();
      double targetValue = totalValue * targetWeight / 100.0;
      double currentValue = currentValues.getOrDefault(stock, 0.0);
      double stockPrice = stock.getPriceOnDate(reBalanceDate);

      double targetShares = targetValue / stockPrice;

      if (targetWeight == 0) {
        stocks.remove(stock);
      } else if (currentValue > targetValue) {
        double excessValue = currentValue - targetValue;
        double sharesToSell = excessValue / stockPrice;
        stocks.put(stock, stocks.get(stock) - sharesToSell);
      } else if (currentValue < targetValue) {
        double shortfallValue = targetValue - currentValue;
        double sharesToBuy = shortfallValue / stockPrice;
        stocks.put(stock, stocks.getOrDefault(stock, 0.0) + sharesToBuy);
      }
    }

    stocks.keySet().retainAll(targetWeights.keySet());
  }

  @Override
  public void savePortfolio(String date) {
    try {
      PortfolioWriter writer = new PortfolioWriter(name, date, "src/data/portfolios");
      for (Stock s : stocks.keySet()) {
        writer.writeStock(s.toString(), stocks.get(s));
      }
      writer.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not save portfolio: " + e.getMessage());
    }
  }

  @Override
  public void addTransaction(Transaction transaction) throws IllegalArgumentException {
    if (!"rebalance".equalsIgnoreCase(transaction.getType())) {
      if (!transactions.isEmpty() && transaction.getDate().isBefore(transactions.get(transactions.size() - 1).getDate())) {
        throw new IllegalArgumentException("Transaction date cannot be before the latest transaction date.");
      }

      for (Transaction t : transactions) {
        if (t.getStock().equals(transaction.getStock()) &&
                t.getDate().getMonth() == transaction.getDate().getMonth() &&
                t.getDate().getYear() == transaction.getDate().getYear()) {
          if ("sell".equalsIgnoreCase(transaction.getType()) &&
                  "buy".equalsIgnoreCase(t.getType()) &&
                  transaction.getShares() > t.getShares()) {
            throw new IllegalArgumentException("Cannot sell more shares than were bought in the same month.");
          }
        }
      }

      transactions.add(transaction);
      transactions.sort(Comparator.comparing(Transaction::getDate));
    }

    Stock stock = transaction.getStock();
    double shares = transaction.getShares();
    if ("buy".equalsIgnoreCase(transaction.getType()) || "rebalance".equalsIgnoreCase(transaction.getType())) {
      stocks.merge(stock, shares, Double::sum);
    } else if ("sell".equalsIgnoreCase(transaction.getType())) {
      double currentShares = stocks.getOrDefault(stock, 0.0);
      if (shares > currentShares) {
        throw new IllegalArgumentException("Cannot remove more shares than the number present.");
      }
      stocks.merge(stock, -shares, Double::sum);
      if (stocks.get(stock) <= 0.001) {
        stocks.remove(stock);
      }
    }
  }

  @Override
  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }
}