package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;

public class PortfolioValueCommand implements ControllerCommand {

  private final String portfolioName;

  private final String date;

  public PortfolioValueCommand(String portfolioName, String date) {

    this.portfolioName = portfolioName;
    this.date = date;
  }


  @Override
  public void execute(Controller controller) {
    try {
      Portfolio portfolio = controller.getPortfolios().get(portfolioName);
      String value = String.valueOf(portfolio.valueOfPortfolio(date));
      controller.displayMessage("Portfolio Value for " + portfolioName + " on "
              + date + " is: \n" + value);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
