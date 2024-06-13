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
 * A class that reads a stock portfolio from a file. The portfolio is read in XML format.
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

  public PortfolioReader(String name) {
    this.name = name;
    this.shares = 0.0;
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    data.append(new String(ch, start, length));
  }

  @Override
  public void startDocument() {
    portfolio = new ModelPortfolio(name);
  }

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

  @Override
  public Portfolio getPortfolio() {
    return portfolio;
  }

  private void reset() {
    symbol = "";
    shares = 0.0;
  }
}
