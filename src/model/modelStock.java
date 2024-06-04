package model;

import java.util.ArrayList;
import java.util.Arrays;

public class modelStock implements Stock {
  private final String symbol;
  private final ArrayList<String> apiInfo;

  protected modelStock(String symbol, String apiInfo) {
    this.symbol = symbol;
    this.apiInfo = new ArrayList<>();

    // Split the .csv file along newlines
    this.apiInfo.addAll(Arrays.asList(apiInfo.split(System.lineSeparator())));
    // Remove the header line
    this.apiInfo.remove(0);
  }

  public double gainedValue(String dateStart, String dateEnd) {
    return 0.0;
  }

  // TODO: Implement this
  @Override
  public double getChange(String dateStart, String dateEnd) {
    // if not valid date, or if dateEnd is not after dateStart, use date class
    return 0.0;
  }

  // TODO: Implement this
  @Override
  public double getMovingAverage(int days, String date) {
    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }
    return 0.0;
  }

  // TODO: Implement this
  @Override
  public String[] getCrossovers(String dateStart, String dateEnd, int days) {
    if (days <= 0) {
      throw new IllegalArgumentException("The number of days must be greater than 0.");
    }
    return new String[0];
  }

  @Override
  public String getSymbol() {
    return symbol;
  }

  // toString prints out stock values
  @Override
  public String toString() {
    return "";
  }


}