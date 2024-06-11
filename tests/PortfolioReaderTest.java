import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

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

  @Before
  public void setUp() throws XMLStreamException {
    portfolio = new ModelPortfolio("portfolioTest");
    writer = new PortfolioWriter("portfolioTest");
    writer.writeStock("2024-06-07", "AAPL", 1, 196.8900);
    reader = new PortfolioReader("portfolioTest");
  }

  @Test
  public void testPortfolioReaderGetName() {
    Attributes a = new AttributesImpl();
    String expectedOutput = a.getValue("name");
    reader.startDocument();
  }
}
