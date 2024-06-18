package controller;

import model.Model;
import view.JFrameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class JFrameControllerImpl implements ActionListener {
    private final Model model;
    private final JFrameView view;

    public JFrameControllerImpl(Model model, JFrameView view) {
        this.view = Objects.requireNonNull(view, "View can not null.");;
        this.model = Objects.requireNonNull(model, "Model can not be null.");
        view.addCreateNewPortfolioListener(this);
        view.buyOrSellEnterButtonListener(this);
        view.getValueButtonListener(this);
        view.getCompositionButtonListener(this);
        view.savePortfolioButtonListener(this);
        view.addLoadPortfolioListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "createNewPortfolio":
            case "buy/sell":
            case "value":
            case "composition":
            case "savePortfolio":
            case "loadPortfolio":
        }
    }
}
