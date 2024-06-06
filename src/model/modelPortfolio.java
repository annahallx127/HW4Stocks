package model;

import java.util.HashMap;
import java.util.Map;

public class modelPortfolio implements Portfolio {
  private final String name;
  private final HashMap<Stock, Integer> stocks;

  public modelPortfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
  }

  @Override
  public Map<Stock, Integer> getStocks() {
    return Map.copyOf(stocks);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void add(Stock s, int shares) {
    stocks.put(s, shares);
  }

  @Override
  public void remove(Stock s, int shares) throws IllegalArgumentException {
    // stock is not present
    if (stocks.get(s) == null) {
      throw new IllegalArgumentException("Cannot remove a stock that doesn't exist.");
    }

    // if shares > than owned num of shares, throw exception
    if (stocks.get(s) - shares < 0) {
      throw new IllegalArgumentException("Cannot remove more shares than the number of shares present.");
    }

    stocks.put(s, stocks.get(s) - shares);
  }

  @Override
  public double valueOfPortfolio(String date) {

    double value = 0.0;
    for (Map.Entry<Stock, Integer> s : stocks.entrySet()) {
      // stock price on this date * # of shares
      // split to find close price
      value += Double.parseDouble(s.getKey().toString(date).split("\\R")[5].substring(7)) * s.getValue();
    }
    return value;
  }

  @Override
  public String toString() {
    // prints the whole portfolio
    StringBuilder ret = new StringBuilder();
    for (Map.Entry<Stock,Integer> s : stocks.entrySet()) {
      // symbol: # shares
      ret.append(s.getKey().toString() + ": "
              + getStocks().get(s.getKey()) + " shares" + System.lineSeparator());
    }
    return ret.toString();
  }

}
