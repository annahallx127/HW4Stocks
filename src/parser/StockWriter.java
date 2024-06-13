package parser;

/**
 * An interface for writing a stock to a file. The stock is written in XML format.
 */
public interface StockWriter {

  /**
   * Writes the stock symbol and the number of shares to the file.
   *
   * @param symbol the stock symbol
   * @param shares the number of shares
   */
  void writeStock(String symbol, double shares);

  /**
   * Closes the writer.
   */
  void close();
}
