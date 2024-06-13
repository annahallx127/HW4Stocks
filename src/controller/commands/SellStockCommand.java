package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Portfolio;
import model.Stock;

public class SellStockCommand implements ControllerCommand {
  private final String portfolioName;
  private final String ticker;
  private final double numOfShares;
  private final String sellDate;

  public SellStockCommand(String portfolioName, Stock ticker,
                          double numOfShares, String sellDate) {
    this.portfolioName = portfolioName;
    this.ticker = String.valueOf(ticker);
    this.numOfShares = numOfShares;
    this.sellDate = sellDate;
  }

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
