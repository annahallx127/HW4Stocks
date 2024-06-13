package controller.commands;

import java.util.Scanner;

import controller.Controller;
import controller.ControllerCommand;
import controller.ControllerMain;
import model.Portfolio;

import static controller.ControllerMain.viewPortfolioChooseMenuScreen;

public class LoadPortfolioCommand implements ControllerCommand {

  private final String portfolioName;


  public LoadPortfolioCommand(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  @Override
  public void execute(Controller controller) {
    try {
      controller.loadPortfolio(portfolioName, "src/portfolios");
      Portfolio loadedPortfolio = controller.getPortfolios().get(portfolioName);
      if (loadedPortfolio != null) {
        System.out.println("Portfolio '" + portfolioName + "' loaded successfully.");
        main.viewPortfolioChooseMenuScreen(loadedPortfolio, controller, controller.getScanner());
      } else {
        System.out.println("Portfolio '" + portfolioName + "' does not exist.");
      }
    } catch (Exception e) {
      System.out.println("Error loading portfolio: " + e.getMessage());
    }
  }
}


}
