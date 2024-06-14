package controller;

import java.util.Map;
import java.util.Scanner;

import controller.ControllerCommand;
import model.Stock;
import model.Portfolio;
import view.View;

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


  /**
   * Retrieves the map of portfolios.
   *
   * @return the map of portfolios.
   */
  Map<String, Portfolio> getPortfolios();

  /**
   * Creates a new portfolio with a given name.
   *
   * @param name the name of the new portfolio.
   */
  void makePortfolio(String name);

  /**
   * Saves a portfolio to a file.
   *
   * @param name the name of the portfolio to save.
   * @param date the date to save the portfolio.
   */
  void savePortfolio(String name, String date);

  /**
   * Loads a portfolio based on its name.
   *
   * @param name the name of the portfolio to load.
   * @param path the path to the portfolio file.
   */
  void loadPortfolio(String name, String path);

  /**
   * Runs the main interaction loop, taking user commands and executing them.
   *
   * @param controller the controller to run.
   * @param view the view to display messages.
   * @param scanner the scanner to read user input.
   */
  void runController(Controller controller, View view, Scanner scanner);
}
