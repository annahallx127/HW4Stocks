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
   * Saves the portfolio to a .xml file. Files saved in this format can be loaded later, and are
   * stored in the data/portfolios folder.
   *
   * @param name the name of the portfolio
   * @param date the date to save for the stocks in the portfolio. Used to calculate the value of
   *             the portfolio when loaded.
   */
  void savePortfolio(String name, String date);

  /**
   * Loads the portfolio from a .xml file.
   *
   * @param name the name of the portfolio
   * @param path the path to the file. The file should be in .xml format and formatted correctly.
   * @throws IllegalArgumentException if the file is not found or if the file is not formatted
   *                                  correctly
   */
  void loadPortfolio(String name, String path) throws IllegalArgumentException;

  /**
   * Gets the portfolios in the Hashmap with the names of the portfolios and
   * the collection of stocks within it.
   *
   * @return a Map of all existing portfolios and its components
   */
  Map<String, Portfolio> getPortfolios();

  /**
   * Plots the graph of the portfolio with the given name.
   *
   * @param name      the name of the portfolio. Must be a valid portfolio name.
   * @param dateStart the start date of the graph. Must be a valid date in the format
   *                  "YYYY-MM-DD".
   * @param dateEnd   the end date of the graph. Must be a valid date in the format "YYYY-MM-DD".
   * @param interval  the interval of the graph. Must be a valid interval.
   * @throws IllegalArgumentException if the portfolio name is not valid, or if the dates are not
   *                                  valid.
   */
  String plotPortfolio(String name, String dateStart, String dateEnd, PlotInterval interval)
          throws IllegalArgumentException;
}
