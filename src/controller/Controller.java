package controller;

import java.util.Map;

import controller.ControllerCommand;
import model.Stock;
import model.Portfolio;

/**
 * Interface for the stock controller in the stock trading application.
 * This interface defines the methods needed to manipulate stocks and portfolios.
 */
public interface Controller {

  /**
   * Executes a given stock command.
   *
   * @param command the command to execute.
   */
  void executeCommand(ControllerCommand command);

  /**
   * Displays a message using the view.
   *
   * @param message the message to be displayed.
   */
  void displayMessage(String message);

  /**
   * Displays an error message using the view.
   *
   * @param message the error message to be displayed.
   */
  void displayError(String message);

  /**
   * Retrieves a stock by its ticker symbol.
   *
   * @param ticker the ticker symbol of the stock.
   * @return the Stock object.
   */
  Stock getStock(String ticker);

//  /**
//   * Creates a new portfolio with a given name.
//   *
//   * @param name the name of the new portfolio.
//   * @return the newly created Portfolio object.
//   */
//  Portfolio createPortfolio(String name, String date);

  Map<String, Portfolio> getPortfolios();

  void makePortfolio(String name);

  void savePortfolio(String name, String date);

  /**
   * Loads a portfolio based on its name.
   *
   * @param name the name of the portfolio to load.
   */
  void loadPortfolio(String name, String path);
}
