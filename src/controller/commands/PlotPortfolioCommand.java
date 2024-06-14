package controller.commands;

import controller.Controller;
import controller.ControllerCommand;
import model.PlotInterval;
import model.Portfolio;

/**
 * A command to plot the value of a portfolio over a specified date range.
 * The plot includes a time scale and a measure of the portfolio's value at each time point.
 */
public class PlotPortfolioCommand implements ControllerCommand {
  private final String portfolioName;
  private final String dateStart;
  private final String dateEnd;
  private final PlotInterval interval;

  /**
   * Constructs a new PlotPortfolioCommand with the specified portfolio name, start date, end date,
   * and interval.
   *
   * @param portfolioName The name of the portfolio to plot.
   * @param dateStart     The start date for the plot.
   * @param dateEnd       The end date for the plot.
   * @param interval      The interval for the plot.
   */
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
