package model;

import java.time.LocalDate;

/**
 * Represents a transaction involving stocks in a portfolio. This interface
 * defines the methods required to access the details of a transaction, such as
 * the date, stock, number of shares, and type of transaction (buy/sell/rebalance).
 */
public interface Transaction {
  /**
   * Gets the date of the transaction.
   *
   * @return the date of the transaction
   */
  LocalDate getDate();

  /**
   * Gets the stock involved in the transaction.
   *
   * @return the stock involved in the transaction
   */
  Stock getStock();

  /**
   * Gets the number of shares involved in the transaction.
   *
   * @return the number of shares involved in the transaction
   */
  double getShares();

  /**
   * Gets the type of transaction (buy/sell/rebalance).
   *
   * @return the type of transaction
   */
  String getType();
}
