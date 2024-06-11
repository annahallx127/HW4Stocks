package parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.ModelPortfolio;
import model.ModelStock;
import model.Portfolio;
import model.Stock;

public class PortfolioReader extends DefaultHandler {
  private Portfolio portfolio;
  private final String name;
  private StringBuilder data;

  private String date;
  private String symbol;
  private double shares;
  private boolean bPortfolio = false;
  private boolean bStock = false;
  private boolean bSymbol = false;
  private boolean bDate = false;
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
    if (qName.equalsIgnoreCase("portfolio")) {
      startDocument();
      bPortfolio = true;
    } else if (qName.equalsIgnoreCase("stock")) {
      bStock = true;
    } else if (qName.equalsIgnoreCase("date")) {
      bDate = true;
    } else if (qName.equalsIgnoreCase("symbol")) {
      bSymbol = true;
    } else if (qName.equalsIgnoreCase("shares")) {
      bShares = true;
    }
    data = new StringBuilder();
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (bPortfolio) {
      portfolio = new ModelPortfolio(data.toString());
      bPortfolio = false;
    } else if (bSymbol) {
      symbol = data.toString();
      bSymbol = false;
    } else if (bDate) {
      date = data.toString();
      bDate = false;
    } else if (bShares) {
      shares = Double.parseDouble(data.toString());
      bShares = false;
    } else if (bStock) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Stock stock = new ModelStock(symbol);
        portfolio.add(stock, shares, LocalDate.parse(date, formatter));
        this.reset();
        bStock = false;
      } catch (DateTimeParseException | IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
  }

  private void reset() {
    symbol = "";
    date = "";
    shares = 0.0;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }
}
