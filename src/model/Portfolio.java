package model;

import java.util.List;
import java.util.Map;

/**
 * Represents a portfolio in the stock investment program. Every portfolio can have multiple stocks
 * with the respective number of whole shares. The portfolio object is responsible for adding and
 * removing stocks from the portfolio, finding the total value of the portfolio on a given date, and
 * checking if the date is a valid date for each stock in the portfolio.
 */
public interface Portfolio {

  /**
   * Gets all the stocks in the portfolio with the respective number of shares.
   *
   * @return a map of the stocks in the portfolio
   */
  Map<Stock, Double> getStocks();

  /**
   * Sets the name of the portfolio.
   *
   * @param name the name of the portfolio.
   */
  void setName(String name);

  /**
   * Gets the name of the portfolio that the user created.
   *
   * @return the name of the portfolio.
   */
  String getName();

  /**
   * Adds a stock to the portfolio. Users should not be able to add a partial share of a stock, nor
   * should they be able to add a stock with a negative number of shares. However, this method is
   * also used to balance the portfolio, so the number of shares may be a floating point number if
   * the portfolio is being rebalanced.
   *
   * @param s      the stock to add to the portfolio.
   * @param shares the number of shares bought.
   */
  void add(Stock s, double shares);

  /**
   * Removes a stock from the portfolio.
   * If the stock is not in the portfolio, the method will throw an IllegalArgumentException.
   *
   * @param s      the stock to remove from the portfolio.
   * @param shares the number of shares wanting to remove.
   * @throws IllegalArgumentException if then number of shares removed is greater than the
   *                                  number of shares owned, or if the stock is not
   *                                  in the portfolio.
   */
  void remove(Stock s, double shares) throws IllegalArgumentException;

  /**
   * Finds the total value of the portfolio on the given date.
   *
   * @param date the date to find the total value of the portfolio on.
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY/MM/DD".
   */
  double valueOfPortfolio(String date) throws IllegalArgumentException;

  /**
   * Prints out the whole portfolio's stocks.
   *
   * @return the portfolio's stocks in a string.
   */
  String toString();

  /**
   * Checks if the date is a valid date for each stock in the portfolio.
   *
   * @param date the date to check if it is a valid date.
   * @return true if the date is a valid date, false otherwise.
   */
  boolean isValidDateForPortfolio(String date);

  /**
   * Finds the value distribution of a portfolio at a given date.
   * Returns a String of the stocks that are valid on that date, the stock values at that
   * date, and the total value of the portfolio at that date.
   *
   * @param date the user specified date of which to check the distribution
   * @return the value distribution of the portfolio
   */
  String getValueDistribution(String date);


  String getCompositionAtDate(String date);
  String getPerformanceOverTime(String startDate, String endDate);

  void reBalancePortfolio();
  void savePortfolio();
  void loadPortfolio();

  /**
   * Adds a transaction to the portfolio.
   *
   * @param transaction the transaction to add
   * @throws IllegalArgumentException if the transaction date is before the latest transaction
   */
  void addTransaction(Transaction transaction);

  /**
   * Gets all transactions in the portfolio.
   *
   * @return a list of transactions
   */
  List<Transaction> getTransactions();
}