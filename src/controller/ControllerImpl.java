package controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import controller.commands.CalculateGainLossCommand;
import controller.commands.CreatePortfolioCommand;
import controller.commands.CrossoverCommand;
import controller.commands.DistributionValueCommand;
import controller.commands.MovingAverageCommand;
import controller.commands.PlotPortfolioCommand;
import controller.commands.PortfolioValueCommand;
import controller.commands.ReBalancePortfolioCommand;
import controller.commands.SellStockCommand;
import controller.commands.ViewCompositionCommand;
import model.Model;
import model.ModelPortfolio;
import model.Portfolio;
import model.Stock;
import view.View;
import model.PlotInterval;

/**
 * Represents the controller for the stock investment program. The controller is responsible for
 * taking input from the user and passing it to the model to be processed. It then takes the output
 * from the model and passes it to the view to be displayed to the user.
 * The controller acts as a middleman between the model and the view, ensuring that the user's
 * input is correctly processed and displayed. It is responsible for handling exceptions thrown by
 * the model and displaying error messages to the user through the view.
 * It uses the Readable and Appendable interfaces to read input from the user and display output
 * to the user. It also creates instances of the model and view to interact with the user and
 * process the data.
 */

public class ControllerImpl implements Controller {

  private final Scanner scanner;
  private final Appendable out;
  private final Model model;
  private static View view;

  public ControllerImpl(Model model, View view, Scanner scanner, Appendable out) {
    this.model = model;
    this.view = view;
    this.scanner = scanner;
    this.out = out;
  }

  @Override
  public Stock getStock(String symbol) throws IllegalArgumentException {
    return model.get(symbol);
  }

  @Override
  public Map<String, Portfolio> getPortfolios() {
    return model.getPortfolios();
  }

  @Override
  public void makePortfolio(String name) {
    model.makePortfolio(name);
    model.getPortfolios().put(name, new ModelPortfolio(name));
  }

  @Override
  public void savePortfolio(String name, String date) {
    model.savePortfolio(name, date);
  }

  @Override
  public void loadPortfolio(String name, String path) {
    model.loadPortfolio(name, path);
  }

  @Override
  public void executeCommand(ControllerCommand command) {
    command.execute(this);
  }

  @Override
  public void displayMessage(String message) {
    view.print(message);
  }


  @Override
  public void displayError(String message) {
    view.print("Error: " + message);
  }

  @Override
  public void runController(Controller controller, View view, Scanner scanner) {
    while (true) {
      printMainMenu(view);
      int choice = getUserInput(scanner);
      if (choice == 7) {
        view.print("Exiting...");
        break;
      }
      handleUserChoice(controller, scanner, view, choice);
    }
  }

  // the main menu prompt
  private static void printMainMenu(View view) {
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
  }

  private static int getUserInput(Scanner scanner) {
    try {
      return scanner.nextInt();
    } catch (InputMismatchException e) {
      scanner.nextLine();
      return -1;
    }
  }

  //controls the user choice and the main menu loop
  private static void handleUserChoice(Controller controller, Scanner scanner, View view, int choice) {
    switch (choice) {
      case 1:
        handleCalculateGainOrLoss(controller, scanner, view);
        break;
      case 2:
        handleCalculateXDayMovingAverage(controller, scanner, view);
        break;
      case 3:
        handleCalculateXDayCrossovers(controller, scanner, view);
        break;
      case 4:
        handleCreatePortfolio(controller, scanner, view);
        break;
      case 5:
        handleViewPortfolio(controller, scanner, view);
        break;
      case 6:
        handleLoadPortfolio(controller, scanner, view);
        break;
      default:
        view.print("Invalid Operation!");
    }
  }

  private static void handleCalculateGainOrLoss(Controller controller, Scanner scanner, View view) {
    view.print("Enter ticker symbol: ");
    scanner.nextLine();
    String ticker = scanner.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      view.print(e.getMessage());
      return;
    }

