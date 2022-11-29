package assignment4;

/**
 * The exception class for invalid json
 */
public class InvalidJsonException extends RuntimeException {

  /**
   * Constructor with message
   *
   * @param message error message
   */
  public InvalidJsonException(final String message) {
    super(message);
  }

  /**
   * Constructor with message and throwable
   *
   * @param message   error message
   * @param throwable throwable
   */
  public InvalidJsonException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
}
