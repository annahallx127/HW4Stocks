import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import controller.ControllerImpl;
import mocks.MockModel;
import mocks.MockStock;
import model.Portfolio;
import view.ViewImpl;

import static org.junit.Assert.assertEquals;

public class MockViewTest {
  private MockModel model;
  private MockStock stock;
  private StringWriter outContent;
  private ControllerImpl controller;

  @Before
  public void setUp() {
    model = new MockModel();
    stock = new MockStock("AAPL");
    stock.setPriceOnDate("2021-01-01", 150.0);
    stock.setPriceOnDate("2021-01-31", 160.0);
    model.addMockStock("AAPL", stock);

    outContent = new StringWriter();
  }

  private void simulateUserInput(String input) {
    StringReader inContent = new StringReader(input);
    controller = new ControllerImpl(inContent, outContent);
  }

  private String standardizeLineSeparators(String input) {
    return input.replace("\r\n", "\n").replace("\r", "\n");
  }

  @Test
  public void testCreatePortfolioAndAddStock() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\nno\n6\n");
    new ViewImpl(controller);
    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(10, portfolio.getStocks().get(stock).intValue());
  }

  @Test
  public void testCalculateGainOrLoss() {
    simulateUserInput("1\nAAPL\n2021-01-01\n2021-01-31\n6\n");
    new ViewImpl(controller);
    assertEquals(10.0, stock.gainedValue("2021-01-01", "2021-01-31"), 0.01);
  }

  @Test
  public void testViewPortfolio() {
    simulateUserInput("5\n1\n6\n");
    model.makePortfolio("MyPortfolio");
    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    model.addPortfolio("MyPortfolio", portfolio);
    new ViewImpl(controller);
    assertEquals("The portfolio is empty!", portfolio.toString());
  }

  @Test
  public void testCalculateXDayMovingAverage() {
    simulateUserInput("2\nAAPL\n10\n2021-01-01\n6\n");
    new ViewImpl(controller);
    assertEquals(155.0, stock.getMovingAverage(10, "2021-01-01"), 0.01);
  }

  @Test
  public void testCalculateXDayCrossovers() {
    simulateUserInput("3\nAAPL\n10\n2021-01-01\n2021-01-31\n6\n");
    new ViewImpl(controller);
    assertEquals("Mock crossover data", stock.getCrossovers("2021-01-01", "2021-01-31", 10));
  }

  @Test
  public void testInvalidTickerSymbolForGainOrLoss() {
    simulateUserInput("1\nINVALID\n6\n");
    new ViewImpl(controller);

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
    assertEquals(standardizeLineSeparators(expectedOutput), standardizeLineSeparators(actualOutput));
  }

  @Test
  public void testInvalidDatesForGainOrLoss() {
    simulateUserInput("1\nAAPL\neoirjoiejerif\nrthrth\n6\n");
    new ViewImpl(controller);

    String expectedOutput = "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Enter ticker symbol: Invalid date.\n" +
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
    assertEquals(standardizeLineSeparators(expectedOutput), standardizeLineSeparators(actualOutput));
  }

  @Test
  public void testInvalidTickerSymbolForMovingAverage() {
    simulateUserInput("2\nINVALID\n10\n2021-01-01\n6\n");
    new ViewImpl(controller);

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
    assertEquals(standardizeLineSeparators(expectedOutput), standardizeLineSeparators(actualOutput));
  }

  @Test
  public void testInvalidDatesForMovingAverage() {
    simulateUserInput("2\nAAPL\n10\nINVALID_DATE\n6\n");
    new ViewImpl(controller);

    String expectedOutput = "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Enter ticker symbol: DISCLAIMER: if you have entered a date range where it falls on a weekend,\n" +
            "the nearest business day forward will be considered\n" +
            "Enter number of days: \n" +
            "Enter date (YYYY-MM-DD): \n" +
            "Invalid Date, \n" +
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
    assertEquals(standardizeLineSeparators(expectedOutput), standardizeLineSeparators(actualOutput));
  }

  @Test
  public void testAddStockToPortfolio() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\nno\n1\n1\nAAPL\n20\nno\n5\n6\n");
    new ViewImpl(controller);
    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(30, portfolio.getStocks().get(stock).intValue());
  }

  @Test
  public void testRemoveStockFromPortfolio() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\nno\n1\n2\nAAPL\n5\n5\n6\n");
    new ViewImpl(controller);
    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(5, portfolio.getStocks().get(stock).intValue());
  }

  @Test
  public void testFindPortfolioValue() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\nno\n1\n3\n2021-01-31\n5\n6\n");
    new ViewImpl(controller);
    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(1600.0, portfolio.valueOfPortfolio("2021-01-31"), 0.001);
  }

  @Test
  public void testFindPortfolioValueInvalidDate() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\nno\n1\n3\nwefwef\n5\n6\n");
    new ViewImpl(controller);

    String expectedOutput = "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Enter ticker symbol: DISCLAIMER: If you have entered a weekend, the date considered will be the friday before.\n" +
            "Enter date (YYYY-MM-DD): \n" +
            "Invalid date.\n" +
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
    assertEquals(standardizeLineSeparators(expectedOutput), standardizeLineSeparators(actualOutput));
  }

  @Test
  public void testPrintPortfolio() {
    simulateUserInput("4\nMyPortfolio\nAAPL\n10\nno\n5\n6\n");
    new ViewImpl(controller);
    Portfolio portfolio = model.getPortfolios().get("MyPortfolio");
    assertEquals(standardizeLineSeparators("AAPL: 10 shares\n"), standardizeLineSeparators(portfolio.toString()));
  }

  @Test
  public void testInvalidDatesForCrossovers() {
    simulateUserInput("3\nAAPL\n10\nINVALID_DATE\n2025-66-66\n6\n");
    new ViewImpl(controller);

    String expectedOutput = "Isaac and Anna's Stock Investment Company\n" +
            "Choose an Option From the Menu:\n" +
            "1. Calculate Gain or Loss of a Stock\n" +
            "2. Calculate X-Day Moving Average\n" +
            "3. Calculate X-Day Crossovers\n" +
            "4. Create a New Portfolio\n" +
            "5. View Existing Portfolios\n" +
            "6. Exit Menu\n" +
            "Choose an option: \n" +
            "Enter ticker symbol: DISCLAIMER: if you have entered a date range where it falls on a weekend,\n" +
            "the nearest business day forward will be considered\n" +
            "Enter number of days: \n" +
            "Enter start date (YYYY-MM-DD): \n" +
            "Invalid start date. Please enter a valid market date.\n" +
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
    assertEquals(standardizeLineSeparators(expectedOutput), standardizeLineSeparators(actualOutput));
  }
}
