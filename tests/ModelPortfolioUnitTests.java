//package model;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class ModelPortfolioUnitTests {
//  private Portfolio portfolio;
//  private Stock stock1;
//  private Stock stock2;
//
//  @Before
//  public void setUp() {
//    portfolio = new modelPortfolio("Test Portfolio");
////    stock1 = new modelStock("AAPL");
//    stock2 = new modelStock("GOOG");
//  }
//
//  @Test
//  public void testAddStock() {
////    portfolio.add(stock1, 10);
////    assertEquals(10, portfolio.getStocks().get(stock1).intValue());
//
//    portfolio.add(stock2, 20);
//    assertEquals(20, portfolio.getStocks().get(stock2).intValue());
//  }
//
//  @Test
//  public void testRemoveStock() {
//    portfolio.add(stock2, 10);
//    portfolio.remove(stock2, 5);
//    assertEquals(5, portfolio.getStocks().get(stock2).intValue());
//
//    portfolio.remove(stock2, 5);
//    assertEquals(0, (int) portfolio.getStocks().get(stock2));
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void testRemoveNonExistingStockThrows() {
//    portfolio.remove(stock2, 5);
//  }
//
//  @Test(expected = IllegalArgumentException.class)
//  public void testRemoveMoreThanOwned() {
//    portfolio.add(stock2, 10);
//    portfolio.remove(stock2, 15);
//  }
//
//  @Test
//  public void testValueOfPortfolio() {
//    portfolio.add(stock2, 6);
//
//    //takes the closing price
//    double expectedValue = (177.4 * 6);
//    assertEquals(expectedValue, portfolio.valueOfPortfolio("2024/05/29"), 0.01);
//  }
//
//  @Test
//  public void testToString() {
//    portfolio.add(stock1, 10);
//    portfolio.add(stock2, 20);
//
//    String expectedString = "AAPL: 10 shares\nGOOGL: 20 shares\n";
//    assertEquals(expectedString, portfolio.toString());
//  }
//}
