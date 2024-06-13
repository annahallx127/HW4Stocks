
import controller.ControllerImpl;
import mocks.MockModel;
import mocks.MockStock;
import model.Portfolio;
import org.junit.Before;
import org.junit.Test;
import view.View;

import java.io.StringWriter;
import java.util.Scanner;

import mocks.MockView;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ControllerTest {
  private MockModel model;
  private View view;
  private StringWriter output;
  private ControllerImpl controller;

  @Before
  public void setUp() {
    model = new MockModel();
    output = new StringWriter();
    view = new MockView(output);
    controller = new ControllerImpl(model, view, new Scanner(System.in), System.out);
    MockStock stock = new MockStock("AAPL");
    stock.setPriceOnDate("2021-01-01", 150.0);
    stock.setPriceOnDate("2021-01-31", 160.0);
    model.addMockStock("AAPL", stock);
  }

  private void simulateInput(String input) {
    Scanner scanner = new Scanner(input);
    controller = new ControllerImpl(model, view, scanner, output);
    controller.runController(controller, view, scanner);
  }

  @Test
  public void testCreatePortfolioAndAddStock() {
    simulateInput("4\nMyPortfolio\nAAPL\n10\nno\n6\n");


    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertTrue("Portfolio should contain AAPL stock", portfolio.getStocks().containsKey(model.get("AAPL")));
    assertEquals("Should have 10 shares of AAPL", 10.0, portfolio.getStocks().get(model.get("AAPL")), 0.01);
    assertTrue("Output should confirm stock addition", output.toString().contains("You have added 10 shares of AAPL to your portfolio!"));
  }

  @Test
  public void testCalculateGainOrLoss() {
    simulateInput("1\nAAPL\n2021-01-01\n2021-01-31\n6\n");
    assertEquals("Gain should be calculated correctly", 10.0, model.get("AAPL").gainedValue("2021-01-01", "2021-01-31"), 0.01);
  }

  @Test
  public void testViewPortfolio() {
    simulateInput("5\n1\n6\n");
    model.makePortfolio("MyPortfolio");
    assertEquals("The portfolio is empty!", model.getPortfolios().get("MyPortfolio").toString());
  }

  @Test
  public void testCalculateXDayMovingAverage() {
    simulateInput("2\nAAPL\n10\n2021-01-01\n6\n");
    assertEquals("Moving average should be calculated correctly", 155.0, model.get("AAPL").getMovingAverage(10, "2021-01-01"), 0.01);
  }

  @Test
  public void testInvalidTickerSymbolForGainOrLoss() {
    simulateInput("1\nINVALID\n2021-01-01\n2021-01-31\n6\n");
    assertTrue("Should handle invalid ticker symbol", output.toString().contains("Invalid ticker symbol"));
  }


  @Test
  public void testInvalidTickerSymbolForGainOrLossDisplay() {
    simulateInput("1\nINVALID\n2021-01-01\n2021-01-31\n7\n");
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

}
