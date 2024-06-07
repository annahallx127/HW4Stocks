package controller;

import java.io.IOException;
import java.io.InputStreamReader;

import model.Model;
import model.ModelImpl;
import view.View;
import view.ViewStock;

public class Main {
  public static void main(String[] args) {
    try {
      Model model = new ModelImpl();
      View view = new ViewStock(model);
      Readable in = new InputStreamReader(System.in);
      Appendable out = System.out;
      Controller controller = new ControllerImpl(in, out);
      controller.go(model, view);
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
  }
}
