package mocks;
import java.util.HashMap;
import java.util.Map;

import model.Model;
import model.Portfolio;
import model.Stock;

/**
 * A mock implementation of the Model interface for testing purposes.
 * This class provides basic implementations of the methods defined in ModelImpl,
 * facilitating unit tests by simulating the behavior of the actual model.
 * It manages a collection of portfolios and stocks, allowing for testing
 * portfolio and stock-related functionalities without relying on real data.
 */
public class MockModel implements Model {

  private final Map<String, Portfolio> portfolios = new HashMap<>();

  private final Map<String, Stock> stocks = new HashMap<>();

  @Override
  public Stock get(String ticker) {
    return stocks.get(ticker);
  }

  @Override
  public void makePortfolio(String name) {
    Portfolio portfolio = new MockPortfolio(name);
    portfolios.put(name, portfolio);
  }

  /**
   * Puts the portfolio into the collection of portfolios/Map
   *
   * @param name of the portfolio
   * @param portfolio the portfolio and its components
   */
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
