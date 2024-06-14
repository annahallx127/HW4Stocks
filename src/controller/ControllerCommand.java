package controller;

/**
 * Interface for commands in the stock trading application.
 */
public interface ControllerCommand {
  /**
   * Executes the command using the given stock controller.
   *
   * @param controller the controller that will execute the command.
   */
  void execute(Controller controller);
}
