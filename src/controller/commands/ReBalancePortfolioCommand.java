package controller.commands;

import java.util.Map;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;
import model.Stock;

/**
 * A command to rebalance a portfolio according to specified target weights of its
 * constituent stocks on a given date. This command is crucial for maintaining a
 * desired asset allocation over time, which is essential for managing
 * investment risk and achieving financial goals.
 * The command includes the date for rebalancing, the name of the portfolio to be rebalanced,
 * and a map of the target
 * weights for each stock in the portfolio.
 */
public class ReBalancePortfolioCommand implements ControllerCommand {

  private final String date;
  private final String portfolioName;
  private final Map<Stock, Integer> targetWeights;

  /**
   * Constructs a new ReBalancePortfolioCommand with the specified date,
   * portfolio name, and target weights.
   *
   * @param date          The date on which the portfolio should be rebalanced.
   * @param portfolioName The name of the portfolio to rebalance.
   * @param targetWeights A map of stocks and their corresponding target weights,
   *                      where each weight is an integer percentage of the total portfolio.
   */
  public ReBalancePortfolioCommand(String date, String portfolioName,
                                   Map<Stock, Integer> targetWeights) {
    this.date = date;
    this.portfolioName = portfolioName;
    this.targetWeights = targetWeights;
  }

  /**
   * Executes the rebalancing operation for the specified portfolio.
   * This method retrieves the portfolio from the controller,
   * applies the new weights by adjusting the proportions of each stock, and
   * confirms the successful rebalance through the controller's view.
   * If there are issues with the input data or the rebalancing process, an error
   * message is displayed.
   *
   * @param controller The controller through which the portfolio is accessed and rebalanced.
   */
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
