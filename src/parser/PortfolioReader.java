package parser;

import org.xml.sax.Attributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import model.ModelPortfolio;
import model.ModelStock;
import model.Portfolio;
import model.Stock;

/**
 * A class that reads a stock portfolio from an XML file. This class extends StockReader
 * and provides functionality to parse an XML file containing portfolio data, including the date,
 * stock symbols, and the number of shares.
 * The XML file is expected to have a structure like:
 * <pre>
 * {@code
 * <portfolio date="YYYY-MM-DD">
 *     <stock>
 *         <symbol>STOCK_SYMBOL</symbol>
 *         <shares>NUMBER_OF_SHARES</shares>
 *     </stock>
 *     ...
 * </portfolio>
 * }
 * </pre>
 * <p>
 * The parsed data is used to create a Portfolio object that can be
 * used for various operations such as adding stocks, calculating values, etc.
 */
public class PortfolioReader extends StockReader {
  private Portfolio portfolio;
  private final String name;
  private StringBuilder data;

  private String date;
  private String symbol;
  private double shares;
  private boolean bStock = false;
  private boolean bSymbol = false;
  private boolean bShares = false;

  /**
   * Constructs a PortfolioReader instance to read and parse the specified portfolio file.
   *
   * @param name the name of the portfolio
   */
  public PortfolioReader(String name) {
    this.name = name;
    this.shares = 0.0;
  }

  /**
   * Handles character data between XML tags.
   *
   * @param ch     the characters
   * @param start  the start position in the character array
   * @param length the number of characters to read
   */
  @Override
  public void characters(char[] ch, int start, int length) {
    data.append(new String(ch, start, length));
  }

  /**
   * Initializes the portfolio object at the start of the XML document.
   */
  @Override
  public void startDocument() {
    portfolio = new ModelPortfolio(name);
  }

  /**
   * Handles the start of an XML element.
   *
   * @param uri        the Namespace URI
   * @param localName  the local name (without prefix)
   * @param qName      the qualified name (with prefix)
   * @param attributes the attributes attached to the element
   */
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if (qName.equalsIgnoreCase(name)) {
      startDocument();
      date = attributes.getValue("date");
    } else if (qName.equalsIgnoreCase("stock")) {
      bStock = true;
    } else if (qName.equalsIgnoreCase("symbol")) {
      bSymbol = true;
    } else if (qName.equalsIgnoreCase("shares")) {
      bShares = true;
    }
    data = new StringBuilder();
  }

  /**
   * Handles the end of an XML element.
   *
   * @param uri       the Namespace URI
   * @param localName the local name (without prefix)
   * @param qName     the qualified name (with prefix)
   */
  @Override
  public void endElement(String uri, String localName, String qName) {
    if (bSymbol) {
      symbol = data.toString();
      bSymbol = false;
    } else if (bShares) {
      shares = Double.parseDouble(data.toString());
      bShares = false;
    } else if (bStock) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate validDate = LocalDate.parse(date, formatter);
        Stock stock = new ModelStock(symbol);
        portfolio.add(stock, shares, validDate.toString());
        this.reset();
        bStock = false;
      } catch (DateTimeParseException | IllegalArgumentException e) {
        e.printStackTrace(System.err);
      }
    }
  }

  /**
   * Returns the parsed portfolio object.
   *
   * @return the portfolio object containing parsed stock data
   */
  @Override
  public Portfolio getPortfolio() {
    return portfolio;
  }

  /**
   * Resets the temporary variables used for storing stock data.
   */
  private void reset() {
    symbol = "";
    shares = 0.0;
  }
}