    String startDate;
    LocalDate validDate;
    int year;
    int month;
    int day;
    do {
      view.print("DISCLAIMER: If you have entered any non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter start date (YYYY-MM-DD): ");

      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        validDate = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = LocalDate.of(year, month, day);

      startDate = validDate.toString();

      if (!stock.isValidDate(startDate)) {
        try {
          stock.isValidDate(startDate);
        } catch (IllegalArgumentException e) {
          view.print(e.getMessage());
        }
        view.print("Invalid start date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(startDate));

    String endDate;
    LocalDate validDate2;
    int year2;
    int month2;
    int day2;
    do {
      view.print("Enter end date (YYYY-MM-DD): ");
      view.print("Enter a valid year (YYYY): ");
      year2 = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month2 = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day2 = scanner.nextInt();

      try {
        validDate = LocalDate.of(year2, month2, day2);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      endDate = validDate.toString();


      if (!stock.isValidDate(endDate)) {
        try {
          stock.isValidDate(endDate);
        } catch (IllegalArgumentException e) {
          view.print(e.getMessage());
        }
        view.print("Invalid end date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(endDate));

    ControllerCommand command = new CalculateGainLossCommand(ticker, startDate, endDate);
    controller.executeCommand(command);
  }


  private static void handleCalculateXDayMovingAverage(Controller controller, Scanner scanner, View view) {
    view.print("Enter ticker symbol: ");
    scanner.nextLine();
    String ticker = scanner.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      view.print(e.getMessage());
      return;
    }

    view.print("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    view.print("Enter number of days: ");
    int days = scanner.nextInt();
    view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
            "date backwards will be considered");
    view.print("Enter date (YYYY-MM-DD): ");
    view.print("Enter valid Year (YYYY): ");
    int year = scanner.nextInt();
    view.print("Enter valid Month (MM): ");
    int month = scanner.nextInt();
    view.print("Enter valid Day (DD): ");
    int day = scanner.nextInt();

    LocalDate validDate;
    try {
      validDate = LocalDate.of(year, month, day);
    } catch (DateTimeParseException e) {
      view.print("Invalid date. Please enter a valid date.");
      return;
    }

    String date = validDate.toString();

    if (days <= 0) {
      view.print("Quantity must be greater than 0 and a whole number");
    } else if (!stock.isValidDate(date)) {
      try {
        stock.isValidDate(date);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
      }
      view.print("Invalid Date.");
    }

    ControllerCommand command = new MovingAverageCommand(ticker, days, date);
    controller.executeCommand(command);
  }

  private static void handleCalculateXDayCrossovers(Controller controller, Scanner scanner, View view) {
    view.print("Enter ticker symbol: ");
    scanner.nextLine();
    String ticker = scanner.nextLine().toUpperCase();
    Stock stock;
    try {
      stock = controller.getStock(ticker);
      stock.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      view.print(e.getMessage());
      return;
    }

    view.print("DISCLAIMER: if you have entered a date range where it falls on a weekend,"
            + "\nthe nearest business day forward will be considered");
    view.print("Enter number of days: ");
    int days;

    try {
      days = scanner.nextInt();
    } catch (InputMismatchException e) {
      view.print("Invalid number. Please try again.");
      return;
    }

    try {
      Integer.parseInt(String.valueOf(days));
    } catch (NumberFormatException e) {
      view.print("Invalid number. Please try again.");
    }

    String startDate;
    LocalDate validDate;
    int year;
    int month;
    int day;
    do {
      view.print("DISCLAIMER: If you have entered any non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter start date (YYYY-MM-DD): ");

      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        validDate = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = LocalDate.of(year, month, day);

      startDate = validDate.toString();

      if (!stock.isValidDate(startDate)) {
        try {
          stock.isValidDate(startDate);
        } catch (IllegalArgumentException e) {
          view.print(e.getMessage());
        }
        view.print("Invalid start date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(startDate));

    String endDate;
    LocalDate validDate2;
    int year2;
    int month2;
    int day2;
    do {
      view.print("Enter end date (YYYY-MM-DD): ");
      view.print("Enter a valid year (YYYY): ");
      year2 = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month2 = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day2 = scanner.nextInt();

      try {
        validDate2 = LocalDate.of(year2, month2, day2);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      endDate = validDate2.toString();


      if (!stock.isValidDate(endDate)) {
        try {
          stock.isValidDate(endDate);
        } catch (IllegalArgumentException e) {
          view.print(e.getMessage());
        }
        view.print("Invalid end date. Please enter a valid market date.");
      }
    }
    while (!stock.isValidDate(endDate));

    if (days <= 0) {
      view.print("Quantity must be greater than 0 and a whole number");
    }

    ControllerCommand command = new CrossoverCommand(ticker, days, startDate, endDate);
    controller.executeCommand(command);
  }

  private static void addAndBuyStock(Portfolio portfolio, Controller controller, Scanner scanner) {
    while (true) {
      view.print("Enter ticker symbol: ");
      String ticker = scanner.nextLine().toUpperCase();
      Stock stock;
      try {
        stock = controller.getStock(ticker);
        stock.isValidSymbol(ticker);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
        return;
      }

      String date;
      LocalDate validDate;
      int year;
      int month;
      int day;
      do {
        view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
                "\ndate backwards will be considered");
        view.print("Enter date you would like to purchase on (YYYY-MM-DD): ");
        view.print("Enter a valid year (YYYY): ");
        year = scanner.nextInt();
        view.print("Enter a valid month (MM): ");
        month = scanner.nextInt();
        view.print("Enter a valid day (DD): ");
        day = scanner.nextInt();

        try {
          validDate = LocalDate.of(year, month, day);
        } catch (DateTimeParseException e) {
          view.print("Invalid date. Please enter a valid date.");
          return;
        }

        validDate = LocalDate.of(year, month, day);

        date = validDate.toString();

        if (!stock.isValidDate(date)) {
          view.print("Invalid date. Please enter a valid market date.");
        }
      }
      while (!stock.isValidDate(date));

      view.print("You are Buying this Stock at Date: " + date);

      view.print("Enter quantity: ");
      int quantity;
      try {
        quantity = scanner.nextInt();
      } catch (InputMismatchException e) {
        view.print("Invalid quantity. Please try again.");
        return;
      }

      if (quantity > 0) {
        portfolio.add(stock, quantity, date);
        view.print("You have added " + quantity + " shares of " + ticker.toUpperCase()
                + " to portfolio: " + portfolio.getName() + "!");
      } else {
        view.print("Quantity must be greater than 0 and a whole number, please try again.");
        return;
      }

      controller.getPortfolios().put(portfolio.getName(), portfolio);
      portfolio.savePortfolio(date);

      view.print("Do you want to add another stock? (yes/no): ");
      scanner.nextLine();
      String response = scanner.nextLine().trim().toLowerCase();
      if (!response.equals("yes")) {
        break;
      }
    }
  }

  private static void handleCreatePortfolio(Controller controller, Scanner scanner, View view) {
    view.print("Enter a name for your portfolio: ");
    scanner.nextLine();
    String name = scanner.nextLine();

    Portfolio portfolio = controller.getPortfolios().get(name);
    if (portfolio == null) {
      controller.makePortfolio(name);
      portfolio = controller.getPortfolios().get(name);
      view.print("New portfolio created: " + name);
    } else {
      view.print("Portfolio already exists.");
    }
    ControllerCommand command = new CreatePortfolioCommand(name);
    controller.executeCommand(command);
    view.print("Your portfolio has been saved in the directory! "
            + "(it might take a while to show up) "
            + "Any changes you have made in this portfolio shall update!");

    try {
      addAndBuyStock(portfolio, controller, scanner);
    } catch (IllegalArgumentException e) {
      view.print(e.getMessage());
      return;
    }
  }

  private static void viewPortfolioChooseMenuScreen(Portfolio portfolio, Controller controller, Scanner scanner) {
    while (true) {
      view.print("You are in the Portfolio: " + portfolio.getName());
      view.print("1. Buy shares of a Stock");
      view.print("2. Sell shares of a Stock");
      view.print("3. View Entire Portfolio (To Date)");
      view.print("4. View Composition of Portfolio");
      view.print("5. Re-Balance Portfolio");
      view.print("6. Find Portfolio Value");
      view.print("7. Find Distribution of Value");
      view.print("8. View Performance Over Time");
      view.print("9. Go Back");

      int option;
      try {
        option = scanner.nextInt();
        scanner.nextLine();
      } catch (InputMismatchException e) {
        view.print("Invalid option. Please try again.");
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
          view.print(portfolio.toString());
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
          handlePlotPerformanceBarChart(portfolio, controller, scanner);
          break;
        case 9:
          return;
        default:
          view.print("Invalid choice. Please try again.");
      }
    }
  }

  private static void handleViewPortfolio(Controller controller, Scanner scanner, View view) {
    Map<String, Portfolio> portfolios = controller.getPortfolios();
    if (portfolios.isEmpty()) {
      view.print("No portfolios available. Please create one first.");
      return;
    }

    view.print("Available Portfolios:");
    int i = 1;
    for (String name : portfolios.keySet()) {
      view.print(i + ". " + name);
      i++;
    }
    view.print(i + ". Go Back");

    view.print("Choose a portfolio to view: ");
    int option;
    try {
      option = scanner.nextInt();
    } catch (InputMismatchException e) {
      view.print("Invalid option. Please try again.");
      return;
    }

    if (option > 0 && option <= portfolios.size()) {
      String selectedPortfolioName = (String) portfolios.keySet().toArray()[option - 1];
      Portfolio portfolio = portfolios.get(selectedPortfolioName);
      view.print(portfolio.toString());
      viewPortfolioChooseMenuScreen(portfolio, controller, scanner);
    } else if (option == portfolios.size() + 1) {
      return;
    } else {
      view.print("Invalid choice. Please try again.");
    }
  }


  private static void sellStocks(Portfolio portfolio, Controller controller, Scanner scanner) {

    LocalDate date;
    String validDate;
    int year;
    int month;
    int day;

    do {
      view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter date you would like to sell on (YYYY-MM-DD): ");

      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        date = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = date.toString();
      try {
        portfolio.isValidDateForPortfolio(validDate);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(validDate)) {
        view.print("Invalid date. Please enter a valid market date.");
      }
    }
    while (!portfolio.isValidDateForPortfolio(validDate));


    view.print("Enter ticker symbol to remove: ");
    scanner.nextLine();
    String ticker = scanner.nextLine().toUpperCase();
    Stock stockToRemove;
    try {
      stockToRemove = controller.getStock(ticker);
      stockToRemove.isValidSymbol(ticker);
    } catch (IllegalArgumentException e) {
      view.print(e.getMessage());
      return;
    }
    view.print("Enter quantity (cannot be fractional): ");

    double numOfShares;
    try {
      numOfShares = scanner.nextInt();
    } catch (IllegalArgumentException e) {
      view.print(e.getMessage());
      return;
    }
    view.print("You are Selling this Stock at Date: " + validDate);
    ControllerCommand command = new SellStockCommand(portfolio.getName(), stockToRemove, numOfShares, validDate);
    controller.executeCommand(command);
    controller.getPortfolios().put(portfolio.getName(), portfolio);
    portfolio.savePortfolio(validDate);
  }

  private static void viewCompositionOfPortfolioAtAnyDate(Portfolio portfolio, Controller controller, Scanner scanner) {
    LocalDate date;
    String validDate;
    int year;
    int month;
    int day;

    do {
      view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter date you would like to view composition on (YYYY-MM-DD): ");

      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        date = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = date.toString();
      try {
        portfolio.isValidDateForPortfolio(validDate);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
      }
      if (!portfolio.isValidDateForPortfolio(validDate)) {
        view.print("Invalid date. Please enter a valid market date.");
      }
    }
    while (!portfolio.isValidDateForPortfolio(validDate));


    ControllerCommand command = new ViewCompositionCommand(portfolio.getName(), validDate);
    controller.executeCommand(command);
  }

  private static void reBalanceChosenPortfolio(Portfolio portfolio, Controller controller, Scanner scanner) {
    LocalDate date;
    String validDate;
    int year;
    int month;
    int day;

    do {
      view.print("DISCLAIMER: This program will not allow you to re-balance on a non " +
              "market date! If you have entered a non-market date, please try again.");
      view.print("Enter date you would like to re-balance on (YYYY-MM-DD): ");

      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        date = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = date.toString();
      try {
        portfolio.isValidDateForPortfolio(validDate);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
        return;
      }
      if (!portfolio.isValidDateForPortfolio(validDate)) {
        view.print("Invalid date. Please enter a valid market date.");
      }
    }
    while (!portfolio.isValidDateForPortfolio(validDate));


    Map<Stock, Integer> targetWeights = new HashMap<>();
    view.print("Enter the weights for each stock in your portfolio. Weights must total to" +
            " 100%. ");
    scanner.nextLine();

    int totalWeight = 0;
    for (Stock stock : portfolio.getStocks().keySet()) {
      view.print("Enter weight for " + portfolio.getStocks().keySet());
      int weight = scanner.nextInt();
      scanner.nextLine();
      while (weight < 0 || weight > 100) {
        view.print("Invalid weight. Please enter a value between 0 and 100.");
        weight = scanner.nextInt();
      }
      targetWeights.put(stock, weight);
      totalWeight += weight;
    }

    if (totalWeight != 100) {
      view.print("The total weight must be exactly 100%. You entered a total of " + totalWeight + "%.");
      return;
    }

    ControllerCommand command = new ReBalancePortfolioCommand(validDate, portfolio.getName(), targetWeights);
    controller.executeCommand(command);
  }

  private static void totalValueOfPortfolioAtDate(Portfolio portfolio, Controller controller, Scanner scanner) {
    LocalDate date;
    String validDate;
    int year;
    int month;
    int day;

    do {
      view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter date you would like to get value on (YYYY-MM-DD): ");

      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        date = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = date.toString();
      try {
        portfolio.isValidDateForPortfolio(validDate);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
        return;
      }
      if (!portfolio.isValidDateForPortfolio(validDate)) {
        view.print("Invalid date. Please enter a valid market date.");
      }
    }
    while (!portfolio.isValidDateForPortfolio(validDate));


    ControllerCommand command = new PortfolioValueCommand(portfolio.getName(), validDate);
    controller.executeCommand(command);
  }


  private static void findDistributionValueAtDate(Portfolio portfolio, Controller controller, Scanner scanner) {
    LocalDate date;
    String validDate;
    int year;
    int month;
    int day;

    do {
      view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter date you would like to find distribution on (YYYY-MM-DD): ");

      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        date = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = date.toString();
      try {
        portfolio.isValidDateForPortfolio(validDate);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
        return;
      }
      if (!portfolio.isValidDateForPortfolio(validDate)) {
        view.print("Invalid date. Please enter a valid market date.");
      }
    }
    while (!portfolio.isValidDateForPortfolio(validDate));

    ControllerCommand command = new DistributionValueCommand(portfolio.getName(), validDate);
    controller.executeCommand(command);
  }

  private static void handleLoadPortfolio(Controller controller, Scanner scanner, View view) {
    view.print("Enter the name of the portfolio to load: ");
    view.print("Format should just be the NAMEOFYOURPORTFOLIO (all in one line): ");

    scanner.nextLine();
    String portfolioName = scanner.nextLine();

    try {
      controller.loadPortfolio(portfolioName, "src/data/portfolios");
      Portfolio loadedPortfolio = controller.getPortfolios().get(portfolioName);
      if (loadedPortfolio != null) {
        view.print("Portfolio '" + portfolioName + "' loaded successfully.");
        viewPortfolioChooseMenuScreen(loadedPortfolio, controller, scanner);
      } else {
        view.print("Portfolio '" + portfolioName + "' does not exist.");
      }
    } catch (Exception e) {
      view.print("Error loading portfolio: " + e.getMessage());
    }
  }

  private static void handlePlotPerformanceBarChart(Portfolio portfolio,
                                                    Controller controller, Scanner scanner) {

    LocalDate startDate;
    String validDate;
    int year;
    int month;
    int day;
    do {
      view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter start date (YYYY-MM-DD): ");
      view.print("Enter a valid year (YYYY): ");
      year = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day = scanner.nextInt();

      try {
        startDate = LocalDate.of(year, month, day);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate = startDate.toString();
      try {
        portfolio.isValidDateForPortfolio(validDate);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
        return;
      }
      if (!portfolio.isValidDateForPortfolio(validDate)) {
        view.print("Invalid date. Please enter a valid market date.");
      }
    }
    while (!portfolio.isValidDateForPortfolio(validDate));

    LocalDate endDate;
    String validDate2;
    int year2;
    int month2;
    int day2;
    do {
      view.print("DISCLAIMER: If you have entered a non market date, the nearest market " +
              "\ndate backwards will be considered");
      view.print("Enter end date (YYYY-MM-DD): ");
      view.print("Enter a valid year (YYYY): ");
      year2 = scanner.nextInt();
      view.print("Enter a valid month (MM): ");
      month2 = scanner.nextInt();
      view.print("Enter a valid day (DD): ");
      day2 = scanner.nextInt();

      try {
        endDate = LocalDate.of(year2, month2, day2);
      } catch (DateTimeParseException e) {
        view.print("Invalid date. Please enter a valid date.");
        return;
      }

      validDate2 = endDate.toString();
      try {
        portfolio.isValidDateForPortfolio(validDate2);
      } catch (IllegalArgumentException e) {
        view.print(e.getMessage());
        return;
      }
      if (!portfolio.isValidDateForPortfolio(validDate2)) {
        view.print("Invalid date. Please enter a valid market date.");
      }
    }
    while (!portfolio.isValidDateForPortfolio(validDate2));

    String interval;
    do {
      view.print("Enter interval (DAYS, WEEKS, MONTHS, YEARS): ");
      scanner.nextLine();
      interval = scanner.nextLine().toUpperCase();
      if (!interval.equals("DAYS") && !interval.equals("WEEKS") && !interval.equals("MONTHS")
              && !interval.equals("YEARS")) {
        view.print("Invalid interval. Please enter a valid interval.");
      }
    }
    while (!validDate.equals("DAYS") && !validDate.equals("WEEKS")
            && !validDate.equals("MONTHS") && !validDate.equals("YEARS"));

    PlotInterval plotInterval = PlotInterval.valueOf(interval);
    ControllerCommand command = new PlotPortfolioCommand(portfolio.getName(), validDate, validDate2,
            plotInterval);
    command.execute(controller);
  }
}