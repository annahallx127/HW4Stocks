package view;

import controller.Controller;
import model.Portfolio;
import model.Stock;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * The ViewImpl class implements the View interface and provides a concrete implementation
 * for interacting with the user in the stock investment application. It handles and responds to
 * user inputs nd displays the options and results. View also handle what the user is able
 * to see on their end. This class interacts with the Controller to
 * perform the operations the user has chosen such as; calculating the gain or loss of a stock,
 * moving averages, crossovers, creating portfolios, and viewing existing portfolios.
 * Through the text-based user interface, the user is able to interact with the application.
 */
public class ViewImpl implements View {
  private final Appendable out;
  private final Controller controller;

  /**
   * Constructs a ViewImpl instance with the specified controller.
   * Initializes the output appendable and starts the main interaction loop with the run() method.
   *
   * @param controller the controller used to interact with the model and handle user inputs
   */
  public ViewImpl(Controller controller) {
    this.controller = controller;
    this.out = controller.getAppendable();
    run();
  }

  /**
   * Private function that System.out.printlns a message to the output appendable.
   * Appends the message followed by a new line separator
   * If an IOException occurs, it wraps the exception in a RuntimeException and rethrows it.
   *
   * @param message message the message to be System.out.printlned
   */
  private void print(String message) {
    try {
      out.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to append output", e);
    }
  }

  @Override
  public void run() {
    while (true) {
      System.out.println("Isaac and Anna's Stock Investment Company");
      System.out.println("Choose an Option From the Menu:");
      System.out.println("1. Calculate Gain or Loss of a Stock");
      System.out.println("2. Calculate X-Day Moving Average");
      System.out.println("3. Calculate X-Day Crossovers");
      System.out.println("4. Create a New Portfolio");
      System.out.println("5. Portfolio Menu Screen");
      System.out.println("6. Load Own Portfolio");
      System.out.println("7. Exit Menu");
      System.out.println("Choose an option: ");

      int choice;
      try {
        choice = controller.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Invalid value. Please try again.");
        continue;
      }

      switch (choice) {
        case 1:
          calculateGainOrLoss();
          break;
        case 2:
          calculateXDayMovingAverage();
          break;
        case 3:
          calculateXDayCrossovers();
          break;
        case 4:
          createPortfolio();
          break;
        case 5:
          viewPortfolio();
          break;
        case 6:
//           loadChosenPortfolio();
          break;
        case 7:
          System.out.println("Exiting...");
          System.exit(0);
          break;
        default:
          System.out.println("Invalid Operation!");
      }
    }
  }

