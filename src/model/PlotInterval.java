package model;

public enum PlotInterval {
  DAYS(7), WEEKS(4), MONTHS(12), YEARS(10),
  FIVE_YEARS(5), TEN_YEARS(5);

  final int baseRows;

  PlotInterval(int baseRows) {
    this.baseRows = baseRows;
  }

  /**
   * The base scale for each asterisk of the x-axis on the plot chart. Higher intervals viewed will
   * have a higher scale factor (ex: $100 for a week, $1000 for a month). This is used to calculate
   * what the relative scale factor should be when taking into account the total portfolio's value.
   *
   * @return the base scale as an int
   */
  public int scale(double totalValue) {
    return baseRows;
  }
}
