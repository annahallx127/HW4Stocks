package parser;

import org.xml.sax.helpers.DefaultHandler;

import model.Portfolio;

/**
 * An abstract class for reading a stock from a file. The stock is read in XML format.
 */
public abstract class StockReader extends DefaultHandler {
  /**
   * Returns the portfolio from the file.
   *
   * @return the portfolio from the file
   */
  public abstract Portfolio getPortfolio();
}
