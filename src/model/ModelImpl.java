package model;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import parser.PortfolioReader;

/**
 * Represents the model for the stock investment program. The model is responsible for storing
 * information about stocks and portfolios, and for making API calls to retrieve stock data.
 * It does not interact with the user directly, but instead provides the data to the controller.
 *
 * <p>
 * Each stock is stored in a map with its symbol as the key. If a stock is not present in the map,
 * the model will make an API call to retrieve the stock data. The stock data is then stored in the
 * data package as a .csv file. If the stock is present in the data package, the model will read the
 * data from the file and create a new stock object with the given symbol.
 * </p>
 *
 * <p>
 * Each stock has information about its symbol and the data from the API/CSV file. The stock
 * object is responsible for calculating the gain or loss of the stock, the moving average of the
 * stock, and the crossovers of the stock. It can also validate dates and check if a date is a
 * market day, throwing exceptions if the date is invalid. These exceptions are caught by the
 * controller and displayed to the user through the view.
 * </p>
 *
 * <p>
 * Each portfolio is stored in a map with its name as the key. The model is responsible for
 * creating new portfolios, adding stocks to portfolios and calculating the gain or loss of a
 * portfolio.
 * </p>
 */
public class ModelImpl implements Model {
  private final HashMap<String, Stock> stocks;
  private final HashMap<String, Portfolio> portfolios;

  public ModelImpl() {
    stocks = new HashMap<>();
    portfolios = new HashMap<>();
  }

  @Override
  public Stock get(String symbol) throws IllegalArgumentException {
    // if present in data package, get the stock
    if (Files.exists(Paths.get("src/data/api" + symbol + ".csv"))) {
      Stock newStock = new ModelStock(symbol);
      stocks.put(symbol, newStock);
      return newStock;
    }

    // if present in map, get the stock
    if (stocks.containsKey(symbol)) {
      return stocks.get(symbol);
    }

    // Call API to find stock
    apiCall(symbol);

    // If apiCall doesn't throw an exception, then the stock information was found and stored in the
    // data/api package. We can create a new stock with the given symbol, and it will automatically
    // assign its own values.
    Stock newStock = new ModelStock(symbol);
    stocks.put(symbol, newStock);
    return newStock;
  }

  @Override
  public void makePortfolio(String name) {
    Portfolio newPortfolio = new ModelPortfolio(name);
    portfolios.put(name, newPortfolio);
  }

  @Override
  public void savePortfolio(String name, String date) {
    Portfolio p = portfolios.get(name);
    if (p == null) {
      throw new IllegalArgumentException("The portfolio does not exist.");
    }
    p.savePortfolio(date);
  }

  @Override
  public void loadPortfolio(String name, String path) throws IllegalArgumentException {
    // path is a directory - append the name of the portfolio to the path
    if (Paths.get(path).toFile().isDirectory()) {
      path = path + "/" + name + ".xml";
    }

    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      PortfolioReader reader = new PortfolioReader(name);
      saxParser.parse(path, reader);
      portfolios.put(reader.getPortfolio().getName(), reader.getPortfolio());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("The portfolio could not be loaded: " + e.getMessage());
    } catch (ParserConfigurationException | SAXException | IOException e) {
      System.err.println("Error when parsing the file: " + e.getMessage());
    }
  }

  /**
   * Queries the AlphaVantage API to find the information on a given stock. The file returned is
   * cached as a .csv file in the data package. If the file is already present, it is overwritten in
   * favor of the new API data.
   *
   * <p>
   * This method should only be called when stock data is missing from the cached database {@code
   * ~/src/data/api}.
   * Over reliance on the API can and will result in rate-limiting from the query server.
   * Each API key is limited to 25 calls per day.
   * </p>
   *
   * @param symbol the ticker symbol of the stock.
   * @throws IllegalArgumentException if the stock is not present in the API database.
   */
  private void apiCall(String symbol) throws IllegalArgumentException {

    final String apiKey = "09I1ESM2FDLI0Y6D";
    // extra API Key: OTYUTQ7V96CNWN4C
    // High volume API key: 09I1ESM2FDLI0Y6D
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
    // This could be made more efficient by checking if each buffer is equal to the new
    // buffer and only overwriting buffers that have changed.
    try {
      BufferedWriter bw = getBufferedWriter(symbol);
      byte[] buffer = new byte[1024];
      int read;
      while ((read = in.read(buffer)) != -1) {
        String chunk = new String(buffer, 0, read);
        bw.write(chunk);
      }
      bw.flush();
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }
  }

  private static BufferedWriter getBufferedWriter(String symbol) throws IOException {
    File file = Paths.get("src/data/api/" + symbol + ".csv").toFile();
    return new BufferedWriter(new FileWriter(file));
  }

  // TODO: map returns a direct reference - need to return a copy but also be able to modify the
  // TODO: map in the model after removing a stock from a portfolio.
  @Override
  public Map<String, Portfolio> getPortfolios() {
    return portfolios;
  }
}
