package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;

/**
 * A command that calculates and displays the total value of a specified portfolio on
 * a given date.
 * This command is intended to provide users with a snapshot of their investment value
 * at a particular
 * point in time, helping them assess the performance and value of their investments.
 * The command includes the name of the portfolio and the date for which the value is to
 * be calculated.
 */
public class PortfolioValueCommand implements ControllerCommand {
  private final String portfolioName;
  private final String date;

  /**
   * Constructs a new PortfolioValueCommand with the specified portfolio name and date.
   *
   * @param portfolioName The name of the portfolio for which to calculate the value.
   * @param date          The date for which the portfolio's total value is calculated.
   */
  public PortfolioValueCommand(String portfolioName, String date) {
    this.portfolioName = portfolioName;
    this.date = date;
  }

  /**
   * Executes the operation to calculate and display the value of the specified portfolio
   * on the given date. It queries the controller for the portfolio and requests
   * the value calculation,
   * then displays the formatted result through the controller's view.
   * If the portfolio does not exist or the date is invalid, an appropriate error
   * message is displayed.
   *
   * @param controller The controller that interfaces with the model and view to
   *                   perform the operation.
   */
  @Override
  public void execute(Controller controller) {
    try {
      Portfolio portfolio = controller.getPortfolios().get(portfolioName);
      double value = portfolio.valueOfPortfolio(date);
      controller.displayMessage("Portfolio Value for " + portfolioName + " on "
              + date + " is: $" + String.format("%.2f", value));
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
