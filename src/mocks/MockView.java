// fix later
//
// package mocks;
//
//import model.Model;
//import model.Portfolio;
//import model.Stock;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.Map;
//import java.util.Scanner;
//
//import view.View;
//
///**
// * A mock implementation of the View interface for testing purposes.
// * This class simulates user interactions with the application, providing
// * a text-based user interface for managing portfolios and stocks. It uses
// * a Model instance to interact with the mock data and handles input/output
// * operations to facilitate testing.
// */
//public class MockView implements View {
//
//  private final Model model;
//  private final Appendable out;
//  private final Scanner scanner;
//
//
//  /**
//   * Constructs a new MockView with the specified model, input, and output.
//   * Initializes the model, output appendable, and scanner for reading input.
//   * Starts the main interaction loop.
//   *
//   * @param model the model to interact with
//   * @param in    the input source for user interactions
//   * @param out   the output target for displaying messages
//   */
//  public MockView(Model model, Readable in, Appendable out) {
//    this.model = model;
//    this.out = out;
//    this.scanner = new Scanner(in);
//    run();
//  }
//
//  private void print(String message) {
//    try {
//      out.append(message).append("\n");
//    } catch (IOException e) {
//      throw new RuntimeException("Failed to append output", e);
//    }
//  }
//
//  @Override
//  public void run() {
//    while (true) {
//      print("Isaac and Anna's Stock Investment Company");
//      print("Choose an Option From the Menu:");
//      print("1. Calculate Gain or Loss of a Stock");
//      print("2. Calculate X-Day Moving Average");
//      print("3. Calculate X-Day Crossovers");
//      print("4. Create a New Portfolio");
//      print("5. View Existing Portfolios");
//      print("6. Exit Menu");
//      print("Choose an option: ");
//
//      int choice = scanner.nextInt();
//      scanner.nextLine();
//
//      switch (choice) {
//        case 1:
//          calculateGainOrLoss();
//          break;
//        case 2:
//          calculateXDayMovingAverage();
//          break;
//        case 3:
//          calculateXDayCrossovers();
//          break;
//        case 4:
//          createPortfolio();
//          break;
//        case 5:
//          viewPortfolio();
//          break;
//        case 6:
//          print("Exiting...");
//          System.exit(0);
//          break;
//        default:
//          print("Invalid Operation!");
//      }
//    }
//  }
//
//  @Override
//  public void calculateGainOrLoss() {
//    print("Enter ticker symbol: ");
//    String ticker = scanner.nextLine().toUpperCase();
//    Stock chosenStock = model.get(ticker);
//    if (chosenStock == null) {
//      print("Invalid ticker symbol.");
//      return;
//    }
//
//    print("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
//            + "\nthe nearest business day forward will be considered");
//    print("Start Date (YYYY-MM-DD): ");
//    String startDate = scanner.nextLine();
//    print("End Date (YYYY-MM-DD): ");
//    String endDate = scanner.nextLine();
//
//    double gainOrLoss = chosenStock.gainedValue(startDate, endDate);
//    print("The gain or loss of " + ticker.toUpperCase() + " is: " + gainOrLoss);
//  }
//
//  @Override
//  public void calculateXDayMovingAverage() {
//    print("Enter ticker symbol: ");
//    String ticker = scanner.nextLine().toUpperCase();
//    Stock stock = model.get(ticker);
//    if (stock == null) {
//      print("Invalid ticker symbol.");
//      return;
//    }
//
//    print("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
//            + "\nthe nearest business day forward will be considered");
//    print("Enter number of days: ");
//    int days = scanner.nextInt();
//    scanner.nextLine();
//    print("Enter date (YYYY-MM-DD): ");
//    String date = scanner.nextLine();
//
//    if (days <= 0) {
//      print("Quantity must be greater than 0 and a whole number");
//    } else if (!stock.isValidDate(date)) {
//      try {
//        stock.isValidDate(date);
//      } catch (IllegalArgumentException e) {
//        print(e.getMessage());
//      }
//      print("Invalid Date, ");
//    } else {
//      print("The " + days + "-Day Moving Average, Starting on: " + date +
//              " is " + stock.getMovingAverage(days, date));
//    }
//  }
//
//  @Override
//  public void calculateXDayCrossovers() {
//    print("Enter ticker symbol: ");
//    String ticker = scanner.nextLine().toUpperCase();
//    Stock stock = model.get(ticker);
//    if (stock == null) {
//      print("Invalid ticker symbol.");
//      return;
//    }
//
//    print("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
//            + "\nthe nearest business day forward will be considered");
//    print("Enter number of days: ");
//    int days = scanner.nextInt();
//    scanner.nextLine();
//
//    String startDate;
//    do {
//      print("Enter start date (YYYY-MM-DD): ");
//      startDate = scanner.nextLine();
//      if (!stock.isValidDate(startDate)) {
//        try {
//          stock.isValidDate(startDate);
//        } catch (IllegalArgumentException e) {
//          print(e.getMessage());
//        }
//        print("Invalid start date. Please enter a valid market date.");
//      }
//    }
//    while (!stock.isValidDate(startDate));
//
//    String endDate;
//    do {
//      print("Enter end date (YYYY-MM-DD): ");
//      endDate = scanner.nextLine();
//      if (!stock.isValidDate(endDate)) {
//        try {
//          stock.isValidDate(endDate);
//        } catch (IllegalArgumentException e) {
//          print(e.getMessage());
//        }
//        print("Invalid end date. Please enter a valid market date.");
//      }
//    }
//    while (!stock.isValidDate(endDate));
//
//    if (days <= 0) {
//      print("Quantity must be greater than 0 and a whole number");
//    } else {
//      print("The " + days + "-Day Crossover for Date Range: " + startDate +
//              " - " + endDate + " is: \n" + stock.getCrossovers(startDate, endDate, days));
//    }
//  }
//
//  private void addAndBuyStock(Portfolio portfolio) {
//    print("You are Buying this Stock at Date: " + LocalDate.now());
//    print("Enter ticker symbol: ");
//    String ticker = scanner.nextLine().toUpperCase();
//    Stock stock = model.get(ticker);
//    if (stock == null) {
//      print("Invalid ticker symbol.");
//      return;
//    }
//
//    print("Enter quantity: ");
//    int quantity = scanner.nextInt();
//    scanner.nextLine();
//    if (quantity > 0) {
//      portfolio.add(stock, quantity);
//      print("You have added " + quantity + " shares of " + ticker.toUpperCase()
//              + " to your portfolio!");
//    } else {
//      print("Quantity must be greater than 0 and a whole number");
//    }
//  }
//
//  @Override
//  public void viewPortfolio() {
//    Map<String, Portfolio> portfolios = model.getPortfolios();
//    if (portfolios.isEmpty()) {
//      print("No portfolios available. Please create one first.");
//      return;
//    }
//
//    print("Available Portfolios:");
//    int i = 1;
//    for (String name : portfolios.keySet()) {
//      print(i + ". " + name);
//      i++;
//    }
//    print(i + ". Go Back");
//
//    print("Choose a portfolio to view: ");
//    int option = scanner.nextInt();
//    scanner.nextLine();
//
//    if (option > 0 && option <= portfolios.size()) {
//      String selectedPortfolioName = (String) portfolios.keySet().toArray()[option - 1];
//      Portfolio portfolio = portfolios.get(selectedPortfolioName);
//      print(portfolio.toString());
//      viewPortfolioChooseMenuScreen(portfolio);
//    } else if (option == portfolios.size() + 1) {
//      return;
//    } else {
//      print("Invalid choice. Please try again.");
//    }
//  }
//
//  private void viewPortfolioChooseMenuScreen(Portfolio portfolio) {
//    while (true) {
//      print("You are in the Portfolio: " + portfolio.getName());
//      print("1. Add a Stock");
//      print("2. Remove a Stock");
//      print("3. Find Portfolio Value");
//      print("4. View Entire Portfolio");
//      print("5. Go Back");
//
//      int option = scanner.nextInt();
//      scanner.nextLine();
//
//      switch (option) {
//        case 1:
//          addAndBuyStock(portfolio);
//          break;
//        case 2:
//          print("Enter ticker symbol to remove: ");
//          String stockSymbol = scanner.nextLine().toUpperCase();
//          Stock stockToRemove = model.get(stockSymbol);
//          if (stockToRemove == null) {
//            print("Invalid ticker symbol.");
//            return;
//          }
//          print("Enter quantity: ");
//          int numOfShares = scanner.nextInt();
//          scanner.nextLine();
//          portfolio.remove(stockToRemove, numOfShares);
//          print("You have removed: " + numOfShares + " shares of "
//                  + stockToRemove.toString().toUpperCase()
//                  + " from the portfolio " + portfolio.getName() + "!\n");
//          break;
//        case 3:
//          print("DISCLAIMER: Date entered cannot be a weekend. If you have entered " +
//                  "a weekend, you will be prompted to try again.");
//          print("Enter date (YYYY-MM-DD): ");
//          String date = scanner.nextLine();
//          double value = portfolio.valueOfPortfolio(date);
//          print("The value of the portfolio on " + date + " is: " + value);
//          break;
//        case 4:
//          print(portfolio.toString());
//          break;
//        case 5:
//          return;
//        default:
//          print("Invalid choice. Please try again.");
//      }
//    }
//  }
//
//  @Override
//  public void createPortfolio() {
//    print("Enter a name for your portfolio: ");
//    String name = scanner.nextLine();
//    model.makePortfolio(name);
//    Portfolio portfolio = model.getPortfolios().get(name);
//    print("Portfolio '" + name + "' created.");
//    addAndBuyStock(portfolio);
//  }
//
//
////  @Override
////  public void loadChosenPortfolio() {
////
////  }
//}
