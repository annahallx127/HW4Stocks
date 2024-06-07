import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import mocks.MockModel;
import mocks.MockStock;
import model.Portfolio;
import view.ViewStock;

import static org.junit.Assert.assertEquals;

public class MockViewTest {
  private MockModel model;
  private MockStock stock;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    model = new MockModel();
    stock = new MockStock("AAPL");
    stock.setPriceOnDate("2021-01-01", 150.0);
    stock.setPriceOnDate("2021-01-31", 160.0);
    model.addMockStock("AAPL", stock);

    InputStream originalIn = System.in;
    System.setOut(new PrintStream(outContent));

  }

  private void simulateUserInput(String input) {
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
  }

  // needed this method so that the line separators would be compatible with testing.
  private String normalizeLineSeparators(String input) {
    return input.replace("\r\n", "\n").replace("\r", "\n");
  }

  @Test
  public void testCreatePortfolioAndAddStock() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\n6\n");

    new ViewStock(model);

    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(10, portfolio.getStocks().get(stock).intValue());
  }

  @Test
  public void testCalculateGainOrLoss() {
    simulateUserInput("1\nAAPL\n2021-01-01\n2021-01-31\n6\n");

    new ViewStock(model);

    assertEquals(10.0, stock.gainedValue("2021-01-01", "2021-01-31"), 0.01);
  }

  @Test
  public void testViewPortfolio() {
    simulateUserInput("5\n1\n5\n6\n");

    Portfolio portfolio = model.makePortfolio("MyPortfolio");
    model.addPortfolio("MyPortfolio", portfolio);

    new ViewStock(model);

    assertEquals("The portfolio is empty!", portfolio.toString());
  }

  @Test
  public void testCalculateXDayMovingAverage() {
    simulateUserInput("2\nAAPL\n10\n2021-01-01\n6\n");

    new ViewStock(model);

    assertEquals(155.0, stock.getMovingAverage(10, "2021-01-01"),
            0.01);
  }

  @Test
  public void testCalculateXDayCrossovers() {
    simulateUserInput("3\nAAPL\n10\n2021-01-01\n2021-01-31\n6\n");

    new ViewStock(model);

    assertEquals("Mock crossover data", stock.getCrossovers("2021-01-01",
            "2021-01-31", 10));
  }

  @Test
  public void testInvalidTickerSymbolForGainOrLoss() {
    simulateUserInput("1\nINVALID\n6\n");
    new ViewStock(model);

    System.out.flush();
    String expectedOutput = "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Enter ticker symbol: Invalid ticker symbol.\n" +
            "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Exiting...\n";

    String actualOutput = outContent.toString();
    assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(actualOutput));
  }

  @Test
  public void testInvalidDatesForGainOrLoss() {
    try {
      simulateUserInput("1\nAAPL\neoirjoiejerif\nrthrth\n6\n");
      new ViewStock(model);

    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date.", e.getMessage());
    }
  }

  @Test
  public void testInvalidTickerSymbolForMovingAverage() {
    try {
      simulateUserInput("2\nINVALID\n");
      new ViewStock(model);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid ticker symbol", e.getMessage());
    }
  }

  @Test
  public void testInvalidDatesForMovingAverage() {
    simulateUserInput("2\nAAPL\n10\nINVALID_DATE\n6\n");

    new ViewStock(model);

    System.out.flush();
    String expectedOutput = "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Enter ticker symbol: " +
            "DISCLAIMER: if you have entered a date range where it falls on a weekend,\n" +
            "the nearest business day forward will be considered\n" +
            "Enter number of days: " +
            "Enter date (YYYY-MM-DD): " +
            "Invalid Start Date\n" +
            "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Exiting...\n";

    String actualOutput = outContent.toString();
    assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(actualOutput));
  }

  @Test
  public void testAddStockToPortfolio() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\n5\n1\n1\nAAPL\n20\n5\n6\n");

    new ViewStock(model);

    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(30, portfolio.getStocks().get(stock).intValue());
  }

  @Test
  public void testRemoveStockFromPortfolio() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\n5\n1\n2\nAAPL\n5\n2\n5\n6\n");

    new ViewStock(model);

    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(5, portfolio.getStocks().get(stock).intValue());
  }

  @Test
  public void testFindPortfolioValue() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\n5\n1\n3\n2021-01-31\n5\n6\n");

    new ViewStock(model);

    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(1600.0, portfolio.valueOfPortfolio("2021-01-31"), 0.001);
  }

  @Test
  public void testFindPortfolioValueInvalidDate() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\n5\n1\n3\nwefwef\n5\n6\n");
    new ViewStock(model);

    System.out.flush();
    String expectedOutput = "";
//    try {
//      simulateUserInput("4\nMyPortfolio\nAAPL\n10\n5\n1\n3\nwefwef\n5\n6\n");
//      new ViewStock(model);
//    } catch (IllegalArgumentException e) {
//      assertEquals("Invalid date.", e.getMessage());
//    }
    String actualOutput = outContent.toString();
    assertEquals(normalizeLineSeparators(expectedOutput), normalizeLineSeparators(actualOutput));
  }

  @Test
  public void testPrintPortfolio() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\n5\n4\n6\n");

    new ViewStock(model);

    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(normalizeLineSeparators("AAPL: 10 shares\n"), normalizeLineSeparators(portfolio.toString()));

  }

  @Test
  public void testInvalidDatesForCrossovers() {
    try {
      simulateUserInput("3\nAAPL\n10\nINVALID_DATE\n2025-66-66\n6\n");
      new ViewStock(model);
//      assertEquals(100, stock.getCrossovers(
//              "INVALID_DATE", "2021-01-31", 10));
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date.", e.getMessage());
    }
  }

}
