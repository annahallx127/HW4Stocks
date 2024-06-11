import model.Model;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * Test class for the Portfolio implementation within the Model.
 * This class contains unit tests to verify the functionality of adding, removing,
 * and managing stocks within a portfolio, as well as calculating the value of the portfolio.
 */
public class ModelPortfolioUnitTests {

  private Portfolio portfolio;
  private Model model;
  private Stock stock1;
  private Stock stock2;

  /**
   * Sets up the application with values before each of the following tests.
   * Initializes the model, creates a test portfolio, and retrieves stock instances.
   */
  @Before
  public void setUp() {
    model = new ModelImpl();
    model.makePortfolio("Test Portfolio");
    portfolio = model.getPortfolios().get("Test Portfolio");
    stock1 = model.get("AAPL");
    stock2 = model.get("GOOG");
  }

  @Test
  public void testAddStock() {
    portfolio.add(stock1, 10);
    assertEquals(10, portfolio.getStocks().get(stock1).intValue());

    portfolio.add(stock2, 20);
    assertEquals(20, portfolio.getStocks().get(stock2).intValue());
  }

  @Test
  public void testRemoveStock() {
    portfolio.add(stock2, 10);
    portfolio.remove(stock2, 5);
    assertEquals(5, portfolio.getStocks().get(stock2).intValue());

    portfolio.remove(stock2, 5);
    assertNull(portfolio.getStocks().get(stock2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonExistingStockThrows() {
    portfolio.remove(stock2, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMoreThanOwnedThrows() {
    portfolio.add(stock2, 10);
    portfolio.remove(stock2, 15);
  }

  @Test
  public void testValueOfPortfolio() {
    portfolio.add(stock2, 6);

    double expectedValue = (177.4 * 6);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024-05-29"), 0.01);
  }

  @Test
  public void testPortfolioToString() {
    portfolio.add(stock1, 10);
    portfolio.add(stock2, 20);

    String expectedString = "AAPL: 10 shares" + System.lineSeparator()
            + "GOOG: 20 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.toString());
  }


  @Test
  public void testEmptyPortfolioToString() {
    portfolio.add(stock2, 10);
    portfolio.remove(stock2, 10);
    String expectedString = "The portfolio is empty!";
    assertEquals(expectedString, portfolio.toString());
  }

  @Test
  public void testRemoveFromEmptyPortfolio() {
    try {
      portfolio.add(stock1, 10);
      portfolio.remove(stock1, 14);
      ;
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot remove more shares than the number of shares present.",
              e.getMessage());
    }
  }

  @Test
  public void testAddMaxToPortfolio2() {
    portfolio.add(stock2, 1000000000);
    String expectedString = "GOOG: 1000000000 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.toString());
  }

  @Test
  public void testPortfolioGetName() {
    assertEquals("Test Portfolio", portfolio.getName());
  }

  @Test
  public void testNewMakeNewPortfolioGetName() {
    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");
    assertEquals("New Portfolio", testPortfolio.getName());
  }

  @Test
  public void testMakeNewPortfolio() {
    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");
    testPortfolio.add(stock2, 100);

    String expectedString = "GOOG: 100 shares" + System.lineSeparator();
    assertEquals(expectedString, testPortfolio.toString());

    testPortfolio.remove(stock2, 10);
    String expectedString2 = "GOOG: 90 shares" + System.lineSeparator();
    assertEquals(expectedString2, testPortfolio.toString());
  }

  @Test
  public void testValueOfPortfolio2() {
    portfolio.add(stock1, 3);
    double expectedValue = (3 * 195.87);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024-06-05"), 0.01);

    portfolio.remove(stock1, 1);
    double expectedValue2 = (2 * 195.87);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-06-05"), 0.01);
  }

  @Test
  public void testValueOfPortfolioOnDifferentDates() {
    portfolio.add(stock1, 3);
    double expectedValue = (3 * 195.87);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024-06-05"), 0.01);

    double expectedValue2 = (3 * 194.0300);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-06-03"), 0.01);

    double expectedValue3 = (3 * 190.9000);
    assertEquals(expectedValue3, portfolio.valueOfPortfolio("2024-05-22"), 0.01);
  }

  @Test
  public void testValueOfPortfolioOnHoliday() {
    portfolio.add(stock1, 3);
    double expectedValue = (3 * 193.6000);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2023-12-25"), 0.01);

    double expectedValue2 = (3 * 192.5300);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-01-01"), 0.01);
  }

  @Test
  public void testValueOfPortfolioOnNonMarketDay() {
    // tests for weekend dates, takes the friday before value instead
    portfolio.add(stock1, 3);
    double expectedValue = (3 * 192.2500);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024-06-02"), 0.01);

    portfolio.add(stock1, 3);
    double expectedValue2 = (6 * 192.2500);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-06-01"), 0.01);
  }

  @Test
  public void testAddZeroShares() {
    try {
      portfolio.add(stock2, 0);
    } catch (IllegalArgumentException e) {
      assertEquals("Shares added must be one or more.", e.getMessage());
    }
  }

  @Test
  public void testDeleteStockFilledPortfolio() {
    portfolio.add(stock1, 3);
    portfolio.remove(stock1, 3);
    portfolio.add(stock2, 4);

    String expectedString = "GOOG: 4 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.toString());
  }

  @Test
  public void testGetStocks() {
    portfolio.add(stock1, 3);

    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");
    testPortfolio.add(stock1, 3);

    assertEquals(testPortfolio.getStocks(), portfolio.getStocks());
  }

  @Test
  public void testGetStocks2() {
    portfolio.add(stock1, 3);
    portfolio.add(stock2, 3);

    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");

    testPortfolio.add(stock1, 3);
    testPortfolio.add(stock2, 3);

    assertEquals(testPortfolio.getStocks(), portfolio.getStocks());
  }

  @Test
  public void testGetValueDistribution() {
    portfolio.add(stock1, 3);
    portfolio.add(stock2, 3);

    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");

    testPortfolio.add(stock1, 3);
    testPortfolio.add(stock2, 3);
    assertEquals(testPortfolio.getValueDistribution("2024-06-03"),
            portfolio.getValueDistribution("2024-06-03"));
  }

  @Test
  public void testGetValueDistributionContents() {
    portfolio.add(stock1, 3); //194.0300 per share @date --> 582.09
    portfolio.add(stock2, 3); //174.4200 per share @date --> 523.26
    // total value 1,105.35

    String valueDistribution = portfolio.getValueDistribution("2024-06-03");
    System.out.println(valueDistribution);

    assertTrue(valueDistribution.contains("AAPL: 582.09, 53%"));
    assertTrue(valueDistribution.contains("GOOG: 523.26, 47%"));
    assertTrue(valueDistribution.contains("Total Portfolio Value: 1105.35"));
  }

}