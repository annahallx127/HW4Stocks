package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.PlotInterval;
import model.Portfolio;

public class PlotPortfolioCommand implements ControllerCommand {
  private final String portfolioName;
  private final String dateStart;
  private final String dateEnd;
  private final PlotInterval interval;

  public PlotPortfolioCommand(String portfolioName, String dateStart,
                              String dateEnd, PlotInterval interval) {
    this.portfolioName = portfolioName;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
    this.interval = interval;
  }

  @Override
  public void execute(Controller controller) {
    try {
      Portfolio portfolio = controller.getPortfolios().get(portfolioName);
      String plot = portfolio.plot(dateStart, dateEnd, interval);
      controller.displayMessage(plot);
    } catch (IllegalArgumentException e) {
      controller.displayError(e.getMessage());
    }
  }
}
