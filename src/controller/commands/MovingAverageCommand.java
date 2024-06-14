package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Stock;

/**
 * A command to calculate and display the moving average of a stock on a specific date.
 * This command is useful
 * for financial analysis, particularly in the context of technical stock analysis where
 * moving averages are
 * commonly used to identify trends and potential buy/sell signals.
 * <p>
 * The command includes parameters for the stock's ticker symbol, the number of days for
 * averaging, and the target date
 * for the calculation. It uses the controller to access stock data and compute the moving
 * average.
 */
public class MovingAverageCommand implements ControllerCommand {
  private final String ticker;
  private final int days;
  private final String date;

  /**
   * Constructs a new MovingAverageCommand with the specified ticker symbol,
   * number of days, and date.
   *
   * @param ticker The stock ticker symbol for which the moving average is to be calculated.
   * @param days   The number of days over which the moving average is computed.
   * @param date   The date at which the moving average is calculated.
   */
  public MovingAverageCommand(String ticker, int days, String date) {
    this.ticker = ticker;
    this.days = days;
    this.date = date;
  }

  /**
   * Executes the moving average calculation for the specified stock and date.
   * This method retrieves the stock data
   * from the model via the controller, computes the moving average for the specified number
   * of days, and displays the result through the controller's view.
   * If there is an issue with the input data or the calculation, it will display an error message.
   *
   * @param controller The controller that facilitates interaction between the model and view
   *                   components, providing access to the stock data.
   */
  @Override
  public void execute(Controller controller) {
    try {
      Stock stock = controller.getStock(ticker);
      double movingAverage = stock.getMovingAverage(days, date);
      controller.displayMessage(days + "-Day Moving Average for " + ticker + " on " + date
              + " is: $" + String.format("%.2f", movingAverage));
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
