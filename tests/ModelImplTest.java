import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import model.Model;
import model.ModelImpl;
import model.Portfolio;
import model.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the ModelImpl implementation.
 * This class contains unit tests to verify the functionality of the ModelImpl class,
 * ensuring that stock and portfolio operations are handled correctly.
 */
public class ModelImplTest {

  private Model model;
  private Stock stock;

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

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
  public void testModelGet() {
    assertEquals("AAPL", stock.toString());
  }

  @Test
  public void testModelGetStockFromAPI() throws IOException {
    Files.deleteIfExists(Paths.get("src/data/AAPL.csv"));
    Stock stock = model.get("AAPL");
    assertEquals("AAPL", stock.toString());
    assertTrue(Files.exists(Paths.get("src/data/AAPL.csv")));
  }

  @Test
  public void testModelGetStockFromFile() throws IOException {
    File tempFile = tempFolder.newFile("TEMP.csv");
    Files.write(Paths.get(tempFile.getPath()), "Sample Data".getBytes());
    Stock stock = model.get("TEMP");
    assertEquals("TEMP", stock.toString());
  }

  @Test
  public void testModelAPIStockNotFound() {
    try {
      model.get("INVALID");
    } catch (IllegalArgumentException e) {
      assertEquals("No results found for stock INVALID.", e.getMessage());
    }
  }

  @Test
  public void testModelMakePortFolio() {
    model.makePortfolio("My Portfolio");
    Portfolio portfolio = model.getPortfolios().get("My Portfolio");
    assertEquals("My Portfolio", portfolio.getName());
  }

  @Test
  public void testModelGetPortfolios() {
    model.makePortfolio("Portfolio 1");
    model.makePortfolio("Portfolio 2");
    assertEquals(2, model.getPortfolios().size());
  }


}