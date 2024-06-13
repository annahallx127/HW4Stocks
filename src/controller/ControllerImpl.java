package controller;

import java.util.Map;

import model.Model;
import model.Portfolio;
import model.Stock;
import view.View;

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
//  private final Readable in;
//  private final Appendable out;
  private Model model;
  private View view;

  /**
   * Constructs a ControllerImpl with the specified input and output.
   * Initializes the model and view components.
   *
   * @param in  the input source for user interactions
   * @param out the output target for displaying messages
   */
  public ControllerImpl(Readable in, Appendable out) {
    this.view = view;
//    this.in = in;
//    this.out = out;
//    this.model = new ModelImpl();
//    this.view = new ViewImpl(this);
  }

//  @Override
//  public void controllerGo() {
//    view.run();
//  }

  @Override
  public Stock getStock(String symbol) throws IllegalArgumentException {
    return model.get(symbol);
  }

  @Override
  public Map<String, Portfolio> getPortfolios() {
    return model.getPortfolios();
  }

  // TODO: find solution for setting portfolio from copy
//  public void setPortfolio() {
//    model.setPortfolio();
//  }

  @Override
  public void makePortfolio(String name) {
    model.makePortfolio(name);
  }

  @Override
  public void savePortfolio(String name, String date) {
    model.savePortfolio(name, date);
  }

  @Override
  public void loadPortfolio(String name, String path) {
    model.loadPortfolio(name, path);
  }


//  public Appendable getAppendable() {
//    return out;
//  }
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


}

