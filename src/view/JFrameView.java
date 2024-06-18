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
        getContentPane().setLayout(new BorderLayout());

        // Panel for the list of portfolios
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel availablePortfoliosLabel = new JLabel("Available Portfolios");
        availablePortfoliosLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(availablePortfoliosLabel, BorderLayout.NORTH);

        JList<String> portfolioList = new JList<>();
        mainPanel.add(new JScrollPane(portfolioList), BorderLayout.CENTER);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        // Panel for the buttons at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        createNewPortfolioButton = new JButton("Create New Portfolio");
        loadNewPortfolio = new JButton("Load Portfolio");

        bottomPanel.add(createNewPortfolioButton);
        bottomPanel.add(loadNewPortfolio);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }



    private void createNewPortfolioWindow() {
        JFrame portfolioMenu = new JFrame("Create New Portfolio");
        portfolioMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        portfolioMenu.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Enter Portfolio Name:");
        JTextField nameField = new JTextField(15);
        panel.add(nameLabel, gbc);
        panel.add(nameField, gbc);

        JButton createButton = new JButton("Create Portfolio");
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createButton, gbc);

        JButton buyStockButton = new JButton("Buy Stock");
        panel.add(buyStockButton, gbc);

        portfolioMenu.add(panel);
        portfolioMenu.pack();
        portfolioMenu.setLocationRelativeTo(null);
        portfolioMenu.setVisible(true);
    }



    // leads to the second frame
    private void portfolioChooseMenuWindow() {
        JFrame portfolioMenu = new JFrame("Portfolio Menu Screen");
        portfolioMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        portfolioMenu.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        transactionWindow = new JButton("Buy or Sell Stock(s)");
        findValueWindow = new JButton("Find Value of Portfolio at Date");
        findCompositionWindow = new JButton("Find Composition of Portfolio at Date");
        savePortfolio = new JButton("Save This Portfolio");

        panel.add(transactionWindow, gbc);
        panel.add(findValueWindow, gbc);
        panel.add(findCompositionWindow, gbc);
        panel.add(savePortfolio, gbc);

        portfolioMenu.add(panel);
        portfolioMenu.pack();
        portfolioMenu.setLocationRelativeTo(null);
        portfolioMenu.setVisible(true);
    }


    // leads to the buy/sell window
    private void buyOrSellWindow() {
        JFrame buySellWindowFrame = new JFrame("Buy or Sell Stock(s)");
        buySellWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buySellWindowFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel transactionType = new JLabel("Transaction Type (buy/sell): ");
        JTextField transactionField = new JTextField(10);
        panel.add(transactionType, gbc);
        panel.add(transactionField, gbc);

        JLabel stockLabel = new JLabel("Stock Ticker: ");
        JTextField stockTickerField = new JTextField(10);
        panel.add(stockLabel, gbc);
        panel.add(stockTickerField, gbc);

        JLabel yearLabel = new JLabel("Year of Transaction (YYYY): ");
        JTextField yearField = new JTextField(10);
        panel.add(yearLabel, gbc);
        panel.add(yearField, gbc);

        JLabel monthLabel = new JLabel("Month of Transaction (MM): ");
        JTextField monthField = new JTextField(10);
        panel.add(monthLabel, gbc);
        panel.add(monthField, gbc);

        JLabel dayLabel = new JLabel("Day of Transaction (DD): ");
        JTextField dayField = new JTextField(10);
        panel.add(dayLabel, gbc);
        panel.add(dayField, gbc);

        JButton buyOrSellEnter = new JButton("Enter Transaction");
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buyOrSellEnter, gbc);

        buySellWindowFrame.add(panel);
        buySellWindowFrame.pack();
        buySellWindowFrame.setLocationRelativeTo(null);
        buySellWindowFrame.setVisible(true);
    }


    private void findValueWindow() {
        JFrame valueFrame = new JFrame("Find Value at Date");
        valueFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        valueFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel year = new JLabel("Enter valid Year (YYYY): ");
        JTextField yearText = new JTextField(10);
        panel.add(year, gbc);
        panel.add(yearText, gbc);

        JLabel month = new JLabel("Enter valid Month (MM): ");
        JTextField monthText = new JTextField(10);
        panel.add(month, gbc);
        panel.add(monthText, gbc);

        JLabel day = new JLabel("Enter valid Day (DD): ");
        JTextField dayText = new JTextField(10);
        panel.add(day, gbc);
        panel.add(dayText, gbc);

        findValueEnter = new JButton("Enter");
        gbc.fill = GridBagConstraints.NONE;
        panel.add(findValueEnter, gbc);

        valueFrame.add(panel);
        valueFrame.pack();
        valueFrame.setLocationRelativeTo(null);
        valueFrame.setVisible(true);
    }


    private void findCompositionWindow() {
        JFrame compFrame = new JFrame("Find Composition at Date");
        compFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        compFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel year = new JLabel("Enter valid Year (YYYY): ");
        JTextField yearText = new JTextField(10);
        panel.add(year, gbc);
        panel.add(yearText, gbc);

        JLabel month = new JLabel("Enter valid Month (MM): ");
        JTextField monthText = new JTextField(10);
        panel.add(month, gbc);
        panel.add(monthText, gbc);

        JLabel day = new JLabel("Enter valid Day (DD): ");
        JTextField dayText = new JTextField(10);
        panel.add(day, gbc);
        panel.add(dayText, gbc);

        findCompEnter = new JButton("Enter");
        gbc.fill = GridBagConstraints.NONE;
        panel.add(findCompEnter, gbc);

        compFrame.add(panel);
        compFrame.pack();
        compFrame.setLocationRelativeTo(null);
        compFrame.setVisible(true);
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
        findCompEnter.addActionListener(listenForValue);
    }

    //needs its own enter button
    public void triggerCompositionButtonListenerWindow(ActionListener listenForComposition) {
        findCompositionWindow.addActionListener(listenForComposition);
    }

    public void getCompositionButtonListener(ActionListener listenForComp) {
        findCompEnter.addActionListener(listenForComp);
    }

    public void savePortfolioButtonListener(ActionListener listenForSave) {
        savePortfolio.addActionListener(listenForSave);
    }

// for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JFrameView();
            }
        });
    }


}