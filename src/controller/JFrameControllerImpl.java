package controller;

import controller.commands.PlotPortfolioCommand;
import controller.commands.PortfolioValueCommand;
import model.Model;
import model.Portfolio;
import view.JFrameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    view.addSavePortfolioListener(this);
    view.addLoadPortfolioListener(this);
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
      case "savePortfolio":
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

  // buy / sell
  private void handleTransaction() {
    // implement transaction logic here
    String transactionType = view.getTransactionType().trim();
    String stockTicker = view.getStockTicker().trim();
    int sharesNum = Integer.parseInt(view.getNumOfShares().trim());
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    Portfolio portfolio = model.getPortfolios().get(view.getPortfolioName());

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//    String ret = String.valueOf(portfolio.add(format.format(year + "-" + month + "-" + day)));


  }

  private void handleFindValue() {
    String year = view.getTransactionYear().trim();
    String month = view.getTransactionMonth().trim();
    String day = view.getTransactionDay().trim();
    Portfolio portfolio = model.getPortfolios().get(view.getPortfolioName());
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    String ret = String.valueOf(portfolio.valueOfPortfolio(format.format(year
            + "-" + month + "-" + day)));

    if (ret.equals("Cannot check portfolio value on weekend, please enter a market date")) {
      view.displayErrorMessage("Please Enter a Valid Market Date!");
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
