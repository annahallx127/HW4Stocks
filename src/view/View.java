package view;

public interface View {
  void run();
  void calculateGainOrLoss();
  void calculateXDayMovingAverage();
  void calculateXDayCrossovers();
  void createPortfolio();
  void viewPortfolio();
}
