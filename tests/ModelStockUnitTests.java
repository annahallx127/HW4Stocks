import org.junit.Before;
import org.junit.Test;
import model.Model;
import model.Stock;
import model.ModelImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the Stock implementation within the Model.
 * This class contains unit tests to verify the functionality of the Stock class,
 * ensuring that stock data retrieval and calculations are handled correctly.
 */
public class ModelStockUnitTests {
  private Model model;
  private Stock stock;

  /**
   * Sets up the application with values before each of the following tests.
   * Initializes the model and retrieves a stock instance.
   */
  @Before
  public void setUp() {
    model = new ModelImpl();
    stock = model.get("AAPL");
  }

  @Test
  public void testStockGetSymbol() {
    assertEquals("AAPL", stock.toString());
  }

  @Test
  public void testStockGainedValueSameDay() {
    try {
      stock.gainedValue("2020-01-02", "2020-01-02");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }
  }

  @Test
  public void testStockGainedValueEndBeforeStart() {
    try {
      stock.gainedValue("2020-01-03", "2020-01-02");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }
  }

  @Test
  public void testStockGainedValueIllegalDates() {
    try {
      stock.gainedValue("1234", "5678");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }

    try {
      stock.gainedValue("@#$!@%", "!@%()@+_");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }
  }

  @Test
  public void testStockGainedValueClosedIntoOpenedWeekendDate() {
    assertEquals(1.7800000000000011, stock.gainedValue("2024-06-02",
            "2024-06-03"), 0.01);
  }

  @Test
  public void testStockGainedValueOpenIntoClosed() {
    try {
      assertEquals(0.0, stock.gainedValue("2024-03-31", "2024-06-01"), 0.01);
    } catch (IllegalArgumentException e) {
      assertEquals("Dates must be valid market days.", e.getMessage());
    }
  }

  @Test
  public void testStockGainedValueBothClosed() {
    try {
      assertEquals(0.0, stock.gainedValue("2024-06-01", "2024-06-02"), 0.01);
    } catch (IllegalArgumentException e) {
      assertEquals("Dates must be valid market days.", e.getMessage());
    }
  }

  @Test
  public void testStockGainedValueOneDay() {
    assertEquals(195.8700 - 194.3500,
            stock.gainedValue("2024-06-04", "2024-06-05"), 0.01);
  }

  @Test
  public void testStockGainedValueMultipleDays() {
    assertEquals(195.8700 - 194.0300,
            stock.gainedValue("2024-06-03", "2024-06-05"), 0.01);
  }

  @Test
  public void testStockGainedValueFuture() {
    try {
      assertEquals(0.0, stock.gainedValue("2024-06-01", "2025-06-02"), 0.01);
    } catch (IllegalArgumentException e) {
      assertEquals("Dates cannot be in the future.", e.getMessage());
    }

    try {
      assertEquals(0.0, stock.gainedValue("2025-06-01", "2025-06-02"), 0.01);
    } catch (IllegalArgumentException e) {
      assertEquals("Dates cannot be in the future.", e.getMessage());
    }
  }

  @Test
  public void testStockMovingAverageFuture() {
    try {
      stock.getMovingAverage(1, "2024-06-08");
    } catch (IllegalArgumentException e) {
      assertEquals("Date cannot be in the future.", e.getMessage());
    }
  }

  @Test
  public void testStockMovingAverageIllegalDate() {
    try {
      stock.getMovingAverage(1, "5678");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date.", e.getMessage());
    }

    try {
      stock.getMovingAverage(1, "!@%()@+_");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid date.", e.getMessage());
    }
  }

  @Test
  public void testStockMovingAverageZeroDays() {
    try {
      stock.getMovingAverage(0, "2024-06-05");
    } catch (IllegalArgumentException e) {
      assertEquals("The number of days must be greater than 0.", e.getMessage());
    }
  }

  @Test
  public void testStockMovingAverageNegativeDays() {
    try {
      stock.getMovingAverage(-1, "2024-06-05");
    } catch (IllegalArgumentException e) {
      assertEquals("The number of days must be greater than 0.", e.getMessage());
    }
  }

