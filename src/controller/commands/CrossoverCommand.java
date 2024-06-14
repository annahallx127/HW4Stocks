package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Stock;

/**
 * A command to calculate and report moving average crossovers for a specified stock
 * within a given date range.
 * A crossover event occurs when the stock's price crosses over its moving average,
 * which can be an indicator
 * for technical analysts to make buy or sell decisions.
 * This command includes parameters such as the stock's ticker symbol,
 * the number of days for the moving average,
 * and the start and end dates for the analysis period.
 */
public class CrossoverCommand implements ControllerCommand {
  private final String ticker;
  private final int days;
  private final String startDate;
  private final String endDate;

  /**
   * Constructs a new CrossoverCommand with specified parameters for the stock ticker,
   * number of days for the moving average, and the date range for analysis.
   *
   * @param ticker     The ticker symbol of the stock for which crossovers are to be calculated.
   * @param days       The number of days over which the moving average is to be computed.
   * @param startDate  The start date of the analysis period.
   * @param endDate    The end date of the analysis period.
   */
  public CrossoverCommand(String ticker, int days, String startDate, String endDate) {
    this.ticker = ticker;
    this.days = days;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * Executes the analysis of moving average crossovers for the specified stock and period.
   * This method retrieves the stock data from the controller's model, calculates the crossovers,
   * and displays the results through the controller's view interface.
   * If any parameters are invalid or the calculation cannot be performed, an
   * error message is displayed.
   *
   * @param controller The controller through which stock data is accessed and
   *                   results are communicated.
   */
  @Override
  public void execute(Controller controller) {
    try {
      Stock stock = controller.getStock(ticker);
      String crossovers = stock.getCrossovers(startDate, endDate, days);
      controller.displayMessage("Crossovers for " + ticker + " from "
              + startDate + " to " + endDate + " are:\n" + crossovers);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
