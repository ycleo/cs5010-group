/**
 * Constants class that includes common used constants for the project
 */
public class Constants {

  /**
   * local host
   */
  public static final String LOCAL_HOST = "localhost";
  /**
   * server port number
   */
  public static final int SERVER_PORT = 1234;
  /**
   * space
   */
  public static final char SPACE = ' ';
  /**
   * help command
   */
  public static final String HELP = "?";
  /**
   * logoff command
   */
  public static final String LOGOFF = "logoff";
  /**
   * list all users command
   */
  public static final String LIST_ALL_USERS = "who";
  /**
   * specified recipient command
   */
  public static final String TO = "@";
  /**
   * all
   */
  public static final String ALL = "all";
  /**
   * insult command
   */
  public static final String INSULT = "!";
  /**
   * connect message identifier
   */
  public static final int CONNECT_MESSAGE = 19;
  /**
   * connect response message identifier
   */
  public static final int CONNECT_RESPONSE = 20;
  /**
   * disconnect message identifier
   */
  public static final int DISCONNECT_MESSAGE = 21;
  /**
   * query connected users message identifier
   */
  public static final int QUERY_CONNECTED_USERS = 22;
  /**
   * query user response message identifier
   */
  public static final int QUERY_USER_RESPONSE = 23;
  /**
   * broadcast message identifier
   */
  public static final int BROADCAST_MESSAGE = 24;
  /**
   * direct message identifier
   */
  public static final int DIRECT_MESSAGE = 25;
  /**
   * failed message identifier
   */
  public static final int FAILED_MESSAGE = 26;
  /**
   * insult message identifier
   */
  public static final int SEND_INSULT = 27;
  /**
   * help details
   */
  public static final String HELP_DETAILS =
      "\n• logoff: Disconnect from the server\n"
          + "• who: List out other connected users\n"
          + "• @<user>: sends a direct message to the specified user \n"
          + "• @all: sends a broadcast message to all users connected\n"
          + "• !<user>: sends a broadcast insult message with a specified insulted user\n";
}
