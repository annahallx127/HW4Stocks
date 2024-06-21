package view;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * The JFrameView class is the graphical user interface for the Stock application,
 * extending JFrame. It manages all user interactions for creating and managing
 * investment portfolios, including operations like buying and selling stocks,
 * and viewing portfolio value and composition on specific dates.
 * This class utilizes various Swing components to facilitate user input and display,
 * and integrates action listeners to connect UI events with backend processing.
 * It is designed to provide an intuitive and efficient user experience for portfolio
 * management within the application.
 */
public class JFrameViewImpl extends JFrameView {
  private JFrame createNewPortfolioFrame;
  private JFrame buySellWindowFrame;
  private JFrame findValueFrame;
  private JFrame findCompFrame;
  private JFrame saveFrame;
  private JButton createNewPortfolioButton;
  private JButton createConfirmButton;
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
  private String currentPortfolioName;

  /**
   * Constructs an instance of JFrameView, setting basic properties like title, size, and
   * visibility.
   * Initializes all the UI components necessary for the main menu view. This constructor serves as
   * the entry point for setting up the GUI framework of the application.
   * It is responsible for initializing components, setting up the layout, and making the initial
   * frame visible to the user.
   */
  public JFrameViewImpl() {
    super("I&A's Stock Investment Company");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(800, 500);
    setLocationRelativeTo(null);
    initializeComponents();
    createMainMenuView();
    setVisible(true);
  }

  //Initializes the buttons and sets the action commands for each button at the start
  private void initializeComponents() {
    createNewPortfolioButton = new JButton("Create New Portfolio");
    createNewPortfolioButton.setActionCommand("createNewPortfolio");

    createConfirmButton = new JButton("Create Portfolio");
    createConfirmButton.setActionCommand("createPortfolioConfirm");
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
        String selectedValue = portfolioList.getSelectedValue();
        if (selectedValue != null) {
          showPortfolioMenu(selectedValue);
          currentPortfolioName = portfolioList.getSelectedValue().trim();
          portfolioList.clearSelection();
        }
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
    if (portfolioMenu == null || !portfolioMenu.getTitle()
            .equals("Portfolio Menu - " + portfolioName)) {
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
    } else {
      portfolioMenu.setVisible(true);
    }
  }

