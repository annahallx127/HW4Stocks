package view;

import controller.Controller;
import model.Portfolio;
import model.Stock;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * The ViewImpl class implements the View interface and provides a concrete implementation
 * for interacting with the user in the stock investment application. It handles and responds to
 * user inputs nd displays the options and results. View also handle what the user is able
 * to see on their end. This class interacts with the Controller to
 * perform the operations the user has chosen such as; calculating the gain or loss of a stock,
 * moving averages, crossovers, creating portfolios, and viewing existing portfolios.
 * Through the text-based user interface, the user is able to interact with the application.
 */
public class ViewImpl implements View {
  private final Appendable out;

  /**
   * Constructs a ViewImpl instance with the specified controller.
   * Initializes the output appendable and starts the main interaction loop with the run() method.
   *
   */
  public ViewImpl(Appendable out) {
    this.out = out;
  }

  /**
   * Private function that System.out.printlns a message to the output appendable.
   * Appends the message followed by a new line separator
   * If an IOException occurs, it wraps the exception in a RuntimeException and rethrows it.
   *
   * @param message message the message to be System.out.printlned
   */
  public void print(String message) {
    try {
      out.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to append output", e);
    }
  }
}