package controller;

import java.io.IOException;
import java.util.Map;

import model.Portfolio;
import model.Stock;

/**
 * The controller interface for the program.
 * Used to start the program and begin the user interface.
 * Contains the necessary methods to interact with the user.
 */
public interface Controller {

  /**
   * Start the controller and begin the program.
   */
  void controllerGo();

  /**
   * Get a stock with the given symbol from the model.
   *
   * @param symbol the symbol of the stock to get.
   * @return the stock with the given symbol.
   * @throws IllegalArgumentException if the stock is not found by the model.
   */
  Stock getStock(String symbol) throws IllegalArgumentException;

  /**
   * Get a map of portfolios from the model.
   *
   * @return the map of portfolios with the given name.
   */
  Map<String, Portfolio> getPortfolios();

  /**
   * Get the next input from the user.
   *
   * @return the next input from the user.
   * @throws IOException if the input cannot be read.
   */
  String next() throws IOException;

  /**
   * Get the next int from the user.
   *
   * @return the next input from the user as an int.
   */
  int nextInt();

  /**
   * Get the next double from the user.
   *
   * @return the next input from the user as a double.
   */
  double nextDouble();
  /**
   * Skips the next line of input from the user.
   * Used to skip the newline character after a command.
   *
   * @return the next input from the user as a string.
   */
  String nextLine();

  /**
   * Get the appendable object for the controller.
   *
   * @return the appendable object for the controller.
   */
  Appendable getAppendable();

  /**
   * Creates a new portfolio in the model with the given name.
   *
   * @param name the name of the portfolio to create.
   */
  void makePortfolio(String name);
}
