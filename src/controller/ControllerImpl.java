package controller;

import java.io.IOException;

import model.Model;
import model.ModelImpl;
import view.View;
import view.ViewStock;

public class ControllerImpl implements Controller {
  private final Readable in;
  private final Appendable out;

  ControllerImpl(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void go(Model model, View view) throws IOException {
    model = new ModelImpl();
    new ViewStock(model);
  }
}
