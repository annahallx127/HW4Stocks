package controller;

import model.ModelImpl;
import view.JFrameView;
import model.Model;

public class JFrameMain {

  public static void main(String[] args) {
    Model model = new ModelImpl();
    JFrameView view = new JFrameView();
    new JFrameControllerImpl(model, view);
  }
}
