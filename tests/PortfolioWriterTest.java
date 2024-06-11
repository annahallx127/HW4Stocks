import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;

import model.ModelPortfolio;
import model.Portfolio;
import parser.PortfolioReader;
import parser.PortfolioWriter;

public class PortfolioWriterTest {
  Portfolio portfolio;
  PortfolioWriter writer;
  PortfolioReader reader;

  @Before
  public void setUp() throws XMLStreamException {
    portfolio = new ModelPortfolio("portfolioTest");
    reader = new PortfolioReader("portfolioTest");
  }

  @Test
  public void testPortfolioWriter() {

  }
}
