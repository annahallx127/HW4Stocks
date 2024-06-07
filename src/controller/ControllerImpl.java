package controller;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import model.Model;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;
import view.View;
import view.ViewImpl;

/**
 * Represents the controller for the stock investment program. The controller is responsible for
 * taking input from the user and passing it to the model to be processed. It then takes the output
 * from the model and passes it to the view to be displayed to the user.
 *
 * <p>
 * The controller acts as a middleman between the model and the view, ensuring that the user's
 * input is correctly processed and displayed. It is responsible for handling exceptions thrown by
 * the model and displaying error messages to the user through the view.
 * </p>
 *
 * <p>
 * It uses the Readable and Appendable interfaces to read input from the user and display output
 * to the user. It also creates instances of the model and view to interact with the user and
 * process the data.
 * </p>
 */
public class ControllerImpl implements Controller {
  private final Readable in;
  private final Appendable out;
  private Model model;
  private View view;

  public ControllerImpl(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
    this.model = new ModelImpl();
    this.view = new ViewImpl(this);
  }

  @Override
  public void go() {
    view.run();
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
  }

  @Override
  public String next() {
    Scanner s = new Scanner(in);
    return s.next();
  }

  @Override
  public int nextInt() {
    Scanner s = new Scanner(in);
    return s.nextInt();
  }

  @Override
  public String nextLine() {
    Scanner s = new Scanner(in);
    return s.nextLine();
  }

  @Override
  public Appendable getAppendable() {
    return out;
  }
}

