package model;

import java.time.LocalDate;

public class ModelTransaction implements Transaction {

  private final LocalDate date;
  private final Stock stock;
  private final double shares;
  private final String type;

  public ModelTransaction(LocalDate date, Stock stock, double shares, String type) {
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

  // 0 if dates are equal --> valid transaction
  // < 0 if this date is less than other date
  // > 0 this date is greater than other date --> valid transaction?
  @Override
  public int compareTo(Transaction other) {
    return this.date.compareTo(other.getDate());
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

  @Override
  public int compare(Transaction o1, Transaction o2) {
    return 0;
  }
}
