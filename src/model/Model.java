package model;

import java.util.Map;

/**
 * The Model interface defines the core functionalities of the model that is required
 * for managing stocks and portfolios. Implementations of this interface are responsible
 * for retrieving stock data, creating portfolios, and managing collections of portfolios
 * and stocks.
 */
public interface Model {

  /**
   * Returns a stock with the given symbol. If the current stock is not present in the list of
   * stocks, this method calls the API to get information on the stock. If the stock is invalid,
   * then it throws an IllegalArgumentException.
   *
   * @param symbol tne ticker symbol of the stock
   * @return the stock the symbol represents
   * @throws IllegalArgumentException if the symbol given is not a valid stock
   */
  Stock get(String symbol) throws IllegalArgumentException;

  /**
   * Creates a portfolio with the given name and adds it to the Hashmap of portfolios.
   *
   * @param name the name of the portfolio
   */
  void makePortfolio(String name);

  /**
   * Gets the portfolios in the Hashmap with the names of the portfolios and
   * the collection of stocks within it.
   *
   * @return a Map of all existing portfolios and its components
   */
  Map<String, Portfolio> getPortfolios();
}
