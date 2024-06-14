package parser;

import org.xml.sax.helpers.DefaultHandler;

import model.Portfolio;

/**
 * An abstract class for reading a stock portfolio from an XML file. This class extends
 * DefaultHandler and serves as a base class for specific implementations that
 * parse XML data to construct a portfolio object.
 * <p>
 * Subclasses should implement the getPortfolio method to return the
 * portfolio object constructed from the XML data.
 * </p>
 */
public abstract class StockReader extends DefaultHandler {
  /**
   * Returns the portfolio object constructed from the XML file.
   * This method should be implemented by subclasses to provide the
   * specific logic for creating and returning the portfolio
   * from the parsed XML data.
   *
   * @return the portfolio object containing the parsed stock data
   */
  public abstract Portfolio getPortfolio();
}
