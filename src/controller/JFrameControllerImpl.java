package controller;

import model.Model;
import model.Portfolio;
import model.Stock;
import view.JFrameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class JFrameControllerImpl implements ActionListener {
  private final Model model;
  private final JFrameView view;

  public JFrameControllerImpl(Model model, JFrameView view) {
    this.view = Objects.requireNonNull(view, "View can not be null.");
    this.model = Objects.requireNonNull(model, "Model can not be null.");

    view.addCreateNewPortfolioListener(this);
    view.addBuyOrSellEnterListener(this);
    view.addFindValueEnterListener(this);
    view.addFindCompEnterListener(this);
    view.addLoadPortfolioListener(this);
    view.addSavePortfolioEnterListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "createNewPortfolio":
        view.createNewPortfolioWindow();
        break;
      case "createPortfolioConfirm":
        handleCreateNewPortfolio();
        break;
      case "buyStock":
        view.buyOrSellWindow();
        break;
      case "enterTransaction":
        handleTransaction();
        break;
      case "findValue":
        view.findValueWindow();
        break;
      case "findValueEnter":
        handleFindValue();
        break;
      case "findComposition":
        view.findCompositionWindow();
        break;
      case "findCompositionEnter":
        handleFindComposition();
        break;
      case "savePortfolioEnter":
        handleSavePortfolio();
        break;
      case "loadPortfolio":
        handleLoadPortfolio();
        break;
    }
  }

  private void handleCreateNewPortfolio() {
    String portfolioName = view.getPortfolioName();
    if (portfolioName == null || portfolioName.isEmpty()) {
      view.displayErrorMessage("Invalid portfolio name.");
      return;
    }

    try {
      model.makePortfolio(portfolioName);
      view.addPortfolioToList(portfolioName);
      view.displayMessage("Portfolio created successfully.");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
    view.closeCreateNewPortfolioWindow();
  }

  private void handleTransaction() {
    String transactionType = view.getTransactionType().trim();
    String stockTicker = view.getStockTicker().trim().toUpperCase();

    int sharesNum;
    try {
      sharesNum = Integer.parseInt(view.getNumOfShares().trim());
    } catch (NumberFormatException e) {
      view.displayErrorMessage("Invalid number of shares.");
      return;
    }

    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();

    String portfolioName = view.getPortfolioName();
    if (portfolioName == null || portfolioName.isEmpty()) {
      view.displayErrorMessage("Please select a valid portfolio.");
      return;
    }

    Portfolio portfolio = model.getPortfolios().get(portfolioName);
    if (portfolio == null) {
      view.displayErrorMessage("Portfolio not found.");
      return;
    }

    if (year.isEmpty() || month.isEmpty() || day.isEmpty()) {
      view.displayErrorMessage("Please enter a valid date.");
      return;
    }

    Stock stock;
    try {
      stock = model.get(stockTicker);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage("Invalid stock ticker. Try again.");
      return;
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date = year + "-" + month + "-" + day;
    try {
      date = format.format(format.parse(date));
    } catch (ParseException e) {
      view.displayErrorMessage("Invalid date format.");
      return;
    }

    try {
      if (transactionType.equalsIgnoreCase("buy")) {
        portfolio.add(stock, sharesNum, date);
        view.displayMessage("Bought " + sharesNum + " share(s) of " + stock + " on " + date);
      } else if (transactionType.equalsIgnoreCase("sell")) {
        portfolio.remove(stock, sharesNum, date);
        view.displayMessage("Sold " + sharesNum + " share(s) of " + stock + " on " + date);
      } else {
        view.displayErrorMessage("Invalid transaction type.");
      }
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }



  private void handleFindValue() {
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    String portfolioName = view.getPortfolioName();

    // Ensure portfolio exists
    if (portfolioName == null || portfolioName.isEmpty()) {
      view.displayErrorMessage("Please select a valid portfolio.");
      return;
    }

    Portfolio portfolio = model.getPortfolios().get(portfolioName);
    if (portfolio == null) {
      view.displayErrorMessage("Portfolio not found.");
      return;
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    String date = year + "-" + month + "-" + day;
    try {
      Date parsedDate = format.parse(date);
      String formattedDate = format.format(parsedDate);

      String ret = String.valueOf(portfolio.valueOfPortfolio(formattedDate));

      if (ret.equals("Cannot check portfolio value on weekend, please enter a market date.")) {
        view.displayErrorMessage("Please Enter a Valid Market Date!");
      } else if (ret.equals("Date cannot be in the future.")) {
        view.displayErrorMessage("Date cannot be in the future.");
      } else {
        view.displayMessage(ret);
      }
    } catch (ParseException e) {
      view.displayErrorMessage("Invalid date format.");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }


  private void handleFindComposition() {
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    String portfolioName = view.getPortfolioName();

    // Ensure portfolio exists
    if (portfolioName == null || portfolioName.isEmpty()) {
      view.displayErrorMessage("Please select a valid portfolio.");
      return;
    }

    Portfolio portfolio = model.getPortfolios().get(portfolioName);
    if (portfolio == null) {
      view.displayErrorMessage("Portfolio not found.");
      return;
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    String date = year + "-" + month + "-" + day;
    try {
      Date parsedDate = format.parse(date);
      String formattedDate = format.format(parsedDate);

      String ret = portfolio.getCompositionAtDate(formattedDate);

      if (ret.equals("No transactions have been made in this portfolio yet.")) {
        view.displayErrorMessage("No transactions have been made in this portfolio yet.");
      } else {
        view.displayMessage(ret);
      }
    } catch (ParseException e) {
      view.displayErrorMessage("Invalid date format.");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }


  private void handleSavePortfolio() {
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    String portfolioName = view.getPortfolioName();

    // Ensure portfolio exists
    if (portfolioName == null || portfolioName.isEmpty()) {
      view.displayErrorMessage("Please select a valid portfolio.");
      return;
    }

    Portfolio portfolio = model.getPortfolios().get(portfolioName);
    if (portfolio == null) {
      view.displayErrorMessage("Portfolio not found.");
      return;
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    String date = year + "-" + month + "-" + day;
    try {
      Date parsedDate = format.parse(date);
      String formattedDate = format.format(parsedDate);

      model.savePortfolio(portfolioName, formattedDate);
      view.displayMessage("Portfolio " + portfolioName + " saved.");
    } catch (ParseException e) {
      view.displayErrorMessage("Invalid date format.");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  private void handleLoadPortfolio() {
    view.loadNewPortfolio();
    model.loadPortfolio(view.getPortfolioName(), view.getLoadedPortfolioFile().getAbsolutePath());
    view.displayMessage("Portfolio " + view.getPortfolioName() + " loaded.");
  }
}
