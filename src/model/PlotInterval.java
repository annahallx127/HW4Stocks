package model;

/**
 * Represents different intervals for plotting data on a chart. Each interval
 * is associated with a base number of rows used for plotting. The intervals
 * include days, weeks, months, years, five years, and ten years.
 */
public enum PlotInterval {
  DAYS, WEEKS, MONTHS, YEARS, FIVE_YEARS, TEN_YEARS;

  // the min resolution for the plot
  private static final int MIN_RESOLUTION = 100;
  // the max resolution for the plot
  private static final int MAX_RESOLUTION = 1000;
  // the target resolution for the plot
  private int targetResolution;

  PlotInterval() {
    targetResolution = MIN_RESOLUTION;
  }

  /**
   * The asterisk scale for each asterisk of the x-axis on the plot chart. Used to calculate
   * what the relative scale factor should be when taking into account the total portfolio's value.
   *
   * @return the asterisk scale as an int
   */
  public int scaleFactor(double totalValue) {
    return (int) (totalValue / targetResolution);
  }

  /**
   * Set the resolution for the plot chart. The resolution is the difference between the start
   * date's value and the end date's value.
   *
   * @param startDateValue the value of the portfolio at the start date
   * @param endDateValue   the value of the portfolio at the end date
   */
  public void setResolution(double startDateValue, double endDateValue) {
    targetResolution = (int) Math.abs(endDateValue - startDateValue);
  }

  /**
   * Get the target resolution for the plot chart.
   *
   * @return the target resolution as an int
   */
  public int getTargetResolution() {
    return targetResolution;
  }
}