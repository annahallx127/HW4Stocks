package model;

public enum PlotScale {
  DAYS(7), WEEKS(4), MONTHS(12), YEARS(10),
  FIVE_YEARS(5), TEN_YEARS(5);

  final int baseRows;

  PlotScale(int baseRows) {
    this.baseRows = baseRows;
  }

  /**
   * The number of rows the plot chart should show. If the number of rows is less than the baseRows,
   * then this method returns the maximum number of rows for the stock.
   *
   * @param stock the stock that should be processed
   * @return the number of rows this plot should have
   */
  public int numRows(Stock stock) {
    return 0;
  }

  /**
   * The base scale for each asterisk of the x-axis on the plot chart. Higher intervals viewed will
   * have a higher scale factor (ex: $100 for a week, $1000 for a month). This is used to calculate
   * what the relative scale factor should be when taking into account the total portfolio's value.
   *
   * @return the base scale as an int
   */
  public int baseScale() {
    return baseRows;
  }
}
