package view;

import model.Model;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ViewStock {
  private final Model model;
  private final Scanner scanner;

  public ViewStock(Model model) {
    this.model = model;
    this.scanner = new Scanner(System.in);
    run();
  }

  private void run() {
    while (true) {
      System.out.println("Isaac and Anna's Stock Investment Company");
      System.out.println("Choose an Option From the Menu:");
      System.out.println("1. Calculate Gain or Loss of a Stock");
      System.out.println("2. Calculate X-Day Moving Average");
      System.out.println("3. Calculate X-Day Crossovers");
      System.out.println("4. Create a New Portfolio");
      System.out.println("5. View Existing Portfolios");
      System.out.println("6. Exit");
      System.out.print("Choose an option: ");
      int choice = scanner.nextInt();
      scanner.nextLine();

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
          System.out.println("Exiting...");
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private void calculateGainOrLoss() {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock chosenStock = model.get(ticker);
    System.out.print("Start Date (YYYY/MM/DD): ");
    String startDate = scanner.nextLine();
    System.out.print("End Date (YYYY/MM/DD): ");
    String endDate = scanner.nextLine();

    if (chosenStock.getSymbol().equalsIgnoreCase(ticker)) {
      System.out.println("The gain or loss of " + ticker.toUpperCase() + " is: " +
              chosenStock.gainedValue(startDate, endDate));
    } else {
      System.out.println("Invalid ticker symbol.");
    }
  }

  private void calculateXDayMovingAverage() {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock stock = model.get(ticker);
    System.out.print("Enter amount of days: ");
    int quantity = scanner.nextInt();
    scanner.nextLine(); // Consume the newline left-over
    System.out.print("Enter start date with YYYY/MM/DD format: ");
    String startDate = scanner.nextLine();

    //  if the date range falls along a weekend, add a message where it says that
    // "Since the date range falls under a weekend, the open day amounts are considered only"
    // can upgrade later to say the exact days
    if (quantity <= 0) {
      System.out.println("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(startDate)) {
      System.out.println("Invalid Start Date");
    } else {
      System.out.println("The" + quantity + "-Day Moving Average, Starting on:"
              + startDate + "is" + stock.getMovingAverage(quantity, startDate));
    }
  }

  private void calculateXDayCrossovers() {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock stock = model.get(ticker);
    System.out.print("Enter amount of days: ");
    int quantity = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Enter start date with YYYY/MM/DD format: ");
    String startDate = scanner.nextLine();
    System.out.print("Enter end date with YYYY/MM/DD format: ");
    String endDate = scanner.nextLine();

    //  if the date range falls along a weekend, add a message where it says that

    if (quantity <= 0) {
      System.out.println("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(startDate) || !stock.isValidDate(endDate)) {
      System.out.println("Invalid Start or End Date");
    } else {
      System.out.println("The" + quantity + "-Day Crossover for Date Range:" + startDate
              + "-" + endDate + "is: \n" + stock.getCrossovers(startDate, endDate, quantity));
    }
  }

  private void addAndBuyStock(Portfolio portfolio) {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock stock = model.get(ticker);
    System.out.print("Enter quantity: ");
    int quantity = scanner.nextInt();
    scanner.nextLine();
    if (quantity > 0) {
      portfolio.add(stock, quantity);
      // add the quantity added for?
    } else {
      System.out.println("Quantity must be greater than 0 and a whole number");
    }
    System.out.println("You have added " + ticker + " to your portfolio!");
  }

  private void viewPortfolio() {
    if (model.getPortfolios().isEmpty()) {
      System.out.println("No portfolios available. Please create one first.");
      return;
    }

    System.out.println("Available Portfolios:");
    for (int i = 0; i < model.getPortfolios().size(); i++) {
      System.out.println((i + 1) + ". " + model.getPortfolios().get(i));
    }
    System.out.println((model.getPortfolios().size() + 1) + ". Go Back");

    System.out.print("Choose a portfolio to view: ");
    int option = scanner.nextInt();
    scanner.nextLine();

    if (option > 0 && option <= model.getPortfolios().size()) {
      System.out.println(model.getPortfolios().get(option - 1));
      viewPortfolioChooseMenuScreen(model.getPortfolios().get(option - 1));
    } else if (option == model.getPortfolios().size() + 1) {
      return;
    } else {
      System.out.println("Invalid choice. Please try again.");
    }
  }

  private void viewPortfolioChooseMenuScreen(Portfolio portfolio) {
    System.out.println("1. Add a Stock");
    System.out.println("2. Remove a Stock");
    System.out.println("3. Find Portfolio Value");
    System.out.println("4. View Entire Portfolio");
    System.out.println("5. Go Back");

    int option = scanner.nextInt();
    scanner.nextLine();

    switch (option) {
      case 1:
        addAndBuyStock(portfolio);
        break;
      case 2:
        System.out.print("What stock would you like to remove? ");
        String stockSymbol = scanner.nextLine();
        Stock stockToRemove = model.get(stockSymbol);
        System.out.print("How many shares? ");
        int numOfShares = scanner.nextInt();
        scanner.nextLine();

        portfolio.remove(stockToRemove, numOfShares);
        break;
      case 3:
        System.out.print("Enter a valid Date (YYYY/MM/DD): ");
        String date = scanner.nextLine();
        portfolio.valueOfPortfolio(date);
        break;
      case 4:
        System.out.println(portfolio);
        break;
      case 5:
        return;
      default:
        System.out.println("Invalid choice. Please try again.");
    }
  }

  private void createPortfolio() {
    System.out.print("Enter a suitable name for your portfolio: ");
    String name = scanner.nextLine();
    Portfolio portfolio = model.makePortfolio(name);
    System.out.println("Portfolio '" + name + "' created.");
    addAndBuyStock(portfolio);
  }

  private void addPortfolioToMenu() {

  }

  public static void main(String[] args) {
    Model model = new ModelImpl();
    new ViewStock(model);
  }
}
