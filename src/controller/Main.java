package controller;

import java.io.IOException;
import java.io.InputStreamReader;

import model.Model;
import model.ModelImpl;
import view.View;
import view.ViewImpl;

/**
 * Represents the main class for the stock program, which creates instances of the model, view, and
 * controller to run the program.
 */
public class Main {
  public static void main(String[] args) {
    Readable in = new InputStreamReader(System.in);
    Appendable out = System.out;
    Controller controller = new ControllerImpl(in, out);
    controller.go();
  }
}
