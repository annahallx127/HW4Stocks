/**
 * An interface for a stock.
 * Each stock has a name, a ticker symbol, opening price, closing price, low price, and high price.
 */
public interface Stock {
  /**
   * Finds if the stock gained or lost value over the given day.
   *
   * @return true if the stock gained value, false if the stock lost value
   */
  public boolean gainedValue();

  /**
   * Finds the total change in the stock's value over the given days.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the total change in value since the stock has been tracked.
   *
   * @param days the number of days to look back to find the change in value.
   *             Must be greater than 0.
   * @return the total change in the stock's value in USD.
   * @throws IllegalArgumentException if the number of days is less than or equal to 0.
   */
  public double getChange(int days);

  /**
   * Finds the stock's X-day moving average.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the average value since the stock has been tracked.
   *
   * @param days the number of days to look back to find the average value.
   *             Must be greater than 0.
   * @param date the date to start looking back from.
   *             Must be a valid date in the format "MM/DD/YYYY".
   * @return the average value of the stock in USD.
   * @throws IllegalArgumentException if the date is not a valid date in the format "MM/DD/YYYY",
   *                                  If the number of days is less than or equal to 0.
   */
  public double getMovingAverage(int days, String date);

  /**
   * Finds the X-day crossovers for the stock over the given days. and a given date range.
   * If the number of days is greater than the number of days the stock has been tracked,
   * the method will return the crossovers since the stock has been tracked.
   *
   * @param days the number of days to look back to find the crossovers.
   *             Must be greater than 0..
   */
  public String[] getCrossovers(String dateStart, String dateEnd, int days);
}
