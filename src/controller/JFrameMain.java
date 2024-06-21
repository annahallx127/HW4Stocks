package controller;

import model.ModelImpl;
import view.JFrameView;
import model.Model;
import view.JFrameViewImpl;

/**
 * The JFrameMain class serves as the entry point for the Stock Investment application.
 * This class is responsible for initializing the model, view, and controller components
 * and starting the application.
 * It creates instances of the model and view, and ties them together with a controller
 * to manage the interactions between them, following the MVC (Model-View-Controller)
 * design pattern.
 */
public class JFrameMain {

  /**
   * The main method sets up the application by creating instances of the model and view,
   * and then initializing the controller with these instances.
   * This setup ensures that all parts of the application are properly coordinated
   * from the start, enabling responsive and structured user interactions.
   *
   * @param args The command line arguments passed to the application, not used in
   *             this application.
   */
  public static void main(String[] args) {
    Model model = new ModelImpl();
    JFrameView view = new JFrameViewImpl();
    new JFrameController(model, view);
  }
}
