package controller;

import java.util.Scanner;

import model.Model;
import model.ModelImpl;

import view.JFrameView;
import view.JFrameViewImpl;
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
    Model model = new ModelImpl();
    if (args.length > 0 && args[0].equals("-text")) {
      Scanner scanner = new Scanner(System.in);
      View view = new ViewImpl(System.out);
      Controller controller = new ControllerImpl(model, view);

      controller.runController(controller, view, scanner);
    } else if (args.length == 0) {
      model = new ModelImpl();
      JFrameView view = new JFrameViewImpl();
      new JFrameController(model, view);
    } else {
      System.err.println("Error with command line arguments");
      System.exit(-1);
    }
  }
}