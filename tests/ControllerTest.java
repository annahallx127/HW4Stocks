import controller.ControllerImpl;
import mocks.MockModel;
import mocks.MockStock;

import model.Portfolio;

import org.junit.Before;
import org.junit.Test;

import view.View;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Scanner;

import mocks.MockView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the ControllerImpl class, which handles the interaction between the user,
 * the model, and the view. This class tests various functionalities such as creating a portfolio,
 * adding stocks, calculating gain/loss, viewing portfolios, calculating moving averages,
 * re-balancing portfolios, and handling invalid inputs. These tests implement MockModel and Mock
 * View so that it a simulate a real life user interaction without needing a user.
 */
public class ControllerTest {
  private MockModel model;
  private View view;
  private StringWriter output;
  private ControllerImpl controller;

  /**
   * Sets up the test environment before each test. Initializes the mock model, view,
   * controller, and adds a mock stock to the model.
   */
  @Before
  public void setUp() {
    model = new MockModel();
    output = new StringWriter();
    view = new MockView(output);
    controller = new ControllerImpl(model, view);
    MockStock stock = new MockStock("AAPL");
    stock.setPriceOnDate("2021-01-01", 150.0);
    stock.setPriceOnDate("2021-01-31", 160.0);
    model.addMockStock("AAPL", stock);
  }

  private void simulateInput(String input) {
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    controller = new ControllerImpl(model, view);
    controller.runController(controller, view, new Scanner(System.in));
  }

  @Test
  public void testCreatePortfolioAndAddStock() {
    String input = "4\nblah\nAAPL\n2021\n01\n01\n9\nno\n7\n";
    simulateInput(input);

    Portfolio portfolio = model.getPortfolios().get("blah");
    assertTrue("Portfolio should contain AAPL stock",
            portfolio.getStocks().containsKey(model.get("AAPL")));
    assertEquals("Should have 9 shares of AAPL", 9.0,
            portfolio.getStocks().get(model.get("AAPL")), 0.01);
    assertTrue("Output should confirm stock addition",
            output.toString().contains("You have added 9 shares of AAPL to your portfolio!"));
  }

  @Test
  public void testCalculateGainOrLoss() {
    String input = "1\nAAPL\n2021\n01\n01\n2021\n01\n31\n7\n";
    simulateInput(input);
    assertEquals("Gain should be calculated correctly",
            10.0, model.get("AAPL").gainedValue("2021-01-01", "2021-01-31"), 0.01);
  }

  @Test
  public void testViewPortfolio() {
    model.makePortfolio("MyPortfolio");
    String input = "5\n1\n9\n7";
    simulateInput(input);
    assertEquals("The portfolio is empty!",
            model.getPortfolios().get("MyPortfolio").toString());
  }

  @Test
  public void testCalculateXDayMovingAverage() {
    String input = "2\nAAPL\n9\n2021\n01\n01\n7\n";
    simulateInput(input);
    assertEquals("Moving average should be calculated correctly",
            155.0, model.get("AAPL").getMovingAverage(9, "2021-01-01"),
            0.01);
  }

  @Test
  public void testInvalidTickerSymbolForGainOrLoss() {
    String input = "1\nINVALID\n2021\n01\n01\n2021\n01\n31\n7\n";
    simulateInput(input);
    assertTrue("Should handle invalid ticker symbol",
            output.toString().contains("Invalid ticker symbol"));
  }

  @Test
  public void testInvalidTickerSymbolForGainOrLossDisplay() {
    String input = "1\nINVALID\n7\n";
    simulateInput(input);
    String expectedOutput = "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. Portfolio Menu Screen\n" +
            "6. Load Own Portfolio\n" +
            "7. Exit Menu\n" +
            "Choose an option:\n" +
            "Enter ticker symbol: \n" +
            "Invalid ticker symbol.\n" +
            "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. Portfolio Menu Screen\n" +
            "6. Load Own Portfolio\n" +
            "7. Exit Menu\n" +
            "Choose an option:\n" +
            "Exiting...\n";
    assertEquals(expectedOutput, output.toString());
  }

  @Test
  public void testReBalancePortfolio() {
    String input = "4\nergerg\nAAPL\n2021\n01\n01\n5\n1\n5\n2021\n01\n01\nAAPL\n90\n7\n";
    simulateInput(input);
    assertTrue("Output should confirm rebalancing",
            output.toString().contains("Re-balanced portfolio"));
  }

  @Test
  public void testGetValueDistribution() {
    model.makePortfolio("MyPortfolio");
    String input = "5\n1\n7\n";
    simulateInput(input);
    assertTrue("Output should show distribution of value",
            output.toString().contains("Distribution of value"));
  }

  @Test
  public void testGetComposition() {
    model.makePortfolio("MyPortfolio");
    String input = "5\n1\n4\n2021\n01\n01\n7\n";
    simulateInput(input);
    assertTrue("Output should show composition of portfolio",
            output.toString().contains("Composition of portfolio"));
  }

  @Test
  public void testInvalidInputForReBalance() {
    String input = "5\n1\n5\n2021\n01\n01\nINVALID\n90\n7\n";
    simulateInput(input);
    assertTrue("Should handle invalid ticker symbol for rebalancing",
            output.toString().contains("Invalid ticker symbol"));
  }

  @Test
  public void testInvalidInputForValueDistribution() {
    model.makePortfolio("MyPortfolio");
    String input = "5\n1\n7\n2021\n01\n01\n9\n7\n";
    simulateInput(input);
    assertTrue("Should handle invalid input for value distribution",
            output.toString().contains("Invalid date"));
  }

  @Test
  public void testInvalidInputForComposition() {
    model.makePortfolio("MyPortfolio");
    String input = "4\nblah\nAAPL\n2021\n01\n01\n6\n5\n1\n4\nINVALID\nryh\nrth\n9\n";
    simulateInput(input);
    assertTrue("Should handle invalid input for composition",
            output.toString().contains("Invalid date"));
  }
}
