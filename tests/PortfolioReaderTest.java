import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import model.ModelPortfolio;
import model.Portfolio;
import parser.PortfolioReader;
import parser.PortfolioWriter;

import static org.junit.Assert.assertEquals;

public class PortfolioReaderTest {
  Portfolio portfolio;
  PortfolioWriter writer;
  PortfolioReader reader;
  File test;
  String pathToWrite;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {
    portfolio = new ModelPortfolio("portfolioTest");
    pathToWrite = "src/data/portfolios/portfolioTest.xml";
    test = folder.newFile("portfolioTest.xml");
    writer = new PortfolioWriter("portfolioTest", "2024-06-06", test.getAbsolutePath());
    writer.writeStock( "AAPL", 1);
    writer.writeStock("GOOG", 2);
    writer.close();
    reader = new PortfolioReader("portfolioTest");

  }

  @Test
  public void testPortfolioReader() {

  }
}
