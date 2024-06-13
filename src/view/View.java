package view;

import model.Portfolio;

/**
 * The View interface defines the methods required for interacting with the user in the stock
 * investment application. It handles user inputs, displays options and results, and manages
 * the main interaction loop. The view is responsible for providing various functionalities of
 * a stock for a user through the text based user interface. The user will be presented
 * with options that utilize the methods in this interface to complete the user interactions.
 */
public interface View {

//  /**
//   * Initiates the view and presents the user with options.
//   * Loads the next menu based off of user inputs.
//   * Handles the main interaction loop with the user.
//   */
//  void run();

  void print(String message);
}