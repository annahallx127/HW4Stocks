import model.Model;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
    portfolio.add(stock1, 10, "2024-06-03");
    assertEquals(10, portfolio.getStocks().get(stock1).intValue());

    portfolio.add(stock2, 20, "2024-06-03");
    assertEquals(20, portfolio.getStocks().get(stock2).intValue());
  }

  @Test
  public void testRemoveStock() {
    portfolio.add(stock2, 10, "2024-06-03");
    portfolio.remove(stock2, 5, "2024-06-04");
    assertEquals(5, portfolio.getStocks().get(stock2).intValue());

    portfolio.remove(stock2, 5, "2024-06-05");
    assertNull(portfolio.getStocks().get(stock2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonExistingStockThrows() {
    portfolio.remove(
            stock2, 5, "2024-06-03");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMoreThanOwnedThrows() {
    portfolio.add(stock2, 10, "2024-06-03");
    portfolio.remove(stock2, 15, "2024-06-03");
  }

  @Test
  public void testValueOfPortfolio() {
    portfolio.add(stock2, 6, "2024-05-04");

    double expectedValue = (177.4 * 6);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024-05-29"), 0.01);
  }

  @Test
  public void testPortfolioToString() {
    portfolio.add(stock1, 10, "2024-06-03");
    portfolio.add(stock2, 20, "2024-06-03");

    String expectedString = "AAPL: 10.0 shares" + System.lineSeparator()
            + "GOOG: 20.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.toString());
  }


  @Test
  public void testEmptyPortfolioToString() {
    portfolio.add(stock2, 10, "2024-06-02");
    portfolio.remove(stock2, 10, "2024-06-02");
    String expectedString = "The portfolio is empty!";
    assertEquals(expectedString, portfolio.toString());
  }

  @Test
  public void testRemoveFromEmptyPortfolio() {
    try {
      portfolio.add(stock1, 10, "2024-06-02");
      portfolio.remove(stock1, 14, "2024-06-02");
    } catch (IllegalArgumentException e) {
      assertEquals("Cannot remove more shares than the number of shares present.",
              e.getMessage());
    }
  }

  @Test
  public void testAddMaxToPortfolio2() {
    portfolio.add(stock2, 1000000000, "2024-06-02");
    String expectedString = "GOOG: 1.0E9 shares" + System.lineSeparator();
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
    testPortfolio.add(stock2, 100, "2024-06-02");

    String expectedString = "GOOG: 100.0 shares" + System.lineSeparator();
    assertEquals(expectedString, testPortfolio.toString());

    testPortfolio.remove(stock2, 10, "2024-06-02");
    String expectedString2 = "GOOG: 90.0 shares" + System.lineSeparator();
    assertEquals(expectedString2, testPortfolio.toString());
  }

  @Test
  public void testValueOfPortfolio2() {
    portfolio.add(stock1, 3, "2024-06-02");
    double expectedValue = (3 * 195.87);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024-06-05"), 0.01);

    portfolio.remove(stock1, 1, "2024-06-02");
    double expectedValue2 = (2 * 195.87);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-06-05"), 0.01);
  }

  @Test
  public void testValueOfPortfolioOnDifferentDates() {
    portfolio.add(stock1, 3, "2024-06-02");
    double expectedValue = (3 * 195.87);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024-06-05"), 0.01);

    double expectedValue2 = (3 * 194.0300);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-06-03"), 0.01);

//    double expectedValue3 = (3 * 190.9000);
    double expectedValue3 = 0.0; // should now be 0.0
    assertEquals(expectedValue3, portfolio.valueOfPortfolio("2024-05-22"), 0.01);
  }

  @Test
  public void testValueOfPortfolioOnHoliday() {
    portfolio.add(stock1, 3, "2022-12-05");
    double expectedValue = (3 * 193.6000);
    assertEquals(expectedValue, portfolio.valueOfPortfolio("2023-12-25"), 0.01);

    double expectedValue2 = (3 * 192.5300);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-01-01"), 0.01);
  }

  @Test
  public void testValueOfPortfolioOnNonMarketDay() {
    // tests for weekend dates, takes the friday before value instead

    portfolio.add(stock1, 6, "2024-06-02");
    double expectedValue2 = (6 * 194.0300);
    assertEquals(expectedValue2, portfolio.valueOfPortfolio("2024-06-03"), 0.01);
  }


  @Test
  public void testAddZeroShares() {
    try {
      portfolio.add(stock2, 0, "2024-06-03");
    } catch (IllegalArgumentException e) {
      assertEquals("Shares must be greater than zero.", e.getMessage());
    }
  }

  @Test
  public void testDeleteStockFilledPortfolio() {
    portfolio.add(stock1, 3, "2024-06-03");
    portfolio.remove(stock1, 3, "2024-06-03");
    portfolio.add(stock2, 4, "2024-06-03");

    String expectedString = "GOOG: 4.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.toString());
  }

  @Test
  public void testGetStocks() {
    portfolio.add(stock1, 3, "2024-06-03");

    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");
    testPortfolio.add(stock1, 3, "2024-06-03");

    assertEquals(testPortfolio.getStocks(), portfolio.getStocks());
  }

  @Test
  public void testGetStocks2() {
    portfolio.add(stock1, 3, "2024-06-03");
    portfolio.add(stock2, 3, "2024-06-03");

    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");

    testPortfolio.add(stock1, 3, "2024-06-03");
    testPortfolio.add(stock2, 3, "2024-06-03");

    assertEquals(testPortfolio.getStocks(), portfolio.getStocks());
  }

  @Test
  public void testGetValueDistribution() {
    portfolio.add(stock1, 3, "2024-06-03");
    portfolio.add(stock2, 3, "2024-06-03");

    Portfolio testPortfolio;
    model.makePortfolio("New Portfolio");
    testPortfolio = model.getPortfolios().get("New Portfolio");

    testPortfolio.add(stock1, 3, "2024-06-03");
    testPortfolio.add(stock2, 3, "2024-06-03");
    assertEquals(testPortfolio.getValueDistribution("2024-06-03"),
            portfolio.getValueDistribution("2024-06-03"));
  }

  @Test
  public void testGetValueDistributionContents() {
    portfolio.add(stock1, 3, "2024-06-03"); //194.0300 per share @date --> 582.09
    portfolio.add(stock2, 3, "2024-06-03"); //174.4200 per share @date --> 523.26
    // total value 1,105.35

    String valueDistribution = portfolio.getValueDistribution("2024-06-03");
    System.out.println(valueDistribution);

    assertTrue(valueDistribution.contains("AAPL: $582.09 (52.66%)"));
    assertTrue(valueDistribution.contains("GOOG: $523.26 (47.34%)"));
    assertTrue(valueDistribution.contains("Total Portfolio Value: $1105.35"));
  }

  // use this to test getValueDistribution too
  @Test
  public void testReBalancePortfolio() {
    portfolio.add(stock1, 4, "2023-10-13"); //178.8500 --> 715.4
    portfolio.add(stock2, 3, "2023-10-13"); //138.5800 --> 415.74
    //total: 1,131.13

    Map<Stock, Integer> targetWeights = new HashMap<>();
    targetWeights.put(stock1, 25); //1,131.13 * .25 then / 194.03 to get shares
    targetWeights.put(stock2, 75);

    portfolio.reBalancePortfolio("2024-06-03", targetWeights);
    // appl on date ^ 194.0300
    // goog on date ^ 174.4200

    Portfolio portfolioAfterReBalance;
    model.makePortfolio("New Portfolio");
    portfolioAfterReBalance = model.getPortfolios().get("New Portfolio");

    portfolioAfterReBalance.add(stock1, 1.6741998660001034, "2024-06-03");
    portfolioAfterReBalance.add(stock2, 5.587289301685588, "2024-06-03");

    assertEquals(portfolioAfterReBalance.valueOfPortfolio("2024-06-03"),
            portfolio.valueOfPortfolio("2024-06-03"), 0.001);
    assertEquals(portfolioAfterReBalance.getValueDistribution("2024-06-03"),
            portfolio.getValueDistribution("2024-06-03"));

  }

  @Test
  public void testReBalancePortfolioSameDate() {
    portfolio.add(stock1, 4, "2023-10-13"); //178.8500 --> 715.4
    portfolio.add(stock2, 3, "2023-10-13"); //138.5800 --> 415.74
    //total: 1,131.13

    Map<Stock, Integer> targetWeights = new HashMap<>();
    targetWeights.put(stock1, 25); //1,131.13 * .25 then / 194.03 to get shares
    targetWeights.put(stock2, 75);

    portfolio.reBalancePortfolio("2024-06-03", targetWeights);

    // appl on date ^ 194.0300, 1.45741638 shares
    // goog on date ^ 174.4200, 4.86382009 shares

    Portfolio portfolioAfterReBalance;
    model.makePortfolio("New Portfolio");
    portfolioAfterReBalance = model.getPortfolios().get("New Portfolio");

    portfolioAfterReBalance.add(stock1, 1.6741998660001034, "2024-06-03");
    portfolioAfterReBalance.add(stock2, 5.587289301685588, "2024-06-03");

    assertEquals(portfolio.toString(), portfolioAfterReBalance.toString());
    assertEquals(portfolioAfterReBalance.valueOfPortfolio("2024-06-03"),
            portfolio.valueOfPortfolio("2024-06-03"), 0.001);
    assertEquals(portfolioAfterReBalance.getValueDistribution("2024-06-03"),
            portfolio.getValueDistribution("2024-06-03"));

  }

  @Test
  public void testReBalancePortfolioSameDateNonMarketDate() {
    portfolio.add(stock1, 4, "2024-06-02"); //178.8500 --> 715.4
    portfolio.add(stock2, 3, "2024-06-02"); //138.5800 --> 415.74
    //total: 1,131.13

    Map<Stock, Integer> targetWeights = new HashMap<>();
    targetWeights.put(stock1, 25); //1,131.13 * .25 then / 194.03 to get shares
    targetWeights.put(stock2, 75);

    try {
      portfolio.reBalancePortfolio("2024-06-02", targetWeights);
    } catch (IllegalArgumentException e) {
      assertEquals("This program does not support re-balancing a" +
              " portfolio on a non market date! Please enter a valid date.", e.getMessage());
    }
    // appl on date ^ 194.0300, 1.45741638 shares
    // goog on date ^ 174.4200, 4.86382009 shares

    Portfolio portfolioAfterReBalance;
    model.makePortfolio("New Portfolio");
    portfolioAfterReBalance = model.getPortfolios().get("New Portfolio");

    portfolioAfterReBalance.add(stock1, 1.6741998660001034, "2024-06-02");
    portfolioAfterReBalance.add(stock2, 5.587289301685588, "2024-06-02");

  }

  @Test
  public void testGetValueDistributionAfterReBalance() {
    portfolio.add(stock1, 4, "2023-10-13"); //178.8500 --> 715.4
    portfolio.add(stock2, 3, "2023-10-13"); //138.5800 --> 415.74
    //total: 1,131.13

    Map<Stock, Integer> targetWeights = new HashMap<>();
    targetWeights.put(stock1, 25); //1,131.13 * .25 then / 194.03 to get shares
    targetWeights.put(stock2, 75);

    portfolio.reBalancePortfolio("2024-06-03", targetWeights);
    // appl on date ^ 194.0300, 1.45741638 shares
    // goog on date ^ 174.4200, 4.86382009 shares
    Portfolio portfolioAfterReBalance;
    model.makePortfolio("New Portfolio");
    portfolioAfterReBalance = model.getPortfolios().get("New Portfolio");

    portfolioAfterReBalance.add(stock1, 1.67419604, "2024-06-04");
    portfolioAfterReBalance.add(stock2, 5.58727802, "2024-06-04");

    assertEquals(portfolioAfterReBalance.getValueDistribution("2024-06-04"),
            portfolio.getValueDistribution("2024-06-04"));
  }

  @Test
  public void testGetValueDistributionAfterReBalanceZero() {
    portfolio.add(stock1, 4, "2023-10-13"); //178.8500 --> 715.4
    portfolio.add(stock2, 3, "2023-10-13"); //138.5800 --> 415.74
    //total: 1,131.13

    Map<Stock, Integer> targetWeights = new HashMap<>();
    targetWeights.put(stock1, 0); //1,131.13 * .25 then / 194.03 to get shares
    targetWeights.put(stock2, 100);

    portfolio.reBalancePortfolio("2024-06-03", targetWeights);
    // appl on date ^ 194.0300, 1.45741638 shares
    // goog on date ^ 174.4200, 4.86382009 shares
    Portfolio portfolioAfterReBalance;
    model.makePortfolio("New Portfolio");
    portfolioAfterReBalance = model.getPortfolios().get("New Portfolio");

    portfolioAfterReBalance.add(stock2, 7.44972306, "2024-06-04");

    assertEquals(portfolioAfterReBalance.getValueDistribution("2024-06-04"),
            portfolio.getValueDistribution("2024-06-04"));

  }

  @Test
  public void testGetValueDistributionAfterReBalanceZeroNonMarketDate() {
    portfolio.add(stock1, 4, "2023-10-13"); //178.8500 --> 715.4
    portfolio.add(stock2, 3, "2023-10-13"); //138.5800 --> 415.74
    //total: 1,131.13

    Map<Stock, Integer> targetWeights = new HashMap<>();
    targetWeights.put(stock1, 0); //1,131.13 * .25 then / 194.03 to get shares
    targetWeights.put(stock2, 100);

    portfolio.reBalancePortfolio("2024-06-03", targetWeights);
    // appl on date ^ 194.0300, 1.45741638 shares
    // goog on date ^ 174.4200, 4.86382009 shares
    Portfolio portfolioAfterReBalance;
    model.makePortfolio("New Portfolio");
    portfolioAfterReBalance = model.getPortfolios().get("New Portfolio");

    portfolioAfterReBalance.add(stock2, 7.44972306, "2024-06-03");
    assertEquals(portfolioAfterReBalance.getValueDistribution("2024-06-09"),
            portfolio.getValueDistribution("2024-06-09"));
  }

  // if the user decides to remove stocks before the date they last changed it, it throws
  @Test
  public void testRemoveStocksBeforeLastTransactionDate() {
    portfolio.add(stock1, 10, "2023-10-13");
    portfolio.remove(stock1, 4, "2024-06-03");

    try {
      portfolio.remove(stock1, 4, "2024-05-22");
    } catch (IllegalArgumentException e) {
      assertEquals("Transaction date cannot be before the latest transaction date.",
              e.getMessage());
    }
  }

  @Test
  public void testRemoveStockAfterValidAdd() {
    portfolio.add(stock1, 10, "2023-10-13");
    portfolio.remove(stock1, 4, "2024-06-03");
    portfolio.add(stock1, 10, "2024-06-05"); // add is after last transaction

    try {
      portfolio.remove(stock1, 4, "2024-06-04");
    } catch (IllegalArgumentException e) {
      assertEquals("Transaction date cannot be before the latest transaction date.",
              e.getMessage());
    }
  }

  @Test
  public void testAddStocksBeforeLastTransactionDate() {
    portfolio.add(stock1, 10, "2023-10-13");
    portfolio.remove(stock1, 4, "2024-06-03");

    try {
      portfolio.add(stock1, 10, "2024-10-13");
    } catch (IllegalArgumentException e) {
      assertEquals("Transaction date cannot be before the latest transaction date.", e.getMessage());
    }
  }

  @Test
  public void testAddStockAfterLastTransactionDateValid() {
    portfolio.add(stock1, 10, "2023-10-13");
    portfolio.remove(stock1, 4, "2024-06-03");
    portfolio.add(stock1, 10, "2024-06-03");

    String expectedString = "AAPL: 16.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.toString());
  }

  @Test
  public void testCompositionOfPortfolio() {
    portfolio.add(stock1, 10, "2023-10-13");
    String expectedString = "AAPL: 10.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.getCompositionAtDate("2024-06-04"));
  }

  @Test
  public void testCompositionOfPortfolioInBetweenTransaction() {
    portfolio.add(stock1, 10, "2023-10-13");
    portfolio.add(stock1, 10, "2024-05-22");


    String expectedString = "AAPL: 20.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.getCompositionAtDate("2024-05-22"));

    portfolio.remove(stock1, 3, "2024-06-03");

    String expectedString2 = "AAPL: 17.0 shares" + System.lineSeparator();
    assertEquals(expectedString2, portfolio.getCompositionAtDate("2024-06-04"));

  }

  @Test
  public void testCompositionOfPortfolioNonMarketDate() {
    portfolio.add(stock1, 10, "2023-10-13");
    portfolio.add(stock1, 10, "2024-05-22");

    String expectedString = "AAPL: 20.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.getCompositionAtDate("2024-06-02"));

  }

  @Test
  public void testCompositionOfPortfolioMultipleStocks() {
    portfolio.add(stock1, 10, "2023-05-22");
    portfolio.add(stock2, 10, "2023-10-13");

    String expectedString = "AAPL: 10.0 shares" + System.lineSeparator() +
            "GOOG: 10.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.getCompositionAtDate("2024-05-22"));
  }

  @Test
  public void testCompositionOfPortfolioBetweenTransactions2() {
    portfolio.add(stock2, 10, "2022-05-22");
    portfolio.add(stock1, 10, "2023-10-13");

    String expectedString = "GOOG: 10.0 shares" + System.lineSeparator()
            + "AAPL: 10.0 shares" + System.lineSeparator();

    assertEquals(expectedString, portfolio.getCompositionAtDate("2024-05-22"));

    String expectedString2 = "GOOG: 10.0 shares" + System.lineSeparator();
    assertEquals(expectedString2, portfolio.getCompositionAtDate("2022-10-13"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompositionOfPortfolioThrowTransactionBeforeLatestTransaction() {
    portfolio.add(stock1, 10, "2023-10-13");
    portfolio.add(stock2, 10, "2022-05-22");

    String expectedString = "AAPL: 10.0 shares" + System.lineSeparator() +
            "GOOG: 10.0 shares" + System.lineSeparator();
    assertEquals(expectedString, portfolio.getCompositionAtDate("2024-05-22"));

    String expectedString2 = "GOOG: 10.0 shares" + System.lineSeparator();
    assertEquals(expectedString2, portfolio.getCompositionAtDate("2022-10-13"));
  }

  @Test
  public void testCompositionOfPortfolioBeforeTransaction() {
    portfolio.add(stock1, 10, "2024-06-06");

    assertEquals("No transactions have been made in this portfolio yet.",
            portfolio.getCompositionAtDate("2024-06-03"));
  }


}