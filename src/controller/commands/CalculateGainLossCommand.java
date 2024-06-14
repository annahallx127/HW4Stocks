package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Stock;

/**
 * Command to calculate the gain or loss of a stock between two dates.
 * This command includes all necessary details such as the stock's ticker symbol
 * and the date range for the calculation.
 * Upon execution, it retrieves the stock data from the controller's model, computes
 * the gain or loss over the specified period,
 * and displays the result through the controller's view.
 * The calculation considers the price difference of the stock between the start and end dates.
 * If the stock data or dates are invalid, an error message is displayed.
 */
public class CalculateGainLossCommand implements ControllerCommand {
  private final String ticker;
  private final String startDate;
  private final String endDate;

  /**
   * Initializes a new instance of the CalculateGainLossCommand with specified parameters.
   *
   * @param ticker    The stock ticker symbol to be analyzed.
   * @param startDate The start date from which to calculate gain or loss.
   * @param endDate   The end date until which to calculate gain or loss.
   */
  public CalculateGainLossCommand(String ticker, String startDate, String endDate) {
    this.ticker = ticker;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * Executes the gain/loss calculation for the specified stock and date range.
   * This method retrieves the stock from the model through the controller, calculates
   * the gain or loss,
   * and uses the controller to display the result or an error message if the operation fails.
   *
   * @param controller The controller that facilitates interaction between the model and
   *                   view components.
   */
  @Override
  public void execute(Controller controller) {
    try {
      Stock stock = controller.getStock(ticker);
      double gainOrLoss = stock.gainedValue(startDate, endDate);
      controller.displayMessage("Gain/Loss for " + ticker.toUpperCase()
              + " from " + startDate + " to " + endDate + " is: $" + String.format("%.2f",
              gainOrLoss));
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
