package view;

/**
 * An interface representing the view in an MVC (Model-View-Controller) architecture.
 * The view is responsible for displaying information to the user and can be implemented
 * to output to various mediums, such as console, GUI, etc. In this case, this program runs a
 * text based user-interface.
 */
public interface View {

  /**
   * A helper function that system.out.println prints a message to the output appendable.
   * Appends the message followed by a new line separator.
   * If an IOException occurs, it wraps the exception in a RuntimeException and rethrows it.
   *
   * @param message message the message to be printed out my the system.
   */
  void print(String message);
}