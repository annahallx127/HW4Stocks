package model;

import java.util.Set;

/**
 * An interface for a stock.
 * Each stock has a name, a ticker symbol, opening price, closing price, low price, and high price.
 */
public interface Stock {
  /**
   * Finds the total change in the stock's value over the given days.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the total change in value since the stock has been tracked.
   *
   * @param dateStart the date to start from.
   *                  Must be in a valid date format "YYYY-MM-DD".
   *                  Must be before dateEnd.
   * @param dateEnd   the date to stop at.
   *                  Must be a valid date in the format "YYYY-MM-DD".
   *                  Must be after dateStart.
   * @return the change in the stock's value in USD.
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY-MM-DD".
   *                                  or if the start date is after the end date.
   */
  double gainedValue(String dateStart, String dateEnd) throws IllegalArgumentException;

  /**
   * Finds the stock's X-day moving average.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the average value since the stock has been tracked.
   *
   * @param days the number of days to look back to find the average value.
   *             Must be greater than 0.
   * @param date the date to start looking back from.
   *             Must be a valid date in the format "YYYY-MM-DD".
   * @return the average value of the stock in USD.
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY-MM-DD",
   *                                  or if the number of days is less than or equal to 0.
   */
  double getMovingAverage(int days, String date) throws IllegalArgumentException;

  /**
   * Finds the X-day crossovers for the stock over the given date range.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the crossovers since the stock has been tracked.
   *
   * @param days      the number of days to look back to find the crossovers.
   *                  Must be greater than 0.
   * @param dateStart the date to start looking back from.
   *                  Must be a valid date in the format "YYYY-MM-DD".
   *                  Must be before dateEnd.
   * @param dateEnd   the date to end looking back from.
   *                  Must be a valid date in the format "YYYY-MM-DD".
   *                  Must be after dateStart.
   * @return the dates of the crossovers in a String.
   * @throws IllegalArgumentException if the date is not a valid date,
   *                                  or if the number of days is less than or equal to 0.
   */
  String getCrossovers(String dateStart, String dateEnd, int days) throws IllegalArgumentException;

  /**
   * Gets the symbol, name, opening price, closing price, low price, and high price of the stock
   * on a given date.
   *
   * @param date the date to get the stock values on.
   *             Must be a valid date in the format "YYYY-MM-DD".
   *             Must be a date the stock has been tracked on.
   * @return the stock values as a string
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY-MM-DD"
   *                                  or if the date is not a date the stock has been tracked on.
   */
  String toString(String date) throws IllegalArgumentException;

  /**
   * Gets the symbol of the stock.
   *
   * @return the stock as a string
   */
  String toString();

  /**
   * Checks if the date entered by the user is within the scope of the dates in the
   * API. Will throw an IllegalArgumentException if the date is in the future or if
   * the format is not YYYY-MM-DD format. Also checks if date is the weekend, if it is
   * it will get the friday before the weekend and return true, so that the user
   * can keep going.
   *
   * @param date the date specified by the user.
   * @return the validity of the entered date in boolean form.
   */
  boolean isValidDate(String date);

  /**
   * Finds the price of the specific stock on a valid specified date.
   * It will present the closing price of that stock on that date.
   *
   * @param date the date the user wants to get the price from
   * @return the price of the stock at that date as a double
   */
  double getPriceOnDate(String date);

  /**
   * Checks if the symbol is a valid symbol.
   *
   * @param symbol the symbol to check.
   * @throws IllegalArgumentException if the symbol is not valid.
   */
  void isValidSymbol(String symbol) throws IllegalArgumentException;

  /**
   * Checks if this stock is equal to the given object.
   *
   * @param o the object to compare this stock to.
   * @return true if the object is equal to this stock, false otherwise.
   */
  boolean equals(Object o);

  /**
   * Returns a hash code value for the stock.
   *
   * @return a hash code value for this stock.
   */
  int hashCode();

  /**
   * Parses the valid symbols from the given file.
   *
   * @param filePath the path to the file containing the valid symbols.
   * @return a set of valid symbols.
   */
  Set<String> parseValidSymbols(String filePath);
}
