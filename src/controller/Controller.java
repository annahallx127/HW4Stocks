package controller;

import java.io.IOException;

import model.Model;
import view.View;

/**
 * The controller interface for the program.
 * Used to start the program and begin the user interface.
 */
public interface Controller {
  /**
   * Start the controller and begin the program.
   *
   * @param model the model to be used.
   * @param view the view to be used.
   * @throws IOException if the controller cannot be started due to an IO error.
   */
  void go(Model model, View view) throws IOException;
}