  // TODO: Make sure to test this in controller - value greater than max Int
  @Test
  public void testStockMovingAverageIllegalDays() {
    try {
      stock.getMovingAverage(Integer.parseInt(String.valueOf((long) (Integer.MAX_VALUE + 1))), "2024-06-05");
    } catch (IllegalArgumentException e) {
      assertEquals("The number of days must be greater than 0.", e.getMessage());
    }
  }

  @Test
  public void testStockMovingAverageSameDay() {
    assertEquals(195.8700, stock.getMovingAverage(1, "2024-06-05"), 0.01);
  }

  @Test
  public void testStockMovingAverageMultipleDays() {
    assertEquals((195.8700 + 194.3500) / 2,
            stock.getMovingAverage(2, "2024-06-05"), 0.01);
    assertEquals((195.8700 + 194.3500 + 194.0300) / 3,
            stock.getMovingAverage(3, "2024-06-05"), 0.01);
  }

  @Test
  public void testStockMovingAverageBeforeStockListing() {
    assertEquals(77.62, stock.getMovingAverage(99, "1999-11-01"), 0.01);
  }

  @Test
  public void testGetPriceOnDate() {
    assertEquals(192.3500, stock.getPriceOnDate("2024-05-21"), 0.01);
  }

  @Test
  public void testGetPriceOnDateInvalidDateGetsFridayBeforePrice() {
      stock.getPriceOnDate("2024-06-02");
      assertEquals(192.25, stock.getPriceOnDate("2024-06-02"), 0.01);
  }

  @Test
  public void testStockCrossoversFuture() {
    try {
      assertEquals(0.0, stock.getCrossovers("2024-06-01", "2025-06-02", 30));
    } catch (IllegalArgumentException e) {
      assertEquals("Dates cannot be in the future.", e.getMessage());
    }

    try {
      assertEquals(0.0, stock.getCrossovers("2025-06-01", "2025-06-02", 30));
    } catch (IllegalArgumentException e) {
      assertEquals("Dates cannot be in the future.", e.getMessage());
    }
  }

  @Test
  public void testStockCrossoversSameDay() {
    try {
      stock.getCrossovers("2020-01-02", "2020-01-02", 1);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }
  }

  @Test
  public void testStockCrossoversBeforeStart() {
    try {
      stock.getCrossovers("2020-01-03", "2020-01-02", 30);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }
  }

  @Test
  public void testStockCrossoversIllegalDates() {
    try {
      stock.getCrossovers("1234", "5678", 30);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }

    try {
      stock.getCrossovers("@#$!@%", "!@%()@+_", 30);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid dates.", e.getMessage());
    }
  }

  @Test
  public void testStockCrossoversClosedIntoOpened() {
    try {
      stock.getCrossovers("2024-06-02", "2024-06-03", 30);
    } catch (IllegalArgumentException e) {
      assertEquals("Dates must be valid market days.", e.getMessage());
    }
  }

  @Test
  public void testStockCrossoversOpenIntoClosed() {
    try {
      stock.getCrossovers("2024-03-31", "2024-06-01", 30);
    } catch (IllegalArgumentException e) {
      assertEquals("Dates must be valid market days.", e.getMessage());
    }
  }

  @Test
  public void testStockCrossoversBothClosed() {
    try {
      stock.getCrossovers("2024-06-01", "2024-06-02", 30);
    } catch (IllegalArgumentException e) {
      assertEquals("Dates must be valid market days.", e.getMessage());
    }
  }

  @Test
  public void testStockCrossoversOneDay() {
    assertEquals("None!" + System.lineSeparator(),
            stock.getCrossovers("2024-06-04", "2024-06-05", 1));
  }

  @Test
  public void testStockCrossoverMultipleDays() {
    String sb = "2024-04-29" + System.lineSeparator() +
            "2024-04-15" + System.lineSeparator() +
            "2024-04-12" + System.lineSeparator() +
            "2024-04-11" + System.lineSeparator();
    assertEquals(sb, stock.getCrossovers("2024-04-10", "2024-04-29", 30));
  }
}
