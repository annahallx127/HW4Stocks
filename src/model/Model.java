package model;

import java.util.HashMap;
import java.util.Map;

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
   * @return the portfolio created and added to the Hashmap
   */
  Portfolio makePortfolio(String name);

  /**
   * Gets the portfolios in the Hashmap with the names of the portfolios and
   * the collection of stocks within it.
   *
   * @return a Map of all existing portfolios and its components
   */
  Map<String, Portfolio> getPortfolios();
}
