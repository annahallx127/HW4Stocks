package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;

public class DistributionValueCommand implements ControllerCommand {

  private final String portfolioName;

  private final String date;

  public DistributionValueCommand(String portfolioName, String date) {
    this.date = date;
    this.portfolioName = portfolioName;
  }

  @Override
  public void execute(Controller controller) {
    try {
      Portfolio portfolio = controller.getPortfolios().get(portfolioName);
      String distribution = portfolio.getValueDistribution(date);
      controller.displayMessage("Portfolio Distribution for "
              + portfolioName + " on " + date + " is: \n" + distribution);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }

}
