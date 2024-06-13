//package controller;
//
//import controller.commands.StockCommand;
//import view.View;
//
///**
// * Controller class that handles operations on stocks and portfolios.
// */
//public class StockController {
//  private View view;
//
//  public StockController(View view) {
//    this.view = view;
//  }
//
//  /**
//   * Executes a given stock command.
//   *
//   * @param command the command to execute.
//   */
//  public void executeCommand(StockCommand command) {
//    command.execute(this);
//  }
//
//  /**
//   * Displays a message using the view.
//   *
//   * @param message the message to be displayed.
//   */
//  public void displayMessage(String message) {
//    view.print(message);
//  }
//
//  /**
//   * Displays an error message using the view.
//   *
//   * @param message the error message to be displayed.
//   */
//  public void displayError(String message) {
//    view.print("Error: " + message);
//  }
//
//  // Other methods that commands might need to interact with model or other components
//}
