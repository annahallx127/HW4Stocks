package model;

public enum PlotInterval {
  DAYS(7), WEEKS(4), MONTHS(12), YEARS(10),
  FIVE_YEARS(5), TEN_YEARS(5);

  private final int baseRows;
  private static final int baseValue = 10;
  private static final int targetFirstValueAsterisks = 5;

  PlotInterval(int baseRows) {
    this.baseRows = baseRows;
  }

  /**
   * The base scale for each asterisk of the x-axis on the plot chart. Used to calculate
   * what the relative scale factor should be when taking into account the total portfolio's value.
   *
   * @return the base scale as an int
   */
  public int scale(double totalValue) {
    return (int) (totalValue / targetFirstValueAsterisks);
  }
}