  @Override
  public void savePortfolioWindow() {
    if (saveFrame == null) {
      saveFrame = new JFrame("Save Portfolio");
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

      saveFrame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
          saveFrame = null;
        }
      });

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
    saveFrame.setVisible(true);
  }


  @Override
  public void createNewPortfolioWindow() {
    if (createNewPortfolioFrame == null) {
      createNewPortfolioFrame = new JFrame("Create New Portfolio");
      createNewPortfolioFrame.setSize(400, 300);
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(5, 5, 5, 5);

      JLabel nameLabel = new JLabel("Enter Portfolio Name:");
      JTextField nameField = new JTextField(15);
      panel.add(nameLabel, gbc);
      panel.add(nameField, gbc);

      createConfirmButton.setPreferredSize(new Dimension(150, 25));
      panel.add(createConfirmButton, gbc);

      createConfirmButton.addActionListener(e -> {
        currentPortfolioName = nameField.getText().trim();
        if (!currentPortfolioName.isEmpty()) {
          displayMessage("Portfolio " + currentPortfolioName + " created successfully.");
          addPortfolioToList(currentPortfolioName);
          closeCreateNewPortfolioWindow();
        } else {
          displayErrorMessage("Portfolio name cannot be empty.");
        }
      });

      createNewPortfolioFrame.add(panel);
      createNewPortfolioFrame.pack();
      createNewPortfolioFrame.setLocationRelativeTo(null);

      createNewPortfolioFrame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
          createNewPortfolioFrame = null;
        }
      });
    }
    createNewPortfolioFrame.setVisible(true);
  }

  @Override
  public void buyOrSellWindow() {
    if (buySellWindowFrame == null) {
      buySellWindowFrame = new JFrame("Buy or Sell Stock(s)");
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

      buySellWindowFrame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
          buySellWindowFrame = null;
        }
      });

      loadAvailableStockSymbols("res/data/listing_status.csv");
    }
    buySellWindowFrame.setVisible(true);
  }

  @Override
  public void findValueWindow() {
    if (findValueFrame == null) {
      findValueFrame = new JFrame("Find Value at Date");
      findValueFrame.setSize(400, 300);

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

      findValueFrame.add(panel);
      findValueFrame.pack();
      findValueFrame.setLocationRelativeTo(null);

      findValueFrame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
          findValueFrame = null;
        }
      });
    }
    findValueFrame.setVisible(true);
  }

  @Override
  public void findCompositionWindow() {
    if (findCompFrame == null) {
      findCompFrame = new JFrame("Find Composition at Date");
      findCompFrame.setSize(400, 300);

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

      findCompFrame.add(panel);
      findCompFrame.pack();
      findCompFrame.setLocationRelativeTo(null);

      findCompFrame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
          findCompFrame = null;
        }
      });
    }
    findCompFrame.setVisible(true);
  }

  @Override
  public boolean loadNewPortfolio() {
    final JFileChooser fileChooser = new JFileChooser(Path.of("res/data/portfolios")
            .toAbsolutePath().toString());
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            ".xml Files", "xml");
    fileChooser.setFileFilter(filter);
    fileChooser.setApproveButtonText("Open");
    int retvalue = fileChooser.showOpenDialog(null);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      loadedPortfolioFile = fileChooser.getSelectedFile();
      addPortfolioToList(loadedPortfolioFile.getName());
      return true;
    }
    return false;
  }

  @Override
  public String getCurrentPortfolioName() {

    return currentPortfolioName;
  }

  @Override
  public void addPortfolioToList(String portfolioName) {
    portfolioListModel
            .addElement(portfolioName.trim());
  }

  @Override
  public void addSavePortfolioEnterListener(ActionListener listenForSave) {
    saveEnter.addActionListener(listenForSave);
  }

  @Override
  public void closeCreateNewPortfolioWindow() {
    if (createNewPortfolioFrame != null) {
      createNewPortfolioFrame.dispose();
      createNewPortfolioFrame = null;
    }
  }

  @Override
  public void displayMessage(String message) {
    messageArea.append(message + System.lineSeparator());
  }

  @Override
  public void displayErrorMessage(String message) {

    JOptionPane.showMessageDialog(
            this, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public String getTransactionType() {
    return transactionTypeField.getText();
  }

  @Override
  public String getStockTicker() {
    return stockTickerField.getText();
  }

  @Override
  public String getTransactionYear() {
    return yearField.getText();
  }

  @Override
  public String getTransactionMonth() {
    return monthField.getText();
  }

  @Override
  public String getTransactionDay() {
    return dayField.getText();
  }

  @Override
  public String getNumOfShares() {
    return numField.getText();
  }

  @Override
  public File getLoadedPortfolioFile() {
    return loadedPortfolioFile;
  }

  @Override
  public String getLoadedPortfolioName() {
    return loadedPortfolioFile.getName();
  }

  @Override
  public void addCreateNewPortfolioListener(ActionListener listenForCreatePButton) {
    createNewPortfolioButton.addActionListener(listenForCreatePButton);
  }

  @Override
  public void addBuyOrSellEnterListener(ActionListener listenForBuyOrSell) {
    buyOrSellEnter.addActionListener(listenForBuyOrSell);
  }

  @Override
  public void addFindValueEnterListener(ActionListener listenForValue) {
    findValueEnter.addActionListener(listenForValue);
  }

  @Override
  public void addFindCompEnterListener(ActionListener listenForComp) {
    findCompEnter.addActionListener(listenForComp);
  }

  @Override
  public void addLoadPortfolioListener(ActionListener listenForLoad) {
    loadNewPortfolio.addActionListener(listenForLoad);
  }

  @Override
  public void addCreateNewPortfolioConfirmListener(ActionListener listener) {
    createConfirmButton.addActionListener(listener);
  }

  // display the available stock symbols for the sake of a better looking/more user-friendly ui
  private Set<String> parseValidSymbols(String filePath) {

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

}
