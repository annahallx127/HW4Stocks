package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;

public class ViewCompositionCommand implements ControllerCommand {
  private final String date;
  private final String portfolioName;

  public ViewCompositionCommand(String portfolio, String date) {
    this.date = date;
    this.portfolioName = portfolio;

  }

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
