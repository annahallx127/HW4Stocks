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
  void add(Stock s, double shares, String date);

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
  void remove(Stock s, double shares, String date) throws IllegalArgumentException;

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
   * @return the value distribution of the portfolio at that date
   */
  String getValueDistribution(String date);

  /**
   * Determines the composition of a portfolio at a specific date. Taking into account that
   * portfolios may change over time. The composition includes a list of the stocks and the number
   * of shares of each stock.
   *
   * @param date the user specified date of which to check the composition
   * @return the composition of the portfolio at that date
   */
  String getCompositionAtDate(String date);

  /**
   * A visualization of how the portfolio has performed over a period of time. This chart includes
   * the time stamp specification and a measure of the value of the portfolio at each time stamp
   * using asterisks. The more asterisks there are next to the time stamp, the better the
   * portfolio performed at that time. These asterisks represent the dollar amount of the
   * portfolio, however many asterisks * the set dollar scale is a representation of the total
   * value of the portfolio at that time stamp. Disclaimer: The numerical value of the calculation
   * is not the exact value.
   *
   * @param startDate the user specified start date of which to examine the portfolio performance.
   * @param endDate the user specified end date of which to examine the portfolio performance.
   * @param scale the timescale on which to plot the graph
   * @return a bar chart representation of the portfolio's performance over the specified
   * time period.
   */
  String plotPerformanceOverTime(String startDate, String endDate, PlotScale scale);

  /**
   * A way for the user to shift the weights of their portfolio so that they are able to invest
   * more or less in different stocks of their portfolio. The user is able to specify
   * the weights of the stock so that a buy and sell transaction are made when this method
   * is called. This involves selling some stock of companies that are over-represented to
   * buy stock of companies that are under-represented. Since stock lose and gain value over time,
   * re-balancing their portfolio will allow them to keep meeting their goals.
   */
  void reBalancePortfolio(String reBalanceDate, Map<Stock, Integer> weightsOfStocks);

  /**
   * Saves a portfolio to disk as an XML file of the same name. The format can be found in the
   * design documentation, but generally saves the portfolios current stocks, number of shares, and,
   * value on a given date.
   *
   * @param date the date to use as a reference for the portfolio's stocks
   */
  void savePortfolio(String date);

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