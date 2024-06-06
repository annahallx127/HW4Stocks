package model;

import java.util.HashMap;
import java.util.Map;

public class modelPortfolio implements Portfolio {
  private String name;
  private HashMap<Stock, Integer> stocks;

  public modelPortfolio(String name) {
    this.name = name;
    this.stocks = new HashMap<>();
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
      // value += s.getKey() * s.getValue();
    }
    return value;
  }

  @Override
  public String toString() {
    // prints the whole portfolio
    StringBuilder ret = new StringBuilder();
    for (Stock s : stocks.keySet()) {
      ret.append(s.toString());
    }
    return ret.toString();
  }
}
