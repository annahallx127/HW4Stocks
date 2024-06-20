package view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class JFrameView extends JFrame {

  private JButton createNewPortfolioButton;
  private JButton loadNewPortfolio;
  private JButton buyOrSellEnter;
  private JButton findValueEnter;
  private JButton findCompEnter;
  private JList<String> portfolioList;
  private DefaultListModel<String> portfolioListModel;
  private JTextField transactionTypeField;
  private JTextField stockTickerField;
  private JTextField yearField;
  private JButton saveEnter;
  private JTextField monthField;
  private JTextField dayField;
  private JTextField numField;
  private JFrame portfolioMenu;
  private File loadedPortfolioFile;
  private JTextArea messageArea;
  private JList<String> availableStocksList;
  private DefaultListModel<String> availableStocksListModel;

  public JFrameView() {
    super("I&A's Stock Investment Company");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(600, 500);
    setLocationRelativeTo(null);
    initializeComponents();
    createMainMenuView();
    setVisible(true);
  }

  private void initializeComponents() {

    createNewPortfolioButton = new JButton("Create New Portfolio");
    createNewPortfolioButton.setActionCommand("createNewPortfolio");

    loadNewPortfolio = new JButton("Load Portfolio");
    loadNewPortfolio.setActionCommand("loadPortfolio");

    buyOrSellEnter = new JButton("Enter Transaction");
    buyOrSellEnter.setActionCommand("enterTransaction");

    findValueEnter = new JButton("Enter");
    findValueEnter.setActionCommand("findValueEnter");

    findCompEnter = new JButton("Enter");
    findCompEnter.setActionCommand("findCompositionEnter");

    saveEnter = new JButton("Save Portfolio");
    saveEnter.setActionCommand("savePortfolioEnter");

    portfolioListModel = new DefaultListModel<>();
    portfolioList = new JList<>(portfolioListModel);
    portfolioList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    availableStocksListModel = new DefaultListModel<>();
    availableStocksList = new JList<>(availableStocksListModel);
    availableStocksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    messageArea = new JTextArea(20, 30);
    messageArea.setEditable(false);
  }

  private void createMainMenuView() {
    getContentPane().setLayout(new BorderLayout());

    portfolioList.addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        showPortfolioMenu(portfolioList.getSelectedValue());
      }
    });

    JPanel mainPanel = new JPanel(new BorderLayout());
    JLabel availablePortfoliosLabel = new JLabel("Available Portfolios");
    availablePortfoliosLabel.setHorizontalAlignment(SwingConstants.CENTER);
    mainPanel.add(availablePortfoliosLabel, BorderLayout.NORTH);
    mainPanel.add(new JScrollPane(portfolioList), BorderLayout.CENTER);

    JPanel bottomLeftPanel = new JPanel(new GridLayout(2, 1));
    bottomLeftPanel.add(createNewPortfolioButton);
    bottomLeftPanel.add(loadNewPortfolio);

    mainPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
    mainPanel.setPreferredSize(new Dimension(320, getHeight()));

    getContentPane().add(mainPanel, BorderLayout.WEST);

    JScrollPane scrollPane = new JScrollPane(messageArea);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.add(scrollPane, BorderLayout.CENTER);

    JLabel calculationsLabel = new JLabel("Update Feed");
    calculationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
    rightPanel.add(calculationsLabel, BorderLayout.NORTH);

    getContentPane().add(rightPanel, BorderLayout.CENTER);
  }

  private void showPortfolioMenu(String portfolioName) {
    if (portfolioMenu != null) {
      portfolioMenu.dispose();
    }
    portfolioMenu = new JFrame("Portfolio Menu - " + portfolioName);
    portfolioMenu.setSize(400, 300);
    portfolioMenu.setLocationRelativeTo(this);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel nameLabel = new JLabel("Portfolio: " + portfolioName);
    panel.add(nameLabel, gbc);

    JButton transactionButton = new JButton("Buy or Sell Stock");
    transactionButton.setActionCommand("buyOrSell");
    transactionButton.addActionListener(e -> buyOrSellWindow());
    panel.add(transactionButton, gbc);

    JButton findValueButton = new JButton("Find Value");
    findValueButton.setActionCommand("findValue");
    findValueButton.addActionListener(e -> findValueWindow());
    panel.add(findValueButton, gbc);

    JButton findCompositionButton = new JButton("Find Composition");
    findCompositionButton.setActionCommand("findComposition");
    findCompositionButton.addActionListener(e -> findCompositionWindow());
    panel.add(findCompositionButton, gbc);

    JButton saveButton = new JButton("Save Portfolio");
    saveButton.setActionCommand("savePortfolio");
    saveButton.addActionListener(e -> savePortfolioWindow());
    panel.add(saveButton, gbc);

    portfolioMenu.add(panel);
    portfolioMenu.pack();
    portfolioMenu.setVisible(true);
  }

  public void savePortfolioWindow() {
    JFrame saveFrame = new JFrame("Save Portfolio");
    saveFrame.setSize(400, 300);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel yearLabel = new JLabel("Enter Year (YYYY): ");
    yearField = new JTextField(10);
    panel.add(yearLabel, gbc);
    panel.add(yearField, gbc);

    JLabel monthLabel = new JLabel("Enter Month (MM): ");
    monthField = new JTextField(10);
    panel.add(monthLabel, gbc);
    panel.add(monthField, gbc);

    JLabel dayLabel = new JLabel("Enter Day (DD): ");
    dayField = new JTextField(10);
    panel.add(dayLabel, gbc);
    panel.add(dayField, gbc);

    gbc.fill = GridBagConstraints.NONE;
    panel.add(saveEnter, gbc);

    saveFrame.add(panel);
    saveFrame.pack();
    saveFrame.setLocationRelativeTo(null);
    saveFrame.setVisible(true);

    saveEnter.addActionListener(e -> {
      String year = yearField.getText().trim();
      String month = monthField.getText().trim();
      String day = dayField.getText().trim();
      if (!year.isEmpty() && !month.isEmpty() && !day.isEmpty()) {
        displayMessage("Portfolio saved with date: " + year + "-" + month + "-" + day);
        saveFrame.dispose();
      } else {
        displayErrorMessage("Please enter a valid date.");
      }
    });
  }

  public void createNewPortfolioWindow() {
    portfolioMenu = new JFrame("Create New Portfolio");
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
    createButton.setPreferredSize(new Dimension(150, 25));
    createButton.setActionCommand("createPortfolioConfirm");
    panel.add(createButton, gbc);

    createButton.addActionListener(e -> {
      String portfolioName = nameField.getText().trim();
      if (!portfolioName.isEmpty()) {
        addPortfolioToList(portfolioName);
        displayMessage("Portfolio created: " + portfolioName);
        closePortfolioMenu();
      }
    });

    portfolioMenu.add(panel);
    portfolioMenu.pack();
    portfolioMenu.setLocationRelativeTo(null);
    portfolioMenu.setVisible(true);
  }

  public void buyOrSellWindow() {
    JFrame buySellWindowFrame = new JFrame("Buy or Sell Stock(s)");
    buySellWindowFrame.setSize(500, 350);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel transactionType = new JLabel("Transaction Type (buy/sell): ");
    transactionTypeField = new JTextField(10);
    panel.add(transactionType, gbc);
    panel.add(transactionTypeField, gbc);

    JLabel stockLabel = new JLabel("Stock Ticker: ");
    stockTickerField = new JTextField(10);
    panel.add(stockLabel, gbc);
    panel.add(stockTickerField, gbc);

    JLabel yearLabel = new JLabel("Year of Transaction (YYYY): ");
    yearField = new JTextField(10);
    panel.add(yearLabel, gbc);
    panel.add(yearField, gbc);

    JLabel monthLabel = new JLabel("Month of Transaction (MM): ");
    monthField = new JTextField(10);
    panel.add(monthLabel, gbc);
    panel.add(monthField, gbc);

    JLabel dayLabel = new JLabel("Day of Transaction (DD): ");
    dayField = new JTextField(10);
    panel.add(dayLabel, gbc);
    panel.add(dayField, gbc);

    JLabel numShares = new JLabel("Number of shares to buy/sell: ");
    numField = new JTextField(10);
    panel.add(numShares, gbc);
    panel.add(numField, gbc);

    gbc.fill = GridBagConstraints.NONE;
    panel.add(buyOrSellEnter, gbc);

    JLabel availableStocksLabel = new JLabel("List of Available Stocks:");
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(availableStocksLabel, gbc);

    JScrollPane scrollPane = new JScrollPane(availableStocksList);
    scrollPane.setPreferredSize(new Dimension(480, 100));
    panel.add(scrollPane, gbc);

    buySellWindowFrame.add(panel);
    buySellWindowFrame.pack();
    buySellWindowFrame.setLocationRelativeTo(null);
    buySellWindowFrame.setVisible(true);

    loadAvailableStockSymbols("res/data/listing_status.csv");
  }

  public void findValueWindow() {
    JFrame valueFrame = new JFrame("Find Value at Date");
    valueFrame.setSize(400, 300);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel year = new JLabel("Enter valid Year (YYYY): ");
    yearField = new JTextField(10);
    panel.add(year, gbc);
    panel.add(yearField, gbc);

    JLabel month = new JLabel("Enter valid Month (MM): ");
    monthField = new JTextField(10);
    panel.add(month, gbc);
    panel.add(monthField, gbc);

    JLabel day = new JLabel("Enter valid Day (DD): ");
    dayField = new JTextField(10);
    panel.add(day, gbc);
    panel.add(dayField, gbc);

    gbc.fill = GridBagConstraints.NONE;
    panel.add(findValueEnter, gbc);

    valueFrame.add(panel);
    valueFrame.pack();
    valueFrame.setLocationRelativeTo(null);
    valueFrame.setVisible(true);
  }

  public void findCompositionWindow() {
    JFrame compFrame = new JFrame("Find Composition at Date");
    compFrame.setSize(400, 300);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel year = new JLabel("Enter valid Year (YYYY): ");
    yearField = new JTextField(10);
    panel.add(year, gbc);
    panel.add(yearField, gbc);

    JLabel month = new JLabel("Enter valid Month (MM): ");
    monthField = new JTextField(10);
    panel.add(month, gbc);
    panel.add(monthField, gbc);

    JLabel day = new JLabel("Enter valid Day (DD): ");
    dayField = new JTextField(10);
    panel.add(day, gbc);
    panel.add(dayField, gbc);

    gbc.fill = GridBagConstraints.NONE;
    panel.add(findCompEnter, gbc);

    compFrame.add(panel);
    compFrame.pack();
    compFrame.setLocationRelativeTo(null);
    compFrame.setVisible(true);
  }

  public boolean loadNewPortfolio() {
    final JFileChooser fileChooser = new JFileChooser(Path.of("res/data/portfolios").toAbsolutePath().toString());
    FileNameExtensionFilter filter = new FileNameExtensionFilter(".xml Files", "xml");
    fileChooser.setFileFilter(filter);
    fileChooser.setApproveButtonText("Open");
    int retvalue = fileChooser.showOpenDialog(null);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      loadedPortfolioFile = fileChooser.getSelectedFile();
      return true;
    }
    return false;
  }

  public String getPortfolioName() {
    return ((JTextField) ((JPanel) portfolioMenu.getContentPane().getComponent(0)).getComponent(1)).getText().trim();
  }

  public void addPortfolioToList(String portfolioName) {
    portfolioListModel.addElement(portfolioName);
  }


  public void addSavePortfolioEnterListener(ActionListener listenForSave) {
    saveEnter.addActionListener(listenForSave);
  }

  public void closePortfolioMenu() {
    portfolioMenu.dispose();
  }

  public void displayMessage(String message) {
    messageArea.append(message + "\n");
  }

  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public String getTransactionType() {
    return transactionTypeField.getText();
  }

  public String getStockTicker() {
    return stockTickerField.getText();
  }

  public String getTransactionYear() {
    return yearField.getText();
  }

  public String getTransactionMonth() {
    return monthField.getText();
  }

  public String getTransactionDay() {
    return dayField.getText();
  }

  public String getNumOfShares() {
    return numField.getText();
  }

  public File getLoadedPortfolioFile() {
    return loadedPortfolioFile;
  }

  public String getLoadedPortfolioName() {
    return loadedPortfolioFile.getName();
  }

  public void addCreateNewPortfolioListener(ActionListener listenForCreatePButton) {
    createNewPortfolioButton.addActionListener(listenForCreatePButton);
  }

  public void addBuyOrSellEnterListener(ActionListener listenForBuyOrSell) {
    buyOrSellEnter.addActionListener(listenForBuyOrSell);
  }

  public void addFindValueEnterListener(ActionListener listenForValue) {
    findValueEnter.addActionListener(listenForValue);
  }

  public void addFindCompEnterListener(ActionListener listenForComp) {
    findCompEnter.addActionListener(listenForComp);
  }

  public void addLoadPortfolioListener(ActionListener listenForLoad) {
    loadNewPortfolio.addActionListener(listenForLoad);
  }

  // display the available stock symbols for the sake of a better looking/more user-friendly ui
  public Set<String> parseValidSymbols(String filePath) {
    Set<String> validSymbols = new HashSet<>();
    String line;

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      line = br.readLine();

      while ((line = br.readLine()) != null) {
        String[] columns = line.split(",");
        String symbol = columns[0].trim();
        if (!symbol.isEmpty()) {
          validSymbols.add(symbol);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return validSymbols;
  }

  private void loadAvailableStockSymbols(String filePath) {
    Set<String> validSymbols = parseValidSymbols(filePath);
    availableStocksListModel.clear();
    for (String symbol : validSymbols) {
      availableStocksListModel.addElement(symbol);
    }
  }

  // Testing
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrameView frameView = new JFrameView();
      frameView.addPortfolioToList("Sample Portfolio 1");
      frameView.addPortfolioToList("Sample Portfolio 2");

      frameView.addCreateNewPortfolioListener(e -> frameView.createNewPortfolioWindow());
      frameView.addBuyOrSellEnterListener(e -> frameView.displayMessage("Transaction entered."));
      frameView.addFindValueEnterListener(e -> frameView.displayMessage("Value found."));
      frameView.addFindCompEnterListener(e -> frameView.displayMessage("Composition found."));
      frameView.addLoadPortfolioListener(e -> frameView.loadNewPortfolio());
    });
  }
}
