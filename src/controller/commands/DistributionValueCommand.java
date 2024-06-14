package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;

/**
 * A command to calculate and display the value distribution of a specific
 * portfolio on a given date.
 * This command involves assessing the proportionate value of each stock within
 * the portfolio relative
 * to the total portfolio value, providing a detailed snapshot of portfolio
 * composition by value at that date.
 *
 * <p>
 * The command uses the portfolio's name to retrieve it and then fetches the distribution
 * for the specified date.
 * The results include the value and percentage of the total for each stock in the portfolio.
 * </p>
 */
public class DistributionValueCommand implements ControllerCommand {

  private final String portfolioName;
  private final String date;

  /**
   * Constructs a new DistributionValueCommand with the specified portfolio name and date.
   *
   * @param portfolioName The name of the portfolio for which to calculate the value distribution.
   * @param date          The date for which the distribution values are calculated.
   */
  public DistributionValueCommand(String portfolioName, String date) {
    this.portfolioName = portfolioName;
    this.date = date;
  }

  /**
   * Executes the operation to retrieve and calculate the value distribution of the
   * specified portfolio
   * on the given date. It queries the controller for the portfolio and requests the
   * distribution calculation,
   * then displays the formatted results through the controller's view.
   * If any errors occur during the retrieval or calculation process, an appropriate
   * error message is displayed.
   *
   * @param controller The controller that interfaces with the model and view to
   *                   perform the operation.
   */
  @Override
  public void execute(Controller controller) {
    try {
      Portfolio portfolio = controller.getPortfolios().get(portfolioName);
      String distribution = portfolio.getValueDistribution(date);
      controller.displayMessage("Portfolio Distribution for " + portfolioName + " on "
              + date + " is: \n" + distribution);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
