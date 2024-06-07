package model;


import java.util.Map;

public interface Portfolio {

  /**
   * Gets all the stocks in the portfolio with the respective number of shares.
   *
   * @return a map of the stocks in the portfolio
   */
  Map<Stock, Integer> getStocks();

  /**
   * Gets the name of the portfolio that the user created.
   *
   * @return the name of the portfolio
   */
  String getName();
  /**
   * Adds a stock to the portfolio.
   *
   * @param s      the stock to add to the portfolio.
   * @param shares the number of shares bought.
   */
  void add(Stock s, int shares);

  /**
   * Removes a stock from the portfolio.
   * If the stock is not in the portfolio, the method will do nothing.
   *
   * @param s      the stock to remove from the portfolio.
   * @param shares the number of shares wanting to remove.
   * @throws IllegalArgumentException if then number of shares removed is greater than the
   *                                  number of shares owned.
   */
  void remove(Stock s, int shares) throws IllegalArgumentException;

  /**
   * Finds the total value of the portfolio on the given date.
   *
   * @param date the date to find the total value of the portfolio on.
   *             Must be a valid date in the format "YYYY/MM/DD".
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY/MM/DD".
   */
  double valueOfPortfolio(String date) throws IllegalArgumentException;

  /**
   * Prints out the whole portfolio's stocks.
   * @return the portfolio's stocks in a string.
   */
  String toString();

  boolean isValidDateForPortfolio(String date);
}