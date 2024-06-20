package controller;

import model.Model;
import model.Portfolio;
import model.Stock;
import view.JFrameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    if (!portfolioName.isEmpty() && !model.getPortfolios().containsKey(portfolioName)) {
      model.makePortfolio(portfolioName);
      view.addPortfolioToList(portfolioName);
      view.displayMessage("Portfolio created successfully.");
      view.closePortfolioMenu();
    } else {
      view.displayErrorMessage("Invalid or existing portfolio name.");
    }
  }

  private void handleTransaction() {
    String transactionType = view.getTransactionType().trim();
    String stockTicker = view.getStockTicker().trim().toUpperCase();

    int sharesNum;
    try {
      sharesNum = Integer.parseInt(view.getNumOfShares().trim());
    } catch (NumberFormatException e) {
      view.displayErrorMessage(e.getMessage());
      return;
    }

    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();

    Portfolio portfolio = model.getPortfolios().get(view.getPortfolioName());


    if (year.isEmpty() || month.isEmpty() || day.isEmpty()) {
      view.displayMessage("Portfolio saved with date: " + year + "-" + month + "-" + day);
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

    if (transactionType.equalsIgnoreCase("buy")) {
      try {
        portfolio.add(stock, sharesNum, date);
      } catch (IllegalArgumentException e) {
        if (e.getMessage().equals("Shares removed must be greater than zero.")) {
          view.displayMessage("Shares removed must be greater than zero.");
        } else if (e.getMessage().equals("Transaction date cannot be before the " +
                "latest transaction date.")) {
          view.displayMessage("Transaction date cannot be before the " +
                  "latest transaction date.");
        } else if (e.getMessage().equals("Cannot remove more shares than the " +
                "number of shares present.")) {
          view.displayMessage("Cannot remove more shares than the " +
                  "number of shares present.");
        }
      }
      view.displayMessage("Bought " + sharesNum + " share(s) of " + stock + " on " + date);
    } else if (transactionType.equalsIgnoreCase("sell")) {
      try {
        portfolio.remove(stock, sharesNum, date);
      } catch (IllegalArgumentException e) {
        if (e.getMessage().equals("Shares removed must be greater than zero.")) {
          view.displayMessage("Shares removed must be greater than zero.");
        } else if (e.getMessage().equals("Transaction date cannot be before the " +
                "latest transaction date.")) {
          view.displayMessage("Transaction date cannot be before the " +
                  "latest transaction date.");
        } else if (e.getMessage().equals("Cannot remove more shares than the " +
                "number of shares present.")) {
          view.displayMessage("Cannot remove more shares than the " +
                  "number of shares present.");
        }
      }
      view.displayMessage(("Sold " + sharesNum + " share(s) of " + stock + " on " + date));
    } else {
      view.displayErrorMessage("Invalid transaction type.");
    }
  }

  private void handleFindValue() {
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    Portfolio portfolio = model.getPortfolios().get(view.getPortfolioName());
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    String ret = String.valueOf(portfolio.valueOfPortfolio(format.format(year
            + "-" + month + "-" + day)));

    if (ret.equals("Cannot check portfolio value on weekend, please enter a market date.")) {
      view.displayErrorMessage("Please Enter a Valid Market Date!");
    } else if (ret.equals(("Date cannot be in the future."))) {
      view.displayErrorMessage(("Date cannot be in the future."));
    } else {
      view.displayMessage(ret);
    }

  }

  private void handleFindComposition() {
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    Portfolio portfolio = model.getPortfolios().get(view.getPortfolioName());
    String ret = portfolio.getCompositionAtDate(format.format(year + "-" + month + "-" + day));
    if (ret.equals("No transactions have been made in this portfolio yet.")) {
      view.displayErrorMessage("No transactions have been made in this portfolio yet.");
    } else {
      view.displayMessage(ret);
    }
  }


  private void handleSavePortfolio() {
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    try {
      model.savePortfolio(view.getPortfolioName(), format.format(year + "-" + month + "-" + day));
      view.displayMessage("Portfolio " + view.getPortfolioName() + " saved.");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  private void handleLoadPortfolio() {
    view.loadNewPortfolio();
    model.loadPortfolio(view.getPortfolioName(), view.getLoadedPortfolioFile().getAbsolutePath());
  }
}
