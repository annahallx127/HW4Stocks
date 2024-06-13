package mocks;

import model.Transaction;
import model.Stock;

import java.time.LocalDate;

public class MockTransaction implements Transaction {
  private final LocalDate date;
  private final Stock stock;
  private final double shares;
  private final String type;

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
    return "MockTransaction{" +
            "date=" + date +
            ", stock=" + stock +
            ", shares=" + shares +
            ", type='" + type + '\'' +
            '}';
  }
}
