package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;
import model.Stock;

/**
 * A command to sell a specified number of shares of a stock from a portfolio on a given date.
 * This command facilitates the execution of a sell order within the context of a stock
 * investment application,
 * adjusting the portfolio's holdings accordingly and updating its state.
 *
 * <p>
 * The command includes the portfolio name from which the stock is to be sold,
 * the ticker symbol of the stock,
 * the number of shares to be sold, and the date on which the sale should occur.
 * </p>
 */
public class SellStockCommand implements ControllerCommand {
  private final String portfolioName;
  private final String ticker;
  private final double numOfShares;
  private final String sellDate;

  /**
   * Constructs a new SellStockCommand with the specified parameters.
   *
   * @param portfolioName The name of the portfolio from which the shares are to be sold.
   * @param ticker        The stock object representing the stock to be sold.
   * @param numOfShares   The number of shares to sell.
   * @param sellDate      The date on which the shares are to be sold.
   */
  public SellStockCommand(String portfolioName, Stock ticker,
                          double numOfShares, String sellDate) {
    this.portfolioName = portfolioName;
    this.ticker = String.valueOf(ticker);
    this.numOfShares = numOfShares;
    this.sellDate = sellDate;
  }

  /**
   * Executes the sale of shares from the specified portfolio. This method retrieves the portfolio
   * and the stock from the controller, executes the sell order, and updates the portfolio state.
   * If successful, a confirmation message is displayed; if not, an error message is shown.
   *
   * @param controller The controller that interfaces with the model and view to
   *                   perform the operation.
   */
  @Override
  public void execute(Controller controller) {
    try {
      Portfolio portfolio = controller.getPortfolios().get(portfolioName);
      Stock stockToRemove = controller.getStock(ticker);
      portfolio.remove(stockToRemove, numOfShares, sellDate);
      controller.displayMessage("Successfully sold " + String.format("%.2f", numOfShares)
              + " shares of " + ticker.toUpperCase()
              + " from portfolio " + portfolioName
              + " on " + sellDate);
      controller.savePortfolio(portfolioName, sellDate);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
