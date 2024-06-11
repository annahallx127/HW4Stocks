package parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import model.ModelPortfolio;
import model.ModelStock;
import model.Portfolio;

public class PortfolioReader extends DefaultHandler {
  private Portfolio portfolio;
  private final String pName;
  private StringBuilder data;

  private String date;
  private String symbol;
  private double shares;
  private double value;
  private boolean bStock = false;
  private boolean bSymbol = false;
  private boolean bShares = false;
  private boolean bValue = false;

  public PortfolioReader(String pName) {
    this.pName = pName;
    this.shares = 0.0;
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    data.append(new String(ch, start, length));
  }

  @Override
  public void startDocument() {
    portfolio = new ModelPortfolio(pName);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if (qName.equalsIgnoreCase("portfolio")) {
      startDocument();
    } else if (qName.equalsIgnoreCase("stock")) {
      bStock = true;
    } else if (qName.equalsIgnoreCase("symbol")) {
      bSymbol = true;
    } else if (qName.equalsIgnoreCase("shares")) {
      bShares = true;
    } else if (qName.equalsIgnoreCase("value")) {
      bValue = true;
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
    } else if (bValue) {
      value = Double.parseDouble(data.toString());
      bValue = false;
    }

    if (qName.equalsIgnoreCase("stock")) {
      portfolio.add(new ModelStock(symbol), shares);
    }
  }
}
