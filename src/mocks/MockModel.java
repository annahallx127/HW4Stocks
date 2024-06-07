package mocks;
import java.util.HashMap;
import java.util.Map;

import model.Model;
import model.Portfolio;
import model.Stock;

public class MockModel implements Model {
  private final Map<String, Portfolio> portfolios = new HashMap<>();
  private final Map<String, Stock> stocks = new HashMap<>();

  @Override
  public Stock get(String ticker) {
    return stocks.get(ticker);
  }

  @Override
  public Portfolio makePortfolio(String name) {
    Portfolio portfolio = new MockPortfolio(name);
    portfolios.put(name, portfolio);
    return portfolio;
  }

  public void addPortfolio(String name, Portfolio portfolio) {
    portfolios.put(name, portfolio);
  }

  @Override
  public Map<String, Portfolio> getPortfolios() {
    return Map.copyOf(portfolios);
  }

  /**
   * Manually adds a stock to the overall hashmap of mock stocks.
   * For the purposes of mock testing only.
   *
   * @param ticker the symbol of the stock.
   * @param stock the components of the specified stock.
   */
  public void addMockStock(String ticker, Stock stock) {
    stocks.put(ticker, stock);
  }
}
