package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;

/**
 * A command to view and display the composition of a portfolio on a given date.
 * This command is used within the stock investment application to provide a snapshot
 * of the portfolio's holdings, including the types and quantities of stocks it contains
 * at a specified point in time.
 *
 * This command includes the date for viewing the composition and the name of the portfolio
 * whose composition is to be displayed.
 */
public class ViewCompositionCommand implements ControllerCommand {
  private final String date;
  private final String portfolioName;

  /**
   * Constructs a new ViewCompositionCommand with the specified portfolio name and date.
   *
   * @param portfolio The name of the portfolio for which the composition is to be viewed.
   * @param date The date for which the portfolio's composition is requested.
   */
  public ViewCompositionCommand(String portfolio, String date) {
    this.date = date;
    this.portfolioName = portfolio;
  }

  /**
   * Executes the operation to retrieve and display the composition of the specified portfolio
   * on the given date. It queries the controller for the portfolio and requests
   * the composition data,
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
      String composition = portfolio.getCompositionAtDate(date);
      controller.displayMessage("Portfolio Composition for " + portfolioName
              + " on " + date + " is: \n" + composition);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
