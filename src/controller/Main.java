package controller;

import java.io.InputStreamReader;

/**
 * The entry point of the stock investment application.
 * This class initializes the necessary components and starts the application.
 */
public class Main {

  /**
   * The main method that serves as the entry point of the application.
   * Initializes the input and output streams, creates a controller, and starts the application.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Readable in = new InputStreamReader(System.in);
    Appendable out = System.out;
    Controller controller = new ControllerImpl(in, out);
    controller.controllerGo();
  }
}
