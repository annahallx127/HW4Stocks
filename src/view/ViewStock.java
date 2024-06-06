package view;

import model.Model;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;

import java.util.Map;
import java.util.Scanner;

public class ViewStock implements View {
  private final Model model;
  private final Scanner scanner;

  public ViewStock(Model model) {
    this.model = model;
    this.scanner = new Scanner(System.in);
    run();
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

  @Override
  public void calculateGainOrLoss() {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock chosenStock = model.get(ticker);
    if (chosenStock == null) {
      System.out.println("Invalid ticker symbol.");
      return;
    }

    System.out.println("DISCLAIMER: if you have entered a date range where it falls on a weekend, "
            + "\n the nearest business day forward will be considered");
    System.out.print("Start Date (YYYY-MM-DD): ");
    String startDate = scanner.nextLine();
    System.out.print("End Date (YYYY-MM-DD): ");
    String endDate = scanner.nextLine();

    double gainOrLoss = chosenStock.gainedValue(startDate, endDate);
    System.out.println("The gain or loss of " + ticker.toUpperCase() + " is: " + gainOrLoss);
  }

  @Override
  public void calculateXDayMovingAverage() {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock stock = model.get(ticker);
    if (stock == null) {
      System.out.println("Invalid ticker symbol.");
      return;
    }

    System.out.println("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    System.out.print("Enter number of days: ");
    int days = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Enter date (YYYY-MM-DD): ");
    String date = scanner.nextLine();

    if (days <= 0) {
      System.out.println("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(date)) {
      System.out.println("Invalid Start Date");
    } else {
      System.out.println("The " + days + "-Day Moving Average, Starting on: " + date +
              " is " + stock.getMovingAverage(days, date));
    }
  }

  @Override
  public void calculateXDayCrossovers() {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock stock = model.get(ticker);
    if (stock == null) {
      System.out.println("Invalid ticker symbol.");
      return;
    }

    System.out.println("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    System.out.print("Enter number of days: ");
    int days = scanner.nextInt();
    scanner.nextLine();

    System.out.print("Enter start date (YYYY-MM-DD): ");
    String startDate = scanner.nextLine();
    System.out.print("Enter end date (YYYY-MM-DD): ");
    String endDate = scanner.nextLine();

    if (days <= 0) {
      System.out.println("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(startDate) || !stock.isValidDate(endDate)) {
      System.out.println("Invalid Start or End Date");
    } else {
      System.out.println("The " + days + "-Day Crossover for Date Range: " + startDate +
              " - " + endDate + " is: \n" + stock.getCrossovers(startDate, endDate, days));
    }
  }

  private void addAndBuyStock(Portfolio portfolio) {
    System.out.print("Enter ticker symbol: ");
    String ticker = scanner.nextLine();
    Stock stock = model.get(ticker);
    if (stock == null) {
      System.out.println("Invalid ticker symbol.");
      return;
    }

    System.out.print("Enter quantity: ");
    int quantity = scanner.nextInt();
    scanner.nextLine();
    if (quantity > 0) {
      portfolio.add(stock, quantity);
      System.out.println("You have added " + quantity + " shares of " + ticker
              + " to your portfolio!");
    } else {
      System.out.println("Quantity must be greater than 0 and a whole number");
    }
  }

  @Override
  public void viewPortfolio() {
    Map<String, Portfolio> portfolios = model.getPortfolios();
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

    System.out.print("Choose a portfolio to view: ");
    int option = scanner.nextInt();
    scanner.nextLine();

    if (option > 0 && option <= portfolios.size()) {
      String selectedPortfolioName = (String) portfolios.keySet().toArray()[option - 1];
      Portfolio portfolio = portfolios.get(selectedPortfolioName);
      System.out.println(portfolio);
      viewPortfolioChooseMenuScreen(portfolio);
    } else if (option == portfolios.size() + 1) {
      return;
    } else {
      System.out.println("Invalid choice. Please try again.");
    }
  }

  private void viewPortfolioChooseMenuScreen(Portfolio portfolio) {
    while (true) {
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
          System.out.print("Enter ticker symbol to remove: ");
          String stockSymbol = scanner.nextLine();
          Stock stockToRemove = model.get(stockSymbol);
          if (stockToRemove == null) {
            System.out.println("Invalid ticker symbol.");
            return;
          }
          System.out.print("Enter quantity: ");
          int numOfShares = scanner.nextInt();
          scanner.nextLine();
          portfolio.remove(stockToRemove, numOfShares);
          break;
        case 3:
          System.out.println("DISCLAIMER: if you have entered a date where it falls on a weekend,"
                  + "\nthe nearest business day forward will be considered");
          System.out.print("Enter date (YYYY-MM-DD): ");
          String date = scanner.nextLine();
          double value = portfolio.valueOfPortfolio(date);
          System.out.println("The value of the portfolio on " + date + " is: " + value);
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
  }

  @Override
  public void createPortfolio() {
    System.out.print("Enter a name for your portfolio: ");
    String name = scanner.nextLine();
    Portfolio portfolio = model.makePortfolio(name);
    model.addPortfolio(name, portfolio);
    System.out.println("Portfolio '" + name + "' created.");
    addAndBuyStock(portfolio);
  }

  //replace later for controller / main
  public static void main(String[] args) {
    Model model = new ModelImpl();
    new ViewStock(model);
  }
}