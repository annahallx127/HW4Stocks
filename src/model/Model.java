package model;

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
  Stock get(String symbol);

  /**
   * Create a portfolio with the given name.
   *
   * @param name the name of the portfolio
   * @return the portfolio created
   */
  Portfolio makePortfolio(String name);

  /**
   * Queries the AlphaVantage API to find the information on a given stock. The file returned is
   * cached as a .csv file in the data package.
   * @param symbol the ticker symbol of the stock.
  */
  void apiCall(String symbol);
}
