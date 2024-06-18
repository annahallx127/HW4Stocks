package controller;

import model.ModelImpl;
import view.View;
import view.ViewImpl;

public class JFrameMain {

    public static void main(String[] args) {
        View view = new ViewImpl(System.out);
        ModelImpl model = new ModelImpl();

    }
}
