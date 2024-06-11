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
   * Private function that prints a message to the output appendable.
   * Appends the message followed by a new line separator
   * If an IOException occurs, it wraps the exception in a RuntimeException and rethrows it.
   *
   * @param message message the message to be printed
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
      print("Isaac and Anna's Stock Investment Company");
      print("Choose an Option From the Menu:");
      print("1. Calculate Gain or Loss of a Stock");
      print("2. Calculate X-Day Moving Average");
      print("3. Calculate X-Day Crossovers");
      print("4. Create a New Portfolio");
      print("5. Portfolio Menu Screen");
      print("6. Load Own Portfolio");
      print("7. Exit Menu");
      print("Choose an option: ");

      int choice;
      try {
        choice = controller.nextInt();
      } catch (InputMismatchException e) {
        print("Invalid value. Please try again.");
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
          print("Exiting...");
          System.exit(0);
          break;
        default:
          print("Invalid Operation!");
      }
    }
  }

  @Override
  public void calculateGainOrLoss() {
    print("Enter ticker symbol: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
      return;
    }

    String startDate;
    do {
      print("DISCLAIMER: If you have entered any non market date, the nearest market " +
              "\ndate backwards will be considered");
      print("Enter start date (YYYY-MM-DD): ");
      startDate = controller.nextLine();
      if (!stock.isValidDate(startDate)) {
        try {
          stock.isValidDate(startDate);
        } catch (IllegalArgumentException e) {
          print(e.getMessage());
        }
        print("Invalid start date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(startDate));

    String endDate;
    do {
      print("Enter end date (YYYY-MM-DD): ");
      endDate = controller.nextLine();
      if (!stock.isValidDate(endDate)) {
        try {
          stock.isValidDate(endDate);
        } catch (IllegalArgumentException e) {
          print(e.getMessage());
        }
        print("Invalid end date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(endDate));

    double gainOrLoss;
    try {
      gainOrLoss = stock.gainedValue(startDate, endDate);
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
      return;
    }
    print("The gain or loss of " + ticker.toUpperCase() + " is: " + gainOrLoss);
  }

  @Override
  public void calculateXDayMovingAverage() {
    print("Enter ticker symbol: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
      return;
    }

    print("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    print("Enter number of days: ");
    int days = controller.nextInt();
    print("DISCLAIMER: If you have entered a non market date, the nearest market " +
            "date backwards will be considered");
    print("Enter date (YYYY-MM-DD): ");
    String date = controller.nextLine();

    if (days <= 0) {
      print("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(date)) {
      try {
        stock.isValidDate(date);
      } catch (IllegalArgumentException e) {
        print(e.getMessage());
      }
      print("Invalid Date.");
    } else {
      print("The " + days + "-Day Moving Average, Starting on: " + date +
              " is " + stock.getMovingAverage(days, date));
    }
  }

  @Override
  public void calculateXDayCrossovers() {
    print("Enter ticker symbol: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
      return;
    }

    print("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    print("Enter number of days: ");
    int days;

    try {
      days = controller.nextInt();
    } catch (InputMismatchException e) {
      print("Invalid number. Please try again.");
      return;
    }

    try {
      Integer.parseInt(String.valueOf(days));
    } catch (NumberFormatException e) {
      print("Invalid number. Please try again.");
    }

    String startDate;
    do {
      print("DISCLAIMER: If you have entered any non market date, the nearest market " +
              "\ndate backwards will be considered");
      print("Enter start date (YYYY-MM-DD): ");
      startDate = controller.nextLine();
      if (!stock.isValidDate(startDate)) {
        try {
          stock.isValidDate(startDate);
        } catch (IllegalArgumentException e) {
          print(e.getMessage());
        }
        print("Invalid start date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(startDate));

    String endDate;
    do {
      print("Enter end date (YYYY-MM-DD): ");
      endDate = controller.nextLine();
      if (!stock.isValidDate(endDate)) {
        try {
          stock.isValidDate(endDate);
        } catch (IllegalArgumentException e) {
          print(e.getMessage());
        }
        print("Invalid end date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(endDate));

    if (days <= 0) {
      print("Quantity must be greater than 0 and a whole number");
    } else {
      print("The " + days + "-Day Crossover for Date Range: " + startDate +
              " - " + endDate + " is: \n" + stock.getCrossovers(startDate, endDate, days));
    }
  }

  private void addAndBuyStock(Portfolio portfolio) {
    while (true) {
      print("Enter ticker symbol: ");
      String ticker = controller.nextLine().toUpperCase();
      Stock stock;
      try {
        stock = controller.getStock(ticker);
        stock.isValidSymbol(ticker);
      } catch (IllegalArgumentException e) {
        print(e.getMessage());
        return;
      }

      String date;
      do {
        print("DISCLAIMER: If you have entered a non market date, the nearest market " +
                "\ndate backwards will be considered");
        print("Enter date you would like to purchase on: ");
        date = controller.nextLine();
        if (!stock.isValidDate(date)) {
          print("Invalid date. Please enter a valid market date.");
        }
      } while (!stock.isValidDate(date));

      print("You are Buying this Stock at Date: " + date);

      print("Enter quantity: ");
      int quantity;
      try {
        quantity = controller.nextInt();
      } catch (InputMismatchException e) {
        print("Invalid quantity. Please try again.");
        return;
      }

      if (quantity > 0) {
        portfolio.add(stock, quantity, date);
        print("You have added " + quantity + " shares of " + ticker.toUpperCase()
                + " to your portfolio!");
      } else {
        print("Quantity must be greater than 0 and a whole number, please try again.");
        return;
      }

      print("Do you want to add another stock? (yes/no): ");
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
      print("No portfolios available. Please create one first.");
      return;
    }

    print("Available Portfolios:");
    int i = 1;
    for (String name : portfolios.keySet()) {
      print(i + ". " + name);
      i++;
    }
    print(i + ". Go Back");

    print("Choose a portfolio to view: ");
    int option;
    try {
      option = controller.nextInt();
    } catch (InputMismatchException e) {
      print("Invalid option. Please try again.");
      return;
    }

    if (option > 0 && option <= portfolios.size()) {
      String selectedPortfolioName = (String) portfolios.keySet().toArray()[option - 1];
      Portfolio portfolio = portfolios.get(selectedPortfolioName);
      print(portfolio.toString());
      viewPortfolioChooseMenuScreen(portfolio);
    } else if (option == portfolios.size() + 1) {
      return;
    } else {
      print("Invalid choice. Please try again.");
    }
  }

  private void sellStocks(Portfolio portfolio) {

    String intendedSellDate;

    do {
      print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      print("Enter date you would like to sell on: ");
      intendedSellDate = controller.nextLine();
      try {
        intendedSellDate = controller.nextLine();
        portfolio.isValidDateForPortfolio(intendedSellDate);
      } catch (IllegalArgumentException e) {
        print(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(intendedSellDate)) {
        print("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(intendedSellDate));

    print("Enter ticker symbol to remove: ");
    String ticker = controller.nextLine().toUpperCase();
    Stock stockToRemove;
    try {
      stockToRemove = controller.getStock(ticker);
      stockToRemove.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
      return;
    }
    print("Enter quantity: ");
    double numOfShares = controller.nextInt();
    try {
      portfolio.remove(stockToRemove, numOfShares, intendedSellDate);
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
      return;
    }

    print("You are Selling this Stock at Date: " + intendedSellDate);

    print("You have removed: " + String.format("%.2f", numOfShares) + " shares of "
            + stockToRemove
            + " from the portfolio " + portfolio.getName() + "on" + intendedSellDate + "!\n");
    controller.savePortfolio(portfolio.getName(), intendedSellDate);
  }


  private void totalValueOfPortfolioAtDate(Portfolio portfolio) {
    print("DISCLAIMER: If you have entered a non market date, the nearest market " +
            "\ndate backwards will be considered");
    print("Enter date (YYYY-MM-DD): ");
    String date = controller.nextLine();
    double value;
    try {
      value = portfolio.valueOfPortfolio(date);
    } catch (IllegalArgumentException e) {
      print(e.getMessage());
      return;
    }
    print("The value of the portfolio on " + date + " is: " + String.format("%.2f", value));
  }

  private void viewPortfolioChooseMenuScreen(Portfolio portfolio) {
    while (true) {
      print("You are in the Portfolio: " + portfolio.getName());
      print("1. Buy shares of a Stock");
      print("2. Sell shares of a Stock");
      print("3. View Entire Portfolio (To Date)");
      print("4. View Composition of Portfolio");
      print("5. Re-Balance Portfolio");
      print("6. Find Portfolio Value");
      print("7. Find Distribution of Value");
      print("8. View Performance Over Time");
      print("9. Save Portfolio to Device");
      print("10. Go Back");

      int option;
      try {
        option = controller.nextInt();
      } catch (InputMismatchException e) {
        print("Invalid option. Please try again.");
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
          print(portfolio.toString());
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
          print("Invalid choice. Please try again.");
      }
    }
  }

  @Override
  public void createPortfolio() {
    print("Enter a name for your portfolio: ");
    String name = controller.nextLine();
    controller.makePortfolio(name);
    Portfolio portfolio = controller.getPortfolios().get(name);
    print("Portfolio '" + name + "' created.");
    // save happens in addAndBuyStock
    addAndBuyStock(portfolio);
  }

  // need a way to save the dates so the methods can look back at them
  private void viewCompositionOfPortfolioAtAnyDate(Portfolio portfolio) {

  }

  private void findDistributionValueAtDate(Portfolio portfolio) {
    String intendedDate;

    do {
      print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      print("Enter date you would like to purchase on: ");
      intendedDate = controller.nextLine();
      try {
        intendedDate = controller.nextLine();
        portfolio.isValidDateForPortfolio(intendedDate);
      } catch (IllegalArgumentException e) {
        print(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(intendedDate)) {
        print("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(intendedDate));

    print("Here is the value distribution of your portfolio at date: " + intendedDate);

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
    print("Enter the name of the portfolio to load: ");
    String portfolioName = controller.nextLine();
    // TODO: add support for path to load portfolio - can be hardcoded for now
    try {
      controller.loadPortfolio(portfolioName, "src/portfolios"); // put the load portfolio into controller
      Portfolio loadedPortfolio = controller.getPortfolios().get(portfolioName);
      if (loadedPortfolio != null) {
        print("Portfolio '" + portfolioName + "' loaded successfully.");
        viewPortfolioChooseMenuScreen(loadedPortfolio);
      } else {
        print("Portfolio '" + portfolioName + "' does not exist.");
      }
    } catch (Exception e) {
      print("Error loading portfolio: " + e.getMessage());
    }
  }
  // call the portfolio menu screen here and then save portfolio
}
