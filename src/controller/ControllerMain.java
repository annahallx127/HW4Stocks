package controller;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import controller.commands.*;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;
import view.View;
import view.ViewImpl;

/**
 * The entry point of the stock investment application.
 * This class initializes the necessary components and starts the application.
 */
public class ControllerMain {
  /**
   * The main method that serves as the entry point of the application.
   * Initializes the input and output streams, creates a controller, and starts the application.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Appendable out = System.out;
    View view = new ViewImpl(out);
    ModelImpl model = new ModelImpl();
    Controller controller = new ControllerImpl(view, scanner, out);

    while (true) {
      view.print("Isaac and Anna's Stock Investment Company");
      view.print("Choose an Option From the Menu:");
      view.print("1. Calculate Gain or Loss of a Stock");
      view.print("2. Calculate X-Day Moving Average");
      view.print("3. Calculate X-Day Crossovers");
      view.print("4. Create a New Portfolio");
      view.print("5. Portfolio Menu Screen");
      view.print("6. Load Own Portfolio");
      view.print("7. Exit Menu");
      view.print("Choose an option:");

      int choice;
      try {
        choice = scanner.nextInt();
      } catch (InputMismatchException e) {
        view.print("Invalid value. Please try again.");
        continue;
      }

      try {
        switch (choice) {
          case 1:
            handleCalculateGainOrLoss(controller, scanner);
            break;
          case 2:
            handleCalculateXDayMovingAverage(controller, scanner);
            break;
          case 3:
            r
            handleCalculateXDayCrossovers(controller, scanner);
            break;
          case 4:
            handleCreatePortfolio(controller, scanner);
            break;
          case 5:
            handleViewPortfolio(controller, scanner);
            break;
          case 6:
            handleLoadPortfolio(controller, scanner);
            break;
          case 7:
            view.print("Exiting...");
            System.exit(0);
            break;
          default:
            view.print("Invalid Operation!");
        }
      } catch (Exception e) {
        view.print("Error: " + e.getMessage());
        scanner.nextLine();  // Consume any other stray input
      }
    }
  }

  private static void handleCalculateGainOrLoss(Controller controller, Scanner scanner) {
    System.out.println("Enter ticker symbol: ");
    String ticker = scanner.nextLine().toUpperCase();
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
      startDate = scanner.nextLine();
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
      endDate = scanner.nextLine();
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

    ControllerCommand command = new CalculateGainLossCommand(ticker, startDate, endDate);
    controller.executeCommand(command);
  }


  private static void handleCalculateXDayMovingAverage(Controller controller, Scanner scanner) {
    System.out.println("Enter ticker symbol: ");
    String ticker = scanner.nextLine().toUpperCase();
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
    int days = scanner.nextInt();
    System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
            "date backwards will be considered");
    System.out.println("Enter date (YYYY-MM-DD): ");
    String date = scanner.nextLine();

    if (days <= 0) {
      System.out.println("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(date)) {
      try {
        stock.isValidDate(date);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      System.out.println("Invalid Date.");
    }

    ControllerCommand command = new MovingAverageCommand(ticker, days, date);
    controller.executeCommand(command);
  }

  private static void handleCalculateXDayCrossovers(Controller controller, Scanner scanner) {
    System.out.println("Enter ticker symbol: ");
    String ticker = scanner.nextLine().toUpperCase();
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
      days = scanner.nextInt();
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
      startDate = scanner.nextLine();
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
      endDate = scanner.nextLine();
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
    }

    ControllerCommand command = new CrossoverCommand(ticker, days, startDate, endDate);
    controller.executeCommand(command);
  }

  private static void addAndBuyStock(Portfolio portfolio, Controller controller, Scanner scanner) {
    while (true) {
      System.out.println("Enter ticker symbol: ");
      String ticker = scanner.nextLine().toUpperCase();
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
        System.out.println("Enter date you would like to purchase on (YYYY-MM-DD): ");
        date = scanner.nextLine();
        if (!stock.isValidDate(date)) {
          System.out.println("Invalid date. Please enter a valid market date.");
        }
      } while (!stock.isValidDate(date));

      System.out.println("You are Buying this Stock at Date: " + date);

      System.out.println("Enter quantity: ");
      int quantity;
      try {
        quantity = scanner.nextInt();
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
      String response = scanner.nextLine().trim().toLowerCase();
      if (!response.equals("yes")) {
        break;
      }
    }
  }

  private static void handleCreatePortfolio(Controller controller, Scanner scanner) {
    System.out.println("Enter a name for your portfolio: ");
    String name = scanner.nextLine();

    Portfolio portfolio = controller.getPortfolios().get(name);

    ControllerCommand command = new CreatePortfolioCommand(name);
    controller.executeCommand(command);

    addAndBuyStock(portfolio, controller, scanner);
  }

  private static void viewPortfolioChooseMenuScreen(Portfolio portfolio, Controller controller, Scanner scanner) {
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
        option = scanner.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Invalid option. Please try again.");
        return;
      }

      switch (option) {
        case 1:
          addAndBuyStock(portfolio, controller, scanner);
          break;
        case 2:
          sellStocks(portfolio, controller, scanner);
          break;
        case 3:
          System.out.println(portfolio.toString());
          break;
        case 4:
          viewCompositionOfPortfolioAtAnyDate(portfolio, controller, scanner);
          break;
        case 5:
          reBalanceChosenPortfolio(portfolio, controller, scanner);
          break;
        case 6:
          totalValueOfPortfolioAtDate(portfolio, controller, scanner);
          break;
        case 7:
          findDistributionValueAtDate(portfolio, controller, scanner);
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

  private static void handleViewPortfolio(Controller controller, Scanner scanner) {
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
      option = scanner.nextInt();
    } catch (InputMismatchException e) {
      System.out.println("Invalid option. Please try again.");
      return;
    }

    if (option > 0 && option <= portfolios.size()) {
      String selectedPortfolioName = (String) portfolios.keySet().toArray()[option - 1];
      Portfolio portfolio = portfolios.get(selectedPortfolioName);
      System.out.println(portfolio.toString());
      viewPortfolioChooseMenuScreen(portfolio, controller, scanner);
    } else if (option == portfolios.size() + 1) {
      return;
    } else {
      System.out.println("Invalid choice. Please try again.");
    }
  }


  private static void sellStocks(Portfolio portfolio, Controller controller, Scanner scanner) {

    String intendedSellDate;

    do {
      System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter date you would like to sell on (YYYY-MM-DD): ");
      intendedSellDate = scanner.nextLine();
      try {
        intendedSellDate = scanner.nextLine();
        portfolio.isValidDateForPortfolio(intendedSellDate);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(intendedSellDate)) {
        System.out.println("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(intendedSellDate));

    System.out.println("Enter ticker symbol to remove: ");
    String ticker = scanner.nextLine().toUpperCase();
    Stock stockToRemove;
    try {
      stockToRemove = controller.getStock(ticker);
      stockToRemove.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("Enter quantity: ");
    double numOfShares = scanner.nextInt();


    System.out.println("You are Selling this Stock at Date: " + intendedSellDate);
    ControllerCommand command = new SellStockCommand(portfolio.getName(), stockToRemove, numOfShares, intendedSellDate);
    controller.executeCommand(command);
  }

  private static void viewCompositionOfPortfolioAtAnyDate(Portfolio portfolio, Controller controller, Scanner scanner) {

    String date;

    do {
      System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter date you would like to sell on (YYYY-MM-DD): ");
      date = scanner.nextLine();
      try {
        date = scanner.nextLine();
        portfolio.isValidDateForPortfolio(date);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(date)) {
        System.out.println("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(date));

    ControllerCommand command = new ViewCompositionCommand(portfolio.getName(), date);
    controller.executeCommand(command);
  }

  private static void reBalanceChosenPortfolio(Portfolio portfolio, Controller controller, Scanner scanner) {
    String date;

    do {
      System.out.println("DISCLAIMER: This program does not support re-balancing on a weekend! Please choose a valid market Date");
      System.out.println("Enter date you would like to sell on (YYYY-MM-DD): ");
      date = scanner.nextLine();
      try {
        portfolio.isValidDateForPortfolio(date);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(date)) {
        System.out.println("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(date));

    Map<Stock, Integer> targetWeights = new HashMap<>();
    System.out.println("Enter the weights for each stock in your portfolio. Weights must total to 100%.");

    int totalWeight = 0;
    for (Stock stock : portfolio.getStocks().keySet()) {
      System.out.println("Enter weight for " + portfolio.getStocks().keySet());
      int weight = scanner.nextInt();
      while (weight < 0 || weight > 100) {
        System.out.println("Invalid weight. Please enter a value between 0 and 100.");
        weight = scanner.nextInt();
      }
      targetWeights.put(stock, weight);
      totalWeight += weight;
    }

    if (totalWeight != 100) {
      System.out.println("The total weight must be exactly 100%. You entered a total of " + totalWeight + "%.");
      return;
    }

    ControllerCommand command = new ReBalancePortfolioCommand(date, portfolio.getName(), targetWeights);
    controller.executeCommand(command);
  }

  private static void totalValueOfPortfolioAtDate(Portfolio portfolio, Controller controller, Scanner scanner) {
    String date;

    do {
      System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter date you would like to sell on (YYYY-MM-DD): ");
      date = scanner.nextLine();
      try {
        date = scanner.nextLine();
        portfolio.isValidDateForPortfolio(date);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(date)) {
        System.out.println("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(date));

    ControllerCommand command = new PortfolioValueCommand(portfolio.getName(), date);
    controller.executeCommand(command);
  }


  private static void findDistributionValueAtDate(Portfolio portfolio, Controller controller, Scanner scanner) {
    String date;

    do {
      System.out.println("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      System.out.println("Enter date you would like to sell on (YYYY-MM-DD): ");
      date = scanner.nextLine();
      try {
        date = scanner.nextLine();
        portfolio.isValidDateForPortfolio(date);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(date)) {
        System.out.println("Invalid date. Please enter a valid market date.");
      }
    } while (!portfolio.isValidDateForPortfolio(date));

    ControllerCommand command = new DistributionValueCommand(portfolio.getName(), date);
    controller.executeCommand(command);
  }


}
