package model;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
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
    if (dateStart == null || dateEnd == null) {
      throw new IllegalArgumentException("dates cannot be null.");
    }

    try {
      Date start = DateFormat.getDateInstance().parse(dateStart);
      Date end = DateFormat.getDateInstance().parse(dateEnd);

      if (start.after(end)) {
        throw new IllegalArgumentException("start date must be before end date.");
      }
    } catch (ParseException e) {
      throw new IllegalArgumentException("dates must be in a valid format.");
    }

    String foundStart = null;
    String foundEnd = null;
    for (String s : apiInfo) {
      if (s.contains(dateStart)) {
        foundStart = s;
      }
      if (s.contains(dateEnd)) {
        foundEnd = s;
      }
    }

    // check if the given string is a weekend. if it is, go back
    if (foundStart == null || foundEnd == null) {
      throw new IllegalArgumentException("did not find the start date or end date of the stock");
    }

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