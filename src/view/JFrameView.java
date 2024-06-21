package view;

import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * An abstract JFrame representation of the stock management program.
 * This GUI allows the user to be able to add
 * portfolios, save and load portfolios, calculate the value, composition, and
 */
public abstract class JFrameView extends JFrame {

  /**
   * Constructs an object in a new window with the given name.
   * @param title the title of the window
   */
  public JFrameView(String title) {
    super(title);
  }

  /**
   * Displays the window for saving portfolios, prompting for date input.
   * Handles saving actions with error checking for date validity.
   */
  public abstract void savePortfolioWindow();

  /**
   * Opens a window for creating a new portfolio, allowing user input for the portfolio name.
   * Ensures the name is valid and updates the main view upon successful creation.
   */
  public abstract void createNewPortfolioWindow();

  /**
   * Presents the user with a transaction window for executing buy or sell stock
   * transactions, including transaction details.
   * Validates user inputs and initiates the transaction process.
   */
  public abstract void buyOrSellWindow();

  /**
   * Provides a window for users to find the total value of a portfolio on a specified date.
   * Ensures date validity and displays the calculated value.
   */
  public abstract void findValueWindow();

  /**
   * Allows the user to view the composition of their portfolio at a chosen date.
   * Useful for assessing asset allocation and diversity at specific times.
   */
  public abstract void findCompositionWindow();

  /**
   * Facilitates the loading of a portfolio from file storage using a file chooser dialogue.
   *
   * @return boolean true if a portfolio is successfully loaded.
   */
  public abstract boolean loadNewPortfolio();

  /**
   * Retrieves the name of the currently selected portfolio from the list.
   * Useful for operations that require knowledge of the selected portfolio.
   */
  public abstract String getCurrentPortfolioName();

  /**
   * Adds a new portfolio name to the list in the GUI.
   * Updates the display to reflect the addition of a new portfolio.
   */
  public abstract void addPortfolioToList(String portfolioName);

  /**
   * Registers an ActionListener to handle the save operation when the save button is clicked.
   * Facilitates external control over the save action in the application.
   */
  public abstract void addSavePortfolioEnterListener(ActionListener listenForSave);

  /**
   * Closes the window used for creating a new portfolio.
   * Helps manage the GUI state by disposing of unnecessary windows.
   */
  public abstract void closeCreateNewPortfolioWindow();

  /**
   * Appends a message to the main application's message area for user notifications.
   * Used for both informational messages and operation confirmations.
   */
  public abstract void displayMessage(String message);

  /**
   * Shows an error message dialog box when user inputs are invalid or actions fail.
   * Essential for guiding users to correct errors and improve data entry.
   */
  public abstract void displayErrorMessage(String message);

  /**
   * Extracts and returns the transaction type specified by the user
   * (buy/sell are the only valid options).
   * Critical for determining the nature of stock transactions in the application.
   */
  public abstract String getTransactionType();

  /**
   * Retrieves the stock ticker symbol from the user input for transaction processing.
   * Enables accurate identification and processing of stock transactions.
   */
  public abstract String getStockTicker();

  /**
   * Obtains the year part of the transaction date entered by the user.
   * Essential for chronological organization and validation of stock transactions.
   */
  public abstract String getTransactionYear();

  /**
   * Obtains the month part of the transaction date from user input.
   * Plays a crucial role in date validation and temporal accuracy of transactions.
   */
  public abstract String getTransactionMonth();

  /**
   * Obtains the day component of the transaction date from user input.
   * Helps ensure the precise timing of transactions for accurate record-keeping.
   */
  public abstract String getTransactionDay();

  /**
   * Obtains the number of shares involved in a transaction from user input.
   */
  public abstract String getNumOfShares();

  /**
   * Gets the loaded portfolio the user has chosen from their computer.
   *
   * @return the loaded portfolio as a File
   */
  public abstract File getLoadedPortfolioFile();

  /**
   * Gets the name of the loaded portfolio file, aiding in identification within the application.
   *
   * @return the name of the loaded portfolio as a String.
   */
  public abstract String getLoadedPortfolioName();

  /**
   * Adds an ActionListener to handle user interaction with the 'Create New Portfolio' button.
   * Enables the creation of new portfolios through UI events.
   *
   * @param listenForCreatePButton the ActionListener that responds to the
   *                               'Create New Portfolio' button click
   */
  public abstract void addCreateNewPortfolioListener(ActionListener listenForCreatePButton);

  /**
   * Adds an ActionListener to manage the 'Buy or Sell' operation when triggered by the user.
   * Central to facilitating stock trading actions directly from the interface.
   *
   * @param listenForBuyOrSell the ActionListener that responds to the
   *                           'Buy or Sell' button click
   */
  public abstract void addBuyOrSellEnterListener(ActionListener listenForBuyOrSell);

  /**
   * Registers an ActionListener to the 'Find Value' button to compute and
   * display portfolio values.
   * Enhances user capability to assess portfolio performance on specified dates.
   *
   * @param listenForValue the ActionListener that responds to the 'Find Value' button click
   */
  public abstract void addFindValueEnterListener(ActionListener listenForValue);

  /**
   * Connects an ActionListener to the 'Find Composition' button to detail portfolio composition.
   * Assists users in understanding their investment distribution on specific dates.
   *
   * @param listenForComp the ActionListener that responds to the 'Find Composition' button click
   */
  public abstract void addFindCompEnterListener(ActionListener listenForComp);

  /**
   * Adds an ActionListener to the 'Load Portfolio' button, facilitating the loading of
   * portfolio files.
   * Essential for switching between different portfolio contexts in the application.
   *
   * @param listenForLoad the ActionListener that responds to the 'Load Portfolio' button click
   */
  public abstract void addLoadPortfolioListener(ActionListener listenForLoad);

  /**
   * Adds an ActionListener to the 'Create Portfolio' confirmation button, which
   * triggers the actual creation of a new portfolio.
   * This method allows for the integration of business logic upon confirming
   * the creation of a portfolio.
   *
   * @param listener the ActionListener that responds to the
   *                 'Create Portfolio' confirmation button click
   */
  public abstract void addCreateNewPortfolioConfirmListener(ActionListener listener);
}