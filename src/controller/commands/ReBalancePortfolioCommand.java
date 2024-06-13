package controller.commands;

import java.util.Map;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;
import model.Stock;

public class ReBalancePortfolioCommand implements ControllerCommand {

  private final String date;
  private final String portfolioName;
  private final Map<Stock, Integer> targetWeights;

  public ReBalancePortfolioCommand(String date, String portfolioName,
                                   Map<Stock, Integer> targetWeights) {
    this.date = date;
    this.portfolioName = portfolioName;
    this.targetWeights = targetWeights;
  }

  @Override
  public void execute(Controller controller) {
    try {
      Portfolio portfolio = controller.getPortfolios().get(portfolioName);
      portfolio.reBalancePortfolio(date, targetWeights);
      controller.displayMessage("Portfolio " + portfolioName
              + " has been successfully rebalanced on " + date);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
