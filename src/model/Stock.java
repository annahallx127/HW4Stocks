package model;

/**
 * An interface for a stock.
 * Each stock has a name, a ticker symbol, opening price, closing price, low price, and high price.
 */
public interface Stock {
  /**
   * Determines if the stock gained or lost value over the given period,
   * and returns the change in value.
   *
   * @param dateStart the date to start from.
   *                  Must be in a valid date format "YYYY/MM/DD".
   *                  Must be before dateEnd.
   * @param dateEnd   the date to stop at.
   *                  Must be a valid date in the format "YYYY/MM/DD".
   *                  Must be after dateStart.
   * @return the change in the stock's value in USD.
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY/MM/DD".
   *                                  or if the start date is after the end date.
   */
  public double gainedValue(String dateStart, String dateEnd) throws IllegalArgumentException;

  /**
   * Finds the total change in the stock's value over the given days.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the total change in value since the stock has been tracked.
   *
   * @param dateStart the date to start looking back from.
   *                  Must be a valid date in the format "YYYY/MM/DD".
   *                  Must be before dateEnd.
   * @param dateEnd   the date to end looking back from.
   *                  Must be a valid date in the format "YYYY/MM/DD".
   *                  Must be after dateStart.
   * @return the total change in the stock's value in USD.
   * @throws IllegalArgumentException if the number of days is less than or equal to 0.
   */
  public double getChange(String dateStart, String dateEnd) throws IllegalArgumentException;

  /**
   * Finds the stock's X-day moving average.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the average value since the stock has been tracked.
   *
   * @param days the number of days to look back to find the average value.
   *             Must be greater than 0.
   * @param date the date to start looking back from.
   *             Must be a valid date in the format "YYYY/MM/DD".
   * @return the average value of the stock in USD.
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY/MM/DD",
   *                                  or if the number of days is less than or equal to 0.
   */
  public double getMovingAverage(int days, String date) throws IllegalArgumentException;

  /**
   * Finds the X-day crossovers for the stock over the given date range.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the crossovers since the stock has been tracked.
   *
   * @param days      the number of days to look back to find the crossovers.
   *                  Must be greater than 0.
   * @param dateStart the date to start looking back from.
   *                  Must be a valid date in the format "YYYY/MM/DD".
   *                  Must be before dateEnd.
   * @param dateEnd   the date to end looking back from.
   *                  Must be a valid date in the format "YYYY/MM/DD".
   *                  Must be after dateStart.
   * @return the dates of the crossovers in an array in the format "YYYY/MM/DD".
   * @throws IllegalArgumentException if the date is not a valid date,
   *                                  or if the number of days is less than or equal to 0.
   */
  public String[] getCrossovers(String dateStart, String dateEnd, int days) throws IllegalArgumentException;

  /**
   * Gets the symbol of the stock.
   *
   * @return the symbol of the stock.
   */
  public String getSymbol();

  /**
   * Gets the symbol, name, opening price, closing price, low price, and high price of the stock on a given date.
   * @param date the date to get the stock values on.
   *             Must be a valid date in the format "YYYY/MM/DD".
   *             Must be a date the stock has been tracked on.
   * @return the stock values as a string
   * @throws IllegalArgumentException if the date is not a valid date in the format "YYYY/MM/DD"
   *                                 or if the date is not a date the stock has been tracked on.
   */
  public String toString(String date) throws IllegalArgumentException;
//  public Date date();
}