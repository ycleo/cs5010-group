package assignment4;

/**
 * The exception class for invalid command
 */
public class InvalidCommandException extends RuntimeException{

  /**
   * Constructor with message
   * @param message error message
   */
  public InvalidCommandException(final String message) {
    super(message);
  }

  /**
   * Constructor with message and throwable
   * @param message error message
   * @param throwable throwable
   */
  public InvalidCommandException(final String message, final Throwable throwable) {
    super(message, throwable);
  }

}
