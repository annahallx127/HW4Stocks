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
  public Stock get(String symbol) throws IllegalArgumentException {
    // Stored in data, make new stock
    // if ()

    // in map, get the stock
    if (stocks.get(symbol) != null) {
      return stocks.get(symbol);
    }

    // Call API to find stock
    apiCall(symbol);

    // If apiCall doesn't throw an exception, then the stock information was found and stored in the
    // data package. We can create a new stock with the given symbol, and it will automatically
    // assign its own values.
    Stock newStock = new modelStock(symbol);
    stocks.put(symbol, newStock);
    return newStock;
  }

  @Override
  public Portfolio makePortfolio(String name) {
    Portfolio newPortfolio = new modelPortfolio(name);
    portfolios.put(name, newPortfolio);
    return newPortfolio;
  }


  /**
   * Queries the AlphaVantage API to find the information on a given stock. The file returned is
   * cached as a .csv file in the data package. If the file is already present, it is overwritten in
   * favor of the new API data.
   *
   * <p>
   * This method should only be called when stock data is missing from the cached database {@code
   * ~/src/data}.
   * Over reliance on the API can and will result in rate-limiting from the query server.
   * Each API key is limited to 25 calls per day.
   * </p>
   *
   * @param symbol the ticker symbol of the stock.
   * @throws IllegalArgumentException if the stock is not present in the API database.
   */
  private void apiCall(String symbol) throws IllegalArgumentException {

    final String apiKey = "W0M1JOKC82EZEQA8";
    // extra API Key: OTYUTQ7V96CNWN4C
    URL url;

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
      throw new RuntimeException("The AlphaVantage API has either changed or "
              + "no longer works.");
    }

    InputStream in;

    try {
      in = url.openStream();
    } catch (IOException e) {
      throw new IllegalArgumentException("No results found for stock " + symbol + ".");
    }

    //  ----------------API call successful, cache the data in the data package----------------

    // Writes 1024 bytes of the API data to the file at a time.
    // If the buffer is not completely full, the remainder of the file is written.
    // Technically this could be more efficient by checking if each buffer is equal to the new
    // buffer and only overwriting buffers that have changed, but the difference is probably
    // negligible.
    try {
      BufferedWriter bw = getBufferedWriter(symbol);
      byte[] buffer = new byte[1024];
      int read;
      while ((read = in.read(buffer)) != -1) {
        String chunk = new String(buffer, 0, read);
        bw.write(chunk);
      }
      in.close();
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
  }

  private static BufferedWriter getBufferedWriter(String symbol) throws IOException {
    File file = new File("src/data/" + symbol + ".csv");
    return new BufferedWriter(new FileWriter(file));
  }

  @Override
  public HashMap<String, Portfolio> getPortfolios() {
    return portfolios;
  }

  @Override
  public void addPortfolio(String name, Portfolio portfolio) {
    portfolios.put(name, portfolio);
  }

}
