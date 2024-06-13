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

import model.ModelPortfolio;
import model.ModelStock;
import model.Portfolio;
import model.Stock;
import parser.PortfolioReader;
import parser.PortfolioWriter;


public class PortfolioReaderTest {
  Portfolio portfolio;
  PortfolioWriter writer;
  PortfolioReader reader;
  File test;
  String writtenTestFile;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {
    portfolio = new ModelPortfolio("portfolioTest");
    portfolio.add(new ModelStock("AAPL"), 1, "2024-06-06");
    portfolio.add(new ModelStock("GOOG"), 2, "2024-06-06");
    writtenTestFile = "src/data/portfolios/portfolioTest.xml";
    test = folder.newFile();
    writer = new PortfolioWriter("portfolioTest", "2024-06-06", folder.getRoot().getAbsolutePath());
    writer.writeStock("AAPL", 1);
    writer.writeStock("GOOG", 2);
    writer.close();
  }

  @Test
  public void testPortfolioReader() {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      reader = new PortfolioReader("portfolioTest");
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
