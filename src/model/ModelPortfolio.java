package model;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import parser.PortfolioReader;
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

  @Override
  public void add(Stock s, double shares) {
    if (shares <= 0) {
      throw new IllegalArgumentException("Shares added must be one or more.");
    }
    stocks.put(s, stocks.getOrDefault(s, 0.0) + shares);
  }

  @Override
  public void remove(Stock s, double shares) throws IllegalArgumentException {
    if (!stocks.containsKey(s)) {
      throw new IllegalArgumentException("Cannot remove a stock that doesn't exist.");
    }

    double currentShares = stocks.get(s);
    if (shares > currentShares) {
      throw new IllegalArgumentException("Cannot remove more shares than "
              + "the number of shares present.");
    }

    if (currentShares - shares <= 0.001) {
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

  // add if date is before first transaction, the value is 0
  @Override
  public double valueOfPortfolio(String date) {
    LocalDate validDate = getValidMarketDateWeekend(date);

//    if (validDate.isBefore())

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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    try {
      LocalDate parsedDate = LocalDate.parse(date, formatter);
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
              Math.ceil(stockValue / totalValue) * 100);
      // math.ceil or math.floor?

      distribution.put(stock.toString(), ", " + stockInfo + "%");
    }

    if (Math.abs(calculatedTotalValue - totalValue) > 0.0001) {
      throw new IllegalArgumentException(
              "Total Distribution Value != Match Total Value of Portfolio");
    }

    distribution.put("Total Portfolio Value", String.format("$%.4f", totalValue));

    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, String> entry : distribution.entrySet()) {
      result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    return result.toString();
  }


  @Override
  public String getCompositionAtDate(String date) {
    LocalDate validDate = getValidMarketDateWeekend(date);

    return "";
  }

  //  check for valid date in view or here?
  // plots the asterisk
  @Override
  public String getPerformanceOverTime(String startDate, String endDate) {

    LocalDate validStartDate = getValidMarketDateWeekend(startDate);
    LocalDate validEndDate = getValidMarketDateWeekend(endDate);

    try {
      isValidDateForPortfolio(validEndDate.toString());
      isValidDateForPortfolio(validStartDate.toString());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);


    return "";
  }

  @Override
  public void reBalancePortfolio() {

  }

  @Override
  public void savePortfolio() {
    PortfolioWriter writer = new PortfolioWriter(name);
    for (Stock s : stocks.keySet()) {
      // TODO: add date impl for this
      writer.writeStock("", s.toString(), stocks.get(s), s.getPriceOnDate(""));
    }
    writer.close();
  }

  @Override
  public void loadPortfolio() {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      DefaultHandler reader = new PortfolioReader(name);
      saxParser.parse("src/data/portfolios/" + name + ".xml", reader);
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  @Override
  public void addTransaction(Transaction transaction) throws IllegalArgumentException {
    // Ensure no transaction is before the latest transaction
    if (!transactions.isEmpty() && transaction.getDate().isBefore(transactions.get(transactions.size() - 1).getDate())) {
      throw new IllegalArgumentException("Transaction date cannot be before the latest transaction date.");
    }

    transactions.add(transaction);
    transactions.sort(Comparator.naturalOrder());

    Stock stock = transaction.getStock();
    double shares = transaction.getShares();

    if (transaction.getType().equalsIgnoreCase("buy")) {
      add(stock, shares);
    } else if (transaction.getType().equalsIgnoreCase("sell")) {
      remove(stock, shares);
    } else {
      throw new IllegalArgumentException("Invalid transaction type.");
    }
  }

  @Override
  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }
}