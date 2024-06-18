package view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class JFrameView extends JFrame {

    private JButton createNewPortfolioButton;
    private JButton loadNewPortfolio;

    private JButton transactionWindow;
    private JButton buyOrSellEnter;

    private JButton fileOpenButton;
    private JButton findValueWindow;
    private JButton findValueEnter;
    private JButton findCompositionWindow;
    private JButton findCompEnter;
    private JButton savePortfolio;

    private JFrame mainMenu;


    public JFrameView() {
        this.mainMenu = new JFrame("I&A's Stock Investment Company");
        mainMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainMenu.setSize(600, 500);
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);

        createMainMenuView();
    }

    private void createMainMenuView() {
        createNewPortfolioButton = new JButton();
        loadNewPortfolio = new JButton();

        getContentPane().setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.add(new JLabel("Available Portfolios"), BorderLayout.SOUTH);
        mainPanel.add(new JList<>(), BorderLayout.CENTER);// list of portfolios


        // bottom of the menu panel w/ 2 button options
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createNewPortfolioButton, BorderLayout.NORTH);
        bottomPanel.add(loadNewPortfolio, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.NORTH);
    }


    // new window popup
    private void createNewPortfolioWindow() {
        JWindow createPortfolioWindow = new JWindow();
        // asks for name of portfolio
        // asks to buy a stock
        JFrame portfolioMenu = new JFrame("Create New Portfolio");
        portfolioMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);


    }


    // leads to the second frame
    private void portfolioChooseMenuWindow() {
        JWindow portfolioWindow = new JWindow();
        JFrame portfolioMenu = new JFrame("Portfolio Menu Screen");
        portfolioMenu.setDefaultCloseOperation(EXIT_ON_CLOSE);

        transactionWindow = new JButton("Buy or Sell Stock(s)");
        findValueWindow = new JButton("Find Value of Portfolio at Date");
        findCompositionWindow = new JButton("Find Composiiton of Portfolio at Date");

        savePortfolio = new JButton("Save This Portfolio");



    }

    // leads to the buy/sell window
    private void buyOrSellWindow() {
        JWindow buySellWindow = new JWindow();
        JFrame buySellWindowFrame = new JFrame("Buy or Sell Stock(s)");
        buySellWindowFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);


        JLabel transactionType = new JLabel("Transaction Type (buy/sell): ");
        JTextField transactionField = new JTextField(1);

        JLabel stockLabel = new JLabel("Stock Ticker: ");
        JTextField stockTickerField = new JTextField(1);

        JLabel yearLabel = new JLabel("Year of Transaction (YYYY): ");
        JTextField yearField = new JTextField(1);

        JLabel monthLabel = new JLabel("Month of Transaction (MM): ");
        JTextField monthField = new JTextField(1);

        JLabel dayLabel = new JLabel("Day of Transaction (DD): ");
        JTextField dayField = new JTextField(1);

        buyOrSellEnter = new JButton("Enter Transaction");



    }

    private void findValueWindow() {
        JWindow valueWindow = new JWindow();
        JFrame valueFrame = new JFrame("Find Value at Date");

        JLabel year = new JLabel("Enter valid Year: ");
        JTextField yearText = new JTextField(1);

        JLabel month = new JLabel("Enter valid Month: ");
        JTextField monthText = new JTextField(1);

        JLabel day = new JLabel("Enter valid Day: ");
        JTextField dayText = new JTextField(1);

        findValueEnter = new JButton("Enter");
    }

    private void findCompositionWindow() {
        JWindow compWindow = new JWindow();
        JFrame compFrame = new JFrame("Find Composition at Date");

        JLabel year = new JLabel("Enter valid Year: ");
        JTextField yearText = new JTextField(1);

        JLabel month = new JLabel("Enter valid Month: ");
        JTextField monthText = new JTextField(1);

        JLabel day = new JLabel("Enter valid Day: ");
        JTextField dayText = new JTextField(1);

        findCompEnter = new JButton("Enter");

    }

    private void loadNewPortfolio() {
        final JFileChooser fileChoser = new JFileChooser("res/data/");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv Files", ".csv");
        fileChoser.setFileFilter(filter);
        int retvalue = fileChoser.showOpenDialog(null);
        if (retvalue == JFileChooser.APPROVE_OPTION) {
            File f = fileChoser.getSelectedFile();
            // TODO: model loads file into list of portfolios, updates portfolio
        }
        loadNewPortfolio.setActionCommand("loadPortfolio");
    }

    // controller should call the listeners below and then use the method from the model
    public void addCreateNewPortfolioListener(ActionListener listenForCreatePButton) {
        createNewPortfolioButton.addActionListener(listenForCreatePButton);
        // should lead to JTextArea to enter portfolio name, then the main portfolio menu
    }

    public void addLoadPortfolioListener(ActionListener listForLoad) {
        loadNewPortfolio.addActionListener(listForLoad);
        // have them load a new portfolio from xml
    }

    public void triggerTransactionWindow(ActionListener listenForTransaction) {
        transactionWindow.addActionListener(listenForTransaction);//add button here
        buyOrSellWindow(); // triggers the buy/sell window
    }

    public void triggerPortfolioMenuWindow(ActionListener listenForMenu) {
        portfolioChooseMenuWindow(); // triggers the portfolio window
    }

    public void buyOrSellEnterButtonListener(ActionListener listenForBuyOrSell) {
        buyOrSellEnter.addActionListener(listenForBuyOrSell);
    }

    //needs its own enter button
    public void triggerValueButtonListenerWindow(ActionListener listenForValue) {
        findValueWindow.addActionListener(listenForValue);
    }
    public void getValueButtonListener(ActionListener listenForValue) {

    }

    //needs its own enter button
    public void triggerCompositionButtonListenerWindow(ActionListener listenForComposition) {
        findCompositionWindow.addActionListener(listenForComposition);
    }

    public void getCompositionButtonListener(ActionListener listenForComp) {

    }

    public void savePortfolioButtonListener(ActionListener listenForSave) {
        savePortfolio.addActionListener(listenForSave);
    }


}