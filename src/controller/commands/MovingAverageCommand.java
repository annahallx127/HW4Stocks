package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.Stock;

public class MovingAverageCommand implements ControllerCommand {
  private final String ticker;
  private final int days;
  private final String date;

  public MovingAverageCommand(String ticker, int days, String date) {
    this.ticker = ticker;
    this.days = days;
    this.date = date;
  }

  @Override
  public void execute(Controller controller) {
    try {
      Stock stock = controller.getStock(ticker);
      double movingAverage = stock.getMovingAverage(days, date);
      controller.displayMessage(days + "-Day Moving Average for " + ticker
              + " on " + date + " is: $" + movingAverage);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
