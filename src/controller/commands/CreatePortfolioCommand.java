package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;

public class CreatePortfolioCommand implements ControllerCommand {
  private final String portfolioName;

  public CreatePortfolioCommand(String portfolioName) {
    this.portfolioName = portfolioName;
  }

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
