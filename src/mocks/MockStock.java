package mocks;

import java.util.HashMap;
import java.util.Map;

import model.PlotInterval;
import model.Stock;

/**
 * A mock implementation of the Stock interface for testing purposes.
 * This class simulates the behavior of a real stock, managing stock prices
 * over time and providing methods to retrieve stock information and perform
 * calculations based on mock data.
 */
public class MockStock implements Stock {

  private final String symbol;

  private final Map<String, Double> prices = new HashMap<>();

  /**
   * Constructs a new MockStock with the specified ticker symbol.
   *
   * @param ticker the ticker symbol of the stock
   */
  public MockStock(String ticker) {
    this.symbol = ticker;
  }

  @Override
  public double gainedValue(String dateStart, String dateEnd) {
    return getPriceOnDate(dateEnd) - getPriceOnDate(dateStart);
  }

  @Override
  public double getMovingAverage(int days, String date) {
    return prices.values().stream().mapToDouble(Double::doubleValue).limit(days).average()
            .orElse(0.0);
  }

  @Override
  public String getCrossovers(String dateStart, String dateEnd, int days) {
    return "Mock crossover data";
  }

  @Override
  public String toString(String date) {
    double price = getPriceOnDate(date);
    return String.format("%s - %s: %s", symbol, date, price);
  }

  @Override
  public String toString() {
    return symbol;
  }

  @Override
  public boolean isValidDate(String date) {
    return prices.containsKey(date);
  }

  @Override
  public double getPriceOnDate(String date) {
    return prices.getOrDefault(date, 0.0);
  }

  // fix this later os that it calls the ones in the api
  @Override
  public void isValidSymbol(String symbol) {
  }

  /**
   * Manually set the price of the stock on the specified date.
   * For the purposes of mock testing only.
   *
   * @param date  the date of the stock price.
   * @param price the price of the stock on that date.
   */
  public void setPriceOnDate(String date, double price) {
    prices.put(date, price);
  }
}
