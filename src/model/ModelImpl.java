package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ModelImpl implements Model {
  HashMap<String, Stock> stocks;
  HashMap<String, Portfolio> portfolios;

  public ModelImpl() {
    stocks = new HashMap<>();
    portfolios = new HashMap<>();
  }

  @Override
  public Stock get(String symbol) {
    if (stocks.get(symbol) != null) {
      // in map
      return stocks.get(symbol);
    }
    // call api
    // TODO: this won't work, but it's essentially what the implementation will be.
    return new modelStock(symbol, apiCall(symbol).toString());
  }

  @Override
  public Portfolio makePortfolio(String name) {
    Portfolio p = new modelPortfolio(name);

    return p;
  }

  @Override
  public StringBuilder apiCall(String symbol) {

    final String apiKey = "W0M1JOKC82EZEQA8";
    // extra API Key: OTYUTQ7V96CNWN4C
    URL url = null;

    // TODO: API calls should only be made after the user requests a date that is not cached,
    //  or if the user requests a date range that is not cached. Then, the data should be cached
    //  again if the date is present, or throw an exception if the date is not present.
    try {
      /*
      create the URL. This is the query to the web service. The query string
      includes the type of query (DAILY stock prices), stock symbol to be
      looked up, the API key and the format of the returned
      data (comma-separated values:csv). This service also supports JSON
      which you are welcome to use.
       */
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + symbol + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      /*
      Execute this query. This returns an InputStream object.
      In the csv format, it returns several lines, each line being separated
      by commas. Each line contains the date, price at opening time, highest
      price for that date, lowest price for that date, price at closing time
      and the volume of trade (no. of shares bought/sold) on that date.

      This is printed below.
       */
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + symbol);
    }

    // API call successful, cache the data in the data package
    try {
      // TODO: check if the file exists before overwrite
      // Files.copy()
      FileWriter writer = new FileWriter(symbol + ".csv");
      writer.append(output);
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }

    return output;
  }
}
