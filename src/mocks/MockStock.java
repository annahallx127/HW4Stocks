package mocks;

import java.util.HashMap;
import java.util.Map;

import model.Stock;

public class MockStock implements Stock {
  private final String ticker;
  private final Map<String, Double> prices = new HashMap<>();

  public MockStock(String ticker) {
    this.ticker = ticker;
  }

  @Override
  public double gainedValue(String dateStart, String dateEnd) {
    return getPriceOnDate(dateEnd) - getPriceOnDate(dateStart);
  }

  @Override
  public double getMovingAverage(int days, String date) {
    return prices.values().stream().mapToDouble(Double::doubleValue).limit(days).average().orElse(0.0);
  }

  @Override
  public String getCrossovers(String dateStart, String dateEnd, int days) {
    return "Mock crossover data";
  }

  @Override
  public String toString(String date) {
    double price = getPriceOnDate(date);
    return String.format("%s - %s: %s", ticker, date, price);
  }

  @Override
  public boolean isValidDate(String date) {
    return prices.containsKey(date);
  }
  @Override
  public double getPriceOnDate(String date) {
    return prices.getOrDefault(date, 0.0);
  }

  @Override
  public String toString() {
    return ticker;
  }

  /**
   * Manually set the price of the stock on the specified date.
   * For the purposes of mock testing only.
   *
   * @param date the date of the stock price.
   * @param price the price of the stock on that date.
   */
  public void setPriceOnDate(String date, double price) {
    prices.put(date, price);
  }
}
