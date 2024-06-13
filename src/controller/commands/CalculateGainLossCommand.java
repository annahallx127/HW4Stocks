package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import controller.ControllerImpl;

import model.Stock;

public class CalculateGainLossCommand implements ControllerCommand {
  private final String ticker;
  private final String startDate;
  private final String endDate;

  public CalculateGainLossCommand(String ticker, String startDate, String endDate) {
    this.ticker = ticker;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  @Override
  public void execute(Controller controller) {
    try {
      Stock stock = controller.getStock(ticker);
      double gainOrLoss = stock.gainedValue(startDate, endDate);
      controller.displayMessage("Gain/Loss for " + ticker.toUpperCase()
              + " from " + startDate + " to " + endDate + " is: $" + gainOrLoss);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
