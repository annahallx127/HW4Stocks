import model.Stock;
import model.Portfolio;
import model.Model;
import model.ModelImpl;
import model.ModelStock;
import model.ModelPortfolio;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import parser.PortfolioReader;
import parser.PortfolioWriter;

/**
 * Unit tests for the PortfolioReader class, which is responsible for reading
 * stock portfolio data from an XML file. This class tests the correctness of
 * reading and parsing the XML file to reconstruct the portfolio data.
 */
public class PortfolioReaderTest {
  private Portfolio portfolio;
  private String writtenTestFile;
  private Model model;

  /**
   * Rule to create a temporary folder for testing file operations.
   */
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  /**
   * Sets up the test environment before each test. Initializes the portfolio,
   * writes the portfolio data to an XML file using PortfolioWriter, and prepares
   * the test file for reading.
   *
   * @throws IOException if an I/O error occurs during file creation or writing
   */
  @Before
  public void setUp() throws IOException {
    model = new ModelImpl();
    portfolio = new ModelPortfolio("portfolioTest");
    portfolio.add(new ModelStock("AAPL"), 1, "2024-06-06");
    portfolio.add(new ModelStock("GOOG"), 2, "2024-06-06");
    writtenTestFile = "src/data/portfolios/portfolioTest.xml";
    File test = folder.newFile();
    PortfolioWriter writer = new PortfolioWriter("portfolioTest", "2024-06-06",
            folder.getRoot().getAbsolutePath());
    writer.writeStock("AAPL", 1);
    writer.writeStock("GOOG", 2);
    writer.close();
  }

  /**
   * Tests the PortfolioReader by reading the previously written XML file and
   * verifying that the portfolio data is correctly reconstructed. This test
   * checks the equality of the stock data, portfolio name, and transactions
   * between the original portfolio and the one read from the XML file.
   */
  @Test
  public void testPortfolioReader() {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      PortfolioReader reader = new PortfolioReader("portfolioTest", model);
      saxParser.parse(writtenTestFile, reader);
      Map<Stock, Double> readerStocks = reader.getPortfolio().getStocks();
      Map<Stock, Double> portfolioStocks = portfolio.getStocks();
      for (Stock stock : readerStocks.keySet()) {
        assertEquals(portfolioStocks.get(stock), readerStocks.get(stock));
      }
      assertEquals(portfolio.getName(), reader.getPortfolio().getName());
      assertEquals(portfolio.getTransactions(), reader.getPortfolio().getTransactions());
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace(System.err);
    }
  }
}
