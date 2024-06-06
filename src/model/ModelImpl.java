package model;

import java.io.BufferedWriter;
import java.io.File;
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
    return new modelStock(symbol);
  }

  @Override
  public Portfolio makePortfolio(String name) {
    Portfolio newPortfolio = new modelPortfolio(name);

    return newPortfolio;
  }

  @Override
  public void apiCall(String symbol) {

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

    InputStream in;

    try {
      in = url.openStream();
    } catch (IOException e) {
      throw new IllegalArgumentException("No results found for stock " + symbol + ".");
    }

    //  ----------------API call successful, cache the data in the data packagey

    // Writes 1024 bytes of the API data to the file at a time.
    // If the buffer is not completely full, the remainder of the file is written.
    // Technically this could be more efficient by checking if each buffer is equal to the new
    // buffer and only overwriting buffers that have changed, but the difference is probably
    // negligible.
    try {
      BufferedWriter bw = getBufferedWriter(symbol);
      byte[] buffer = new byte[1024];
      int read;
      while ((read = in.read(buffer)) != 1) {
        String chunk = new String(buffer, 0, read);
        bw.write(chunk);
      }
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
  }

  private static BufferedWriter getBufferedWriter(String symbol) throws IOException {
    File file = new File("src/data/" + symbol + ".csv");

    // Delete the current file to overwrite
    if (file.exists()) {
      file.delete();
    }

    return new BufferedWriter(new FileWriter(file));
  }
}