  @Override
  public void calculateGainOrLoss() {
    System.out.println("Enter ticker symbol: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }

    String startDate;
    do {
      System.out.println("DISCLAIMER: If you have entered any non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter start date (YYYY-MM-DD): ");
      startDate = controller.nextLine();
      if (!stock.isValidDate(startDate)) {
        try {
          stock.isValidDate(startDate);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }
        System.out.println("Invalid start date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(startDate));

    String endDate;
    do {
      System.out.println("Enter end date (YYYY-MM-DD): ");
      endDate = controller.nextLine();
      if (!stock.isValidDate(endDate)) {
        try {
          stock.isValidDate(endDate);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }
        System.out.println("Invalid end date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(endDate));

    double gainOrLoss;
    try {
      gainOrLoss = stock.gainedValue(startDate, endDate);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("The gain or loss of " + ticker.toUpperCase() + " is: " + gainOrLoss);
  }

  @Override
  public void calculateXDayMovingAverage() {
    System.out.println("Enter ticker symbol: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }

    System.out.println("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    System.out.println("Enter number of days: ");
    int days = controller.nextInt();
    System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
            "date backwards will be considered");
    System.out.println("Enter date (YYYY-MM-DD): ");
    String date = controller.nextLine();

    if (days <= 0) {
      System.out.println("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(date)) {
      try {
        stock.isValidDate(date);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      System.out.println("Invalid Date.");
    } else {
      System.out.println("The " + days + "-Day Moving Average, Starting on: " + date +
              " is " + stock.getMovingAverage(days, date));
    }
  }

  @Override
  public void calculateXDayCrossovers() {
    System.out.println("Enter ticker symbol: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }

    System.out.println("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    System.out.println("Enter number of days: ");
    int days;

    try {
      days = controller.nextInt();
    } catch (InputMismatchException e) {
      System.out.println("Invalid number. Please try again.");
      return;
    }

    try {
      Integer.parseInt(String.valueOf(days));
    } catch (NumberFormatException e) {
      System.out.println("Invalid number. Please try again.");
    }

    String startDate;
    do {
      System.out.println("DISCLAIMER: If you have entered any non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter start date (YYYY-MM-DD): ");
      startDate = controller.nextLine();
      if (!stock.isValidDate(startDate)) {
        try {
          stock.isValidDate(startDate);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }
        System.out.println("Invalid start date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(startDate));

    String endDate;
    do {
      System.out.println("Enter end date (YYYY-MM-DD): ");
      endDate = controller.nextLine();
      if (!stock.isValidDate(endDate)) {
        try {
          stock.isValidDate(endDate);
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }
        System.out.println("Invalid end date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(endDate));

    if (days <= 0) {
      System.out.println("Quantity must be greater than 0 and a whole number");
    } else {
      System.out.println("The " + days + "-Day Crossover for Date Range: " + startDate +
              " - " + endDate + " is: \n" + stock.getCrossovers(startDate, endDate, days));
    }
  }

  private void addAndBuyStock(Portfolio portfolio) {
    while (true) {
      System.out.println("Enter ticker symbol: ");
      String ticker = controller.nextLine().toUpperCase();
      Stock stock;
      try {
        stock = controller.getStock(ticker);
        stock.isValidSymbol(ticker);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        return;
      }

      String date;
      do {
        System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
                "\ndate backwards will be considered");
        System.out.println("Enter date you would like to purchase on: ");
        date = controller.nextLine();
        if (!stock.isValidDate(date)) {
          System.out.println("Invalid date. Please enter a valid market date.");
        }
      } while (!stock.isValidDate(date));

      System.out.println("You are Buying this Stock at Date: " + date);

      System.out.println("Enter quantity: ");
      int quantity;
      try {
        quantity = controller.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Invalid quantity. Please try again.");
        return;
      }

      if (quantity > 0) {
        portfolio.add(stock, quantity, date);
        System.out.println("You have added " + quantity + " shares of " + ticker.toUpperCase()
                + " to your portfolio!");
      } else {
        System.out.println("Quantity must be greater than 0 and a whole number, please try again.");
        return;
      }

      System.out.println("Do you want to add another stock? (yes/no): ");
      String response = controller.nextLine().trim().toLowerCase();
      if (!response.equals("yes")) {
        break;
      }
    }
  }

  @Override
  public void viewPortfolio() {
    Map<String, Portfolio> portfolios = controller.getPortfolios();
    if (portfolios.isEmpty()) {
      System.out.println("No portfolios available. Please create one first.");
      return;
    }

    System.out.println("Available Portfolios:");
    int i = 1;
    for (String name : portfolios.keySet()) {
      System.out.println(i + ". " + name);
      i++;
    }
    System.out.println(i + ". Go Back");

    System.out.println("Choose a portfolio to view: ");
    int option;
    try {
      option = controller.nextInt();
    } catch (InputMismatchException e) {
      System.out.println("Invalid option. Please try again.");
      return;
    }

    if (option > 0 && option <= portfolios.size()) {
      String selectedPortfolioName = (String) portfolios.keySet().toArray()[option - 1];
      Portfolio portfolio = portfolios.get(selectedPortfolioName);
      System.out.println(portfolio.toString());
      viewPortfolioChooseMenuScreen(portfolio);
    } else if (option == portfolios.size() + 1) {
      return;
    } else {
      System.out.println("Invalid choice. Please try again.");
    }
  }

  private void sellStocks(Portfolio portfolio) {

    String intendedSellDate;

    do {
      System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter date you would like to sell on: ");
      intendedSellDate = controller.nextLine();
      try {
        intendedSellDate = controller.nextLine();
        portfolio.isValidDateForPortfolio(intendedSellDate);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(intendedSellDate)) {
        System.out.println("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(intendedSellDate));

    System.out.println("Enter ticker symbol to remove: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stockToRemove;
    try {
      stockToRemove = controller.getStock(ticker);
      stockToRemove.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("Enter quantity: ");
    double numOfShares = controller.nextInt();
    try {
      portfolio.remove(stockToRemove, numOfShares, intendedSellDate);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }

    System.out.println("You are Selling this Stock at Date: " + intendedSellDate);

    System.out.println("You have removed: " + String.format("%.2f", numOfShares) + " shares of "
            + stockToRemove
            + " from the portfolio " + portfolio.getName() + "on" + intendedSellDate + "!\n");
    controller.savePortfolio(portfolio.getName(), intendedSellDate);
  }


  private void totalValueOfPortfolioAtDate(Portfolio portfolio) {
    System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
            "\ndate backwards will be considered");
    System.out.println("Enter date (YYYY-MM-DD): ");
    String date = controller.nextLine();
    double value;
    try {
      value = portfolio.valueOfPortfolio(date);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("The value of the portfolio on " + date + " is: " + String.format("%.2f", value));
  }

  private void viewPortfolioChooseMenuScreen(Portfolio portfolio) {
    while (true) {
      System.out.println("You are in the Portfolio: " + portfolio.getName());
      System.out.println("1. Buy shares of a Stock");
      System.out.println("2. Sell shares of a Stock");
      System.out.println("3. View Entire Portfolio (To Date)");
      System.out.println("4. View Composition of Portfolio");
      System.out.println("5. Re-Balance Portfolio");
      System.out.println("6. Find Portfolio Value");
      System.out.println("7. Find Distribution of Value");
      System.out.println("8. View Performance Over Time");
      System.out.println("9. Save Portfolio to Device");
      System.out.println("10. Go Back");

      int option;
      try {
        option = controller.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Invalid option. Please try again.");
        return;
      }

      switch (option) {
        case 1:
          addAndBuyStock(portfolio);
          break;
        case 2:
          sellStocks(portfolio);
          break;
        case 3:
          System.out.println(portfolio.toString());
          break;
        case 4:
          viewCompositionOfPortfolioAtAnyDate(portfolio);
          break;
        case 5:
          reBalanceChosenPortfolio(portfolio);
          break;
        case 6:
          totalValueOfPortfolioAtDate(portfolio);
          break;
        case 7:
          findDistributionValueAtDate(portfolio);
          break;
        case 8:
          plotPerformanceBarChart(portfolio);
          break;
        case 9:
//          savePortfolioToDevice(portfolio);
          break;
        case 10:
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  @Override
  public void createPortfolio() {
    System.out.println("Enter a name for your portfolio: ");
    String name = controller.nextLine();
    controller.makePortfolio(name);
    Portfolio portfolio = controller.getPortfolios().get(name);
    System.out.println("Portfolio '" + name + "' created.");
    // save happens in addAndBuyStock
    addAndBuyStock(portfolio);
  }

  // need a way to save the dates so the methods can look back at them
  private void viewCompositionOfPortfolioAtAnyDate(Portfolio portfolio) {

  }

  private void findDistributionValueAtDate(Portfolio portfolio) {
    String intendedDate;

    do {
      System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter date you would like to purchase on: ");
      intendedDate = controller.nextLine();
      try {
        intendedDate = controller.nextLine();
        portfolio.isValidDateForPortfolio(intendedDate);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(intendedDate)) {
        System.out.println("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(intendedDate));

    System.out.println("Here is the value distribution of your portfolio at date: " + intendedDate);

    portfolio.getValueDistribution(intendedDate);

  }

//
//  private void savePortfolioToDevice(Portfolio portfolio) {
//
//  }

  private void reBalanceChosenPortfolio(Portfolio portfolio) {

  }

  private void plotPerformanceBarChart(Portfolio portfolio) {

  }

  // should the user load the portfolio in the menu screen or in the portfolio menu screen?
  // probably the first menu
  private void loadChosenPortfolio() {
    System.out.println("Enter the name of the portfolio to load: ");
    String portfolioName = controller.nextLine();
    // TODO: add support for path to load portfolio - can be hardcoded for now
    try {
      controller.loadPortfolio(portfolioName, "src/portfolios"); // put the load portfolio into controller
      Portfolio loadedPortfolio = controller.getPortfolios().get(portfolioName);
      if (loadedPortfolio != null) {
        System.out.println("Portfolio '" + portfolioName + "' loaded successfully.");
        viewPortfolioChooseMenuScreen(loadedPortfolio);
      } else {
        System.out.println("Portfolio '" + portfolioName + "' does not exist.");
      }
    } catch (Exception e) {
      System.out.println("Error loading portfolio: " + e.getMessage());
    }
  }
  // call the portfolio menu screen here and then save portfolio
}
//package view;

//import java.io.IOException;

//public class ViewImpl {
//  private final Appendable out;
//
//  public ViewImpl(Appendable out) {
//    this.out = out;
//  }
//
//  public void System.out.println(String message) {
//    try {
//      out.append(message).append("\n");
//    } catch (IOException e) {
//      throw new RuntimeException("Failed to append output", e);
//    }
//  }
//}

