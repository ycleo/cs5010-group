public class Constants {

  public static final String LOCAL_HOST = "localhost";
  public static final int SERVER_PORT = 1234;
  public static final char SPACE = ' ';
  public static final String HELP = "?";
  public static final String LOGOFF = "logoff";
  public static final String LIST_ALL_USERS = "who";
  public static final String TO = "@";
  public static final String ALL = "all";
  public static final String INSULT = "!";
  public static final int CONNECT_MESSAGE = 19;
  public static final int CONNECT_RESPONSE = 20;
  public static final int DISCONNECT_MESSAGE = 21;
  public static final int QUERY_CONNECTED_USERS = 22;
  public static final int QUERY_USER_RESPONSE = 23;
  public static final int BROADCAST_MESSAGE = 24;
  public static final int DIRECT_MESSAGE = 25;
  public static final int FAILED_MESSAGE = 26;
  public static final int SEND_INSULT = 27;
  public static final String HELP_DETAILS =
      "\n• logoff: Disconnect from the server\n"
          + "• who: List out other connected users\n"
          + "• @<user>: sends a direct message to the specified user \n"
          + "• @all: sends a broadcast message to all users connected\n"
          + "• !<user>: sends a insult message to the to the specified user\n";
}
