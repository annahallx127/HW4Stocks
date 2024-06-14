package view;

import java.io.IOException;

/**
 * A concrete implementation of the View interface. This class is responsible for
 * displaying messages to the user by appending them to a provided Appendable object.
 * It ensures that messages are correctly appended and handles any I/O exceptions
 * that may occur during the process.
 */
public class ViewImpl implements View {
  private final Appendable out;

  /**
   * Constructs a ViewImpl object with the specified Appendable for output.
   *
   * @param out the Appendable object to which messages will be appended
   */
  public ViewImpl(Appendable out) {
    this.out = out;
  }

  @Override
  public void print(String message) {
    try {
      out.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to append output", e);
    }
  }
}
