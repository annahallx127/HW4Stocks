public abstract class AbstractStock implements Stock {
  private final double price;
  private final double openPrice;
  private final double closePrice;
  private final double lowPrice;
  private final double highPrice;
  private final String name;
  private final String symbol;

  protected AbstractStock(String name, String symbol, double price, double openPrice,
                          double closePrice, double lowPrice, double highPrice) {
    this.name = name;
    this.symbol = symbol;
    this.price = price;
    this.openPrice = openPrice;
    this.closePrice = closePrice;
    this.lowPrice = lowPrice;
    this.highPrice = highPrice;
  }

  @Override
  public boolean gainedValue() {
    return closePrice > openPrice;
  }

  @Override
  public double getChange(int days) {
    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }

    return 0.0;
  }

  @Override
  public double getMovingAverage(int days, String date) {
    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }
    return 0.0;
  }

  @Override
  public String[] getCrossovers(String dateStart, String dateEnd, int days) {
    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }
    return new String[0];
  }
}
