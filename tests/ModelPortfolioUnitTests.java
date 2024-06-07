import model.Model;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModelPortfolioUnitTests {

  private Portfolio portfolio;
  private Model model;
  private Stock stock1;
  private Stock stock2;

  @Before
  public void setUp() {
    model = new ModelImpl();
    portfolio = model.makePortfolio("Test Portfolio");
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
    assertEquals(null, portfolio.getStocks().get(stock2));
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
      portfolio.remove(stock1, 14);;
    }
    catch (IllegalArgumentException e) {
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
    testPortfolio = model.makePortfolio("New Portfolio");
    assertEquals("New Portfolio", testPortfolio.getName());
  }

  @Test
  public void testMakeNewPortfolio() {
    Portfolio testPortfolio;
    testPortfolio = model.makePortfolio("New Portfolio");
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
    testPortfolio = model.makePortfolio("New Portfolio");
    testPortfolio.add(stock1, 3);

    assertEquals(testPortfolio.getStocks(), portfolio.getStocks());
  }

  @Test
  public void testGetStocks2() {
    portfolio.add(stock1, 3);
    portfolio.add(stock2, 3);

    Portfolio testPortfolio;
    testPortfolio = model.makePortfolio("New Portfolio");
    testPortfolio.add(stock1, 3);
    testPortfolio.add(stock2, 3);

    assertEquals(testPortfolio.getStocks(), portfolio.getStocks());
  }

}
