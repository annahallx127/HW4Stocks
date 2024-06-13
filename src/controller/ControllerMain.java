package controller;

import java.util.Scanner;

import model.ModelImpl;

import view.View;
import view.ViewImpl;

/**
 * The entry point of the stock investment application.
 * This class initializes the necessary components and starts the application.
 */
public class ControllerMain {
  /**
   * The main method that serves as the entry point of the application.
   * Initializes the input and output streams, creates a controller, and starts the application.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    View view = new ViewImpl(System.out);
    ModelImpl model = new ModelImpl();
    Controller controller = new ControllerImpl(model, view, scanner, System.out);

    controller.runController(controller, view, scanner);
  }
}