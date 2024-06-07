package view;

public interface View {

  /**
   * Initiates the view and presents the user with options.
   * Loads the next menu based off of user inputs.
   * Handles the main interaction loop with the user.
   */
  void run();

  /**
   * Presents the user with the options to calculate the gain or loss of a stock over a
   * specified period. If the date(s) entered is invalid, it will prompt the user back to the menu
   * to restart. This method uses the model to retrieve stock data and compute
   * the financial result, calculating it using the getChange() method and user inputs.
   * Once the gain or loss is calculated, it will take the user back to the original menu screen.
   */
  void calculateGainOrLoss();

  /**
   * Presents the user with the options to calculate the moving average of a stock
   * over a specified number of days. If the date(s) entered is invalid, it will prompt the user
   * back to the menu to restart.
   * This uses the method getMovingAverage() to calculate the result based on user inputs.
   * Once the average is calculated, it will display the result and take the user back to the
   * original menu screen.
   */
  void calculateXDayMovingAverage();

  /**
   * Presents the user with the options to calculate the crossovers of a specified stock for a
   * specified period over a given number of days. Uses the getCrossovers() method and
   * user inputs to calculate result. This method can help the user identify potential buy or
   * sell signals based on crossover events. If the date(s) entered is invalid, it will prompt the
   * user back to the menu to restart.
   */
  void calculateXDayCrossovers();

  /**
   * Presents the user with the options to create a new stock portfolio. Based off the user inputs
   * it will then create the portfolio with the specified name and stocks.
   * After the portfolio is created, it will prompt the user back to the menu screen, the
   * created portfolio will appear on the menu screen for when they want to review their
   * portfolios.
   */
  void createPortfolio();

  /**
   * Displays the existing portfolios and their details. It will then allow the user to interact
   * with each of their portfolios, whether to add, remove, or find the value of their portfolio
   * on a specific day based off user inputs. If the date(s) entered is invalid, it will prompt
   * the user back to the menu to restart.
   * If there are no existing portfolios, it will then prompt the user to create one and take
   * them back to the menu screen.
   */
  void viewPortfolio();
}