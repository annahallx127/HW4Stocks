package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Stock;

public class CrossoverCommand implements ControllerCommand {
  private final String ticker;
  private final int days;
  private final String startDate;
  private final String endDate;

  public CrossoverCommand(String ticker, int days, String startDate, String endDate) {
    this.ticker = ticker;
    this.days = days;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  @Override
  public void execute(Controller controller) {
    try {
      Stock stock = controller.getStock(ticker);
      String crossovers = stock.getCrossovers(startDate, endDate, days);
      controller.displayMessage("Crossovers for " + ticker + " from "
              + startDate + " to " + endDate + " are:\n" + crossovers);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
