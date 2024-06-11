package model;

import java.time.LocalDate;
import java.util.Comparator;

public interface Transaction extends Comparator<Transaction> {
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
   * Gets the type of transaction (buy/sell).
   *
   * @return the type of transaction
   */
  String getType();

  int compareTo(Transaction other);
}
