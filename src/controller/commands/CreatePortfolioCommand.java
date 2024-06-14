package controller.commands;

import controller.Controller;
import controller.ControllerCommand;

/**
 * A command that facilitates the creation of a new portfolio within the stock investment program.
 * This command includes the name of the portfolio to be created and interacts with the controller
 * to execute the creation process.
 * Upon execution, this command will invoke the controller's method to create a new portfolio
 * with the specified name.
 * If the portfolio is created successfully, a confirmation message is displayed.
 * If the process fails due to invalid input or other issues, an appropriate error message
 * is displayed.
 */
public class CreatePortfolioCommand implements ControllerCommand {
  private final String portfolioName;  // The name of the portfolio to be created.

  /**
   * Constructs a new CreatePortfolioCommand with the specified portfolio name.
   *
   * @param portfolioName The name of the portfolio to create. This name should be unique
   *                      within the application context.
   */
  public CreatePortfolioCommand(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  /**
   * Executes the command to create a new portfolio by invoking the controller's method
   * for portfolio creation.
   * It handles the interaction with the controller to ensure the portfolio is
   * added to the system.
   * Upon successful creation, a success message is displayed; otherwise, an error
   * message is shown.
   *
   * @param controller The controller instance through which the portfolio creation is mediated.
   */
  @Override
  public void execute(Controller controller) {
    try {
      controller.makePortfolio(portfolioName);
      controller.displayMessage("Portfolio '" + portfolioName + "' created successfully.");
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
