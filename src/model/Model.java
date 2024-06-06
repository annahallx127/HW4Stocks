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
   * @throws IllegalArgumentException if the symbol given is not a stock
   */
  Stock get(String symbol) throws IllegalArgumentException;

  /**
   * Create a portfolio with the given name.
   *
   * @param name the name of the portfolio
   * @return the portfolio created
   */
  Portfolio makePortfolio(String name);

  /**
   * Gets the portfolios in the hashmap with the names of the portfolios and
   * the collection of stocks within it.
   *
   * @return a map of all existing portfolios and its components
   */
  HashMap<String, Portfolio> getPortfolios();

  /**
   * Adds a portfolio to the HashMap with its respective components; name and the stocks
   * under the portfolio name.
   *
   * @param name name of the portfolio
   * @param portfolio a portfolio/collections of the stocks
   */
  void addPortfolio(String name, Portfolio portfolio);

}
