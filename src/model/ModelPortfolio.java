package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.PortfolioWriter;

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
  private final HashMap<Stock, Double> stocks;
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
    LocalDate validDate = getValidMarketDateWeekend(date);

    // if date is before the first transaction date, return 0
    if (!transactions.isEmpty() && validDate.isBefore(transactions.get(0).getDate())) {
      return 0.0;
    }

    double value = 0.0;
    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      double shares = entry.getValue();
      double stockPrice = stock.getPriceOnDate(validDate.toString());
      value += stockPrice * shares;
    }
    return value;
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

  // changes over time / after rebalancing
  @Override
  public String getValueDistribution(String date) {
    LocalDate validDate = getValidMarketDateWeekend(date);

    try {
      isValidDateForPortfolio(validDate.toString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    double totalValue = valueOfPortfolio(validDate.toString());

    if (totalValue == 0) {
      return "The value of the portfolio is 0 at this date, it is empty";
    }

    Map<String, String> distribution = new HashMap<>();

    double calculatedTotalValue = 0.0;
    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      double shares = entry.getValue();
      double stockPrice = stock.getPriceOnDate(validDate.toString());
      double stockValue = stockPrice * shares;
      calculatedTotalValue += stockValue;
      String stockInfo = String.format("$%.4f (%.2f%%)", stockValue,
              Math.floor(stockValue / totalValue) * 100);
      // math.ceil or math.floor?

      distribution.put(stock.toString(), ", " + stockInfo + "%");
    }

    if (Math.abs(calculatedTotalValue - totalValue) > 0.0001) {
      throw new IllegalArgumentException(
              "Total Distribution Value != Total Value of Portfolio");
    }

    distribution.put("Total Portfolio Value", String.format("$%.4f", totalValue));
    // could change to have no format, j do tostring

    StringBuilder result = new StringBuilder();
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
    try {
      isValidDateForPortfolio(validDate.toString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    Map<Stock, Double> portfolioComposition = new HashMap<>();
    for (Transaction transaction : transactions) {
      if (!transaction.getDate().isAfter(validDate)) {
        Stock stock = transaction.getStock();
        double shares = transaction.getShares();
        if (transaction.getType().equalsIgnoreCase("buy")) {
          portfolioComposition.put(stock, portfolioComposition.getOrDefault(stock, 0.0));
        } else if (transaction.getType().equalsIgnoreCase("sell")) {
          portfolioComposition.put(stock, portfolioComposition.getOrDefault(stock, 0.0));
        }
        // if transaction type is sell, it should remove? so the composition type doesn't matter?

      }
    }

    StringBuilder result = new StringBuilder();
    for (Map.Entry<Stock, Double> entry : portfolioComposition.entrySet()) {
      result.append(entry.getKey().toString()).append(": ").
              append(entry.getValue()).append(" shares").append(System.lineSeparator());
    }

    return result.toString();
  }

  // check for valid date in view or here?
  // plots the asterisk
  @Override
  public String plotPerformanceOverTime(String startDate, String endDate, PlotScale scale) {

    LocalDate validStartDate = getValidMarketDateWeekend(startDate);
    LocalDate validEndDate = getValidMarketDateWeekend(endDate);

    try {
      isValidDateForPortfolio(validEndDate.toString());
      isValidDateForPortfolio(validStartDate.toString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    return "";
  }

  // weights must be whole numbers
  // can make it chronological
  // takes a specified date
  // weight cannot be negative
  // if weight is 0? then it gets rid of stock?
  // for the view, specify to the user the format
  // ask go through the portfolio and ask the user the weight of each stock
  // then store it into a hashmap
  // if they haven't entered a weight in for a stock..?
  @Override
  public void reBalancePortfolio(String reBalanceDate, Map<Stock, Integer> targetWeights) {

    LocalDate intendedDate = LocalDate.parse(reBalanceDate, DateTimeFormatter.ISO_LOCAL_DATE);

    if (transactions.isEmpty() && intendedDate.isBefore(
            transactions.get(transactions.size() - 1).getDate())) {
      throw new IllegalArgumentException("ReBalance date cannot be before the latest transaction.");
    }

    try {
      isValidDateForPortfolio(reBalanceDate.toString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    // if not in date format? is this needed
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    String dateCheck = reBalanceDate.toString();
//    LocalDate parsedDate = LocalDate.parse(dateCheck, formatter);
//
//    if (intendedDate != parsedDate) {
//      throw new IllegalArgumentException("Invalid date format.");
//    }

    for (Map.Entry<Stock, Integer> entry : targetWeights.entrySet()) {
      int weight = entry.getValue();
      if (weight < 0 || weight > 100) {
        throw new IllegalArgumentException("Each weight must target weight must be between 0 and 100");
      }
    }

    int totalWeight = targetWeights.values().stream().mapToInt(Integer::intValue).sum();
    if (totalWeight != 100) {
      throw new IllegalArgumentException("Error: The total weight of all stocks must add up to 100%.");
    }

    double totalValue = valueOfPortfolio(reBalanceDate.toString());

    Map<Stock, Double> currentValues = new HashMap<>();
    for (Map.Entry<Stock, Double> entry : stocks.entrySet()) {
      Stock stock = entry.getKey();
      double shares = entry.getValue();
      double stockPrice = stock.getPriceOnDate(reBalanceDate.toString());
      currentValues.put(stock, shares * stockPrice);
    }

    Map<Stock, Double> targetValues = new HashMap<>();
    for (Map.Entry<Stock, Integer> entry : targetWeights.entrySet()) {
      Stock stock = entry.getKey();
      int intendedWeight = entry.getValue();
      targetValues.put(stock, totalValue * intendedWeight / 100);
    }

    for (Stock stock : targetValues.keySet()) {
      double currentValue = currentValues.getOrDefault(stock, 0.0);
      double targetValue = targetValues.get(stock);
      double stockPrice = stock.getPriceOnDate(reBalanceDate.toString());

      //sell
      if (currentValue > targetValue) {
        double valueDifferenceToSell = currentValue - targetValue;
        double sharesToSell = valueDifferenceToSell/stockPrice;
        remove(stock, sharesToSell, reBalanceDate);
      }

      //buy
      if (currentValue < targetValue) {
        double valueDifferenceToBuy = targetValue - currentValue;
        double sharesToBuy = valueDifferenceToBuy / stockPrice;
        add(stock, sharesToBuy, reBalanceDate);
      }
    }
  }

  @Override
  public void savePortfolio(String date) {
    PortfolioWriter writer = new PortfolioWriter(name, date, "src/data/portfolios");
    for (Stock s : stocks.keySet()) {
      writer.writeStock(s.toString(), stocks.get(s));
    }
    writer.close();
  }

  @Override
  public void addTransaction(Transaction transaction) throws IllegalArgumentException {
    // no transaction is before the latest transaction
    if (!transactions.isEmpty() && transaction.getDate().
            isBefore(transactions.get(transactions.size() - 1).getDate())) {
      throw new IllegalArgumentException("Transaction date cannot be " +
              "before the latest transaction date.");
    }

    // check if transaction is illegal within the same month
    for (Transaction t : transactions) {
      if (t.getStock().equals(transaction.getStock()) && t.getDate().getMonth()
              == transaction.getDate().getMonth() && t.getDate().getYear()
              == transaction.getDate().getYear()) {
        if (transaction.getType().equalsIgnoreCase("sell")
                && t.getType().equalsIgnoreCase("buy") && transaction.getShares()
                > t.getShares()) {
          throw new IllegalArgumentException("Cannot sell more shares " +
                  "than the number of shares present in the same month.");
        }
      }
    }

    transactions.add(transaction);
    transactions.sort(transaction);

    Stock stock = transaction.getStock();
    double shares = transaction.getShares();

    if (transaction.getType().equalsIgnoreCase("buy")) {
      stocks.put(stock, stocks.getOrDefault(stock, 0.0) + shares);
    } else if (transaction.getType().equalsIgnoreCase("sell")) {
      double currentShares = stocks.get(stock);
      if (shares > currentShares) {
        throw new IllegalArgumentException("Cannot remove more shares than the " +
                "number of shares present.");
      }
      if (currentShares - shares <= 0.001) {
        stocks.remove(stock);
      } else {
        stocks.put(stock, currentShares - shares);
      }
    } else {
      throw new IllegalArgumentException("Invalid transaction type.");
    }
  }


  @Override
  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }
}