package controller;

import java.util.Map;
import java.util.Scanner;

import model.Stock;
import model.Portfolio;
import view.View;

/**
 * The Controller interface for managing user interactions in the stock trading application.
 * It defines methods for executing stock commands, displaying messages, and managing portfolios.
 */
public interface Controller {

  /**
   * Executes a specific command related to stock transactions or portfolio management.
   * The command to be executed is passed as an argument, allowing for various operations
   * such as buying or selling stocks, or adjusting portfolio settings.
   *
   * @param command the command to execute, encapsulating the necessary action and parameters.
   */
  void executeCommand(ControllerCommand command);

  /**
   * Displays a message to the user through the designated view. This method can be used
   * to provide feedback, results of operations, or general information.
   *
   * @param message the message to be displayed to the user.
   */
  void displayMessage(String message);

  /**
   * Displays an error message to the user, specifically for error notifications
   * and exception handling feedback through the designated view.
   *
   * @param message the error message to be displayed.
   */
  void displayError(String message);

  /**
   * Retrieves a Stock object using its ticker symbol. This method allows the controller
   * to fetch stock data which can be used in various operations such as transactions or analyses.
   *
   * @param ticker the ticker symbol of the stock to retrieve.
   * @return the Stock object associated with the given ticker symbol.
   */
  Stock getStock(String ticker);

  /**
   * Retrieves a map of all portfolios managed within the application. This can be used
   * for displaying portfolio summaries or for operations on multiple portfolios.
   *
   * @return a map where each key is a portfolio name and each value is the corresponding Portfolio object.
   */
  Map<String, Portfolio> getPortfolios();

  /**
   * Creates a new portfolio with a specified name. This method is responsible for initializing
   * and registering a new portfolio in the system.
   *
   * @param name the name of the new portfolio to create.
   */
  void makePortfolio(String name);

  /**
   * Saves the current state of a portfolio to persistent storage, typically a file, based on
   * the given name and date. This method ensures that portfolio data can be retrieved at a later time.
   *
   * @param name the name of the portfolio to save.
   * @param date the date on which the portfolio is saved.
   */
  void savePortfolio(String name, String date);

  /**
   * Loads a portfolio from persistent storage based on its name. This method is useful for retrieving
   * previously saved portfolio states.
   *
   * @param name the name of the portfolio to load.
   * @param path the file path where the portfolio data is stored.
   */
  void loadPortfolio(String name, String path);

  /**
   * Starts the main loop of the controller, processing user inputs and commands through the given
   * scanner and executing operations as directed. This loop continues until it is explicitly terminated
   * by the user or an exit command is executed.
   *
   * @param controller the controller instance to run this method on.
   * @param view       the view interface for user interactions and displaying results.
   * @param scanner    a Scanner object for reading user input.
   */
  void runController(Controller controller, View view, Scanner scanner);
}
