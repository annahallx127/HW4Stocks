package mocks;

import java.time.LocalDate;

import model.Stock;
import model.Transaction;

/**
 * Represents a transaction involving a stock. This class implements the
 * Transaction interface and provides the details of the transaction,
 * including the date, stock, number of shares, and type of transaction
 * (buy/sell/rebalance).
 */
public class MockTransaction implements Transaction {

  private final LocalDate date;
  private final Stock stock;
  private final double shares;
  private final String type;

  /**
   * Constructs a new ModelTransaction with the specified date, stock,
   * number of shares, and type of transaction.
   *
   * @param date   the date of the transaction
   * @param stock  the stock involved in the transaction
   * @param shares the number of shares involved in the transaction
   * @param type   the type of transaction (buy/sell/rebalance)
   */
  public MockTransaction(LocalDate date, Stock stock, double shares, String type) {
    this.date = date;
    this.stock = stock;
    this.shares = shares;
    this.type = type;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public Stock getStock() {
    return stock;
  }

  @Override
  public double getShares() {
    return shares;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Transaction{" +
            "date=" + date +
            ", stock=" + stock +
            ", shares=" + shares +
            ", type='" + type + '\'' +
            '}';
  }

}
