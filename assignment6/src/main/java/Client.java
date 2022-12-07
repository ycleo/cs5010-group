import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * Client class to send and receive message, and communicate with other clients via the server
 */
public class Client {

  final private int ZERO = 0;
  final private int ONE = 1;
  final private int FOUR = 4;
  final private String SPACE = " ";
  final private String INVALID_USERNAME_MESSAGE = "Please type in a valid command.";
  private Socket socket;
  private DataInputStream dataReader;
  private DataOutputStream dataWriter;
  private String clientUsername;

  /**
   * Constructs client instance
   *
   * @param socket         client socket
   * @param clientUsername client username
   */
  public Client(Socket socket, String clientUsername) {
    try {
      this.socket = socket;
      this.dataReader = new DataInputStream(socket.getInputStream());
      this.dataWriter = new DataOutputStream(socket.getOutputStream());
      this.clientUsername = clientUsername;
    } catch (IOException e) {
      closeSocketAndStream(socket, dataReader, dataWriter);
    }
  }

  /**
   * send message based on different command instructions
   */
  public void sendMessage() {
    try {
      sendConnectMessage();

      while (socket.isConnected()) {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine().trim();
        if (command.equals(Constants.HELP)) {
          System.out.println(Constants.HELP_DETAILS);
        } else if (command.equals(Constants.LOGOFF)) {
          sendDisconnectMessage();
        } else if (command.equals(Constants.LIST_ALL_USERS)) {
          sendQueryConnectedUsers();
        } else if (command.substring(ZERO, ONE).equals(Constants.INSULT)) {
          String recipientUsername = command.substring(ONE);
          sendInsultMessage(recipientUsername);
        } else if (command.substring(ZERO, ONE).equals(Constants.TO)) {
          String message = command.substring(command.indexOf(SPACE));
          if (command.substring(ONE, FOUR).equals(Constants.ALL)) {
            sendBroadcastMessage(message);
          } else {
            String recipientUsername = command.substring(ONE, command.indexOf(SPACE));
            sendDirectMessage(recipientUsername, message);
          }
        } else {
          System.out.println(INVALID_USERNAME_MESSAGE);
        }
      }
    } catch (IOException e) {
      closeSocketAndStream(socket, dataReader, dataWriter);
    }
  }

  /**
   * Send client username to server
   *
   * @throws IOException I/O exception
   */
  public void sendUsername() throws IOException {
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeInt(clientUsername.length());
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeUTF(clientUsername);
  }

  /**
   * Send connect message to server
   *
   * @throws IOException I/O exception
   */
  public void sendConnectMessage() throws IOException {
    dataWriter.writeInt(Constants.CONNECT_MESSAGE);
    sendUsername();
  }

  /**
   * Send disconnect message to server
   *
   * @throws IOException I/O exception
   */
  public void sendDisconnectMessage() throws IOException {
    dataWriter.writeInt(Constants.DISCONNECT_MESSAGE);
    sendUsername();
  }

  /**
   * Send query connected users message to server
   *
   * @throws IOException I/O exception
   */
  public void sendQueryConnectedUsers() throws IOException {
    dataWriter.writeInt(Constants.QUERY_CONNECTED_USERS);
    sendUsername();
  }

  /**
   * Send insult message request to server
   *
   * @param recipientUsername recipient username
   * @throws IOException I/O exception
   */
  public void sendInsultMessage(String recipientUsername) throws IOException {
    dataWriter.writeInt(Constants.SEND_INSULT);
    sendUsername();
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeInt(recipientUsername.length());
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeUTF(recipientUsername);
  }

  /**
   * Send broadcast message to server
   *
   * @param message broadcast message
   * @throws IOException I/O exception
   */
  public void sendBroadcastMessage(String message) throws IOException {
    dataWriter.writeInt(Constants.BROADCAST_MESSAGE);
    sendUsername();
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeInt(message.length());
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeUTF(message);
  }

  /**
   * Send direct message request to server
   *
   * @param recipientUsername recipient username
   * @param message           direct message
   * @throws IOException I/O exception
   */
  public void sendDirectMessage(String recipientUsername, String message) throws IOException {
    dataWriter.writeInt(Constants.DIRECT_MESSAGE);
    sendUsername();
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeInt(recipientUsername.length());
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeUTF(recipientUsername);
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeInt(message.length());
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeUTF(message);
  }

  /**
   * Creates another runnable thread to listen to message from the server
   */
  public void listenForMessage() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        int messageIdentifier;
        while (socket.isConnected()) {
          try {
            messageIdentifier = dataReader.readInt();
            dataReader.readChar();
            switch (messageIdentifier) {
              case Constants.CONNECT_RESPONSE:
                receiveConnectResponse();
                break;
              case Constants.QUERY_USER_RESPONSE:
                receiveQueryUsersResponse();
                break;
              default:
                receiveMessage();
                break;
            }
          } catch (IOException e) {
            closeSocketAndStream(socket, dataReader, dataWriter);
          }
        }
      }
    }).start();
  }

  /**
   * Receives connect response message
   *
   * @throws IOException I/O exception
   */
  public void receiveConnectResponse() throws IOException {
    boolean success = dataReader.readBoolean();
    dataReader.readChar();
    int messageLength = dataReader.readInt();
    dataReader.readChar();
    String message = dataReader.readUTF();
    System.out.println(message);
  }

  /**
   * Receives query users response
   *
   * @throws IOException I/O exception
   */
  public void receiveQueryUsersResponse() throws IOException {
    System.out.print("OTHER connected users: ");
    int numberOfUsers = dataReader.readInt();
    for (int i = 0; i < numberOfUsers; i++) {
      dataReader.readChar();
      dataReader.readInt();
      dataReader.readChar();
      System.out.print(dataReader.readUTF() + " ");
    }
    System.out.print("\n");
  }

  /**
   * Receives message
   *
   * @throws IOException I/O exception
   */
  public void receiveMessage() throws IOException {
    dataReader.readInt();
    dataReader.readChar();
    System.out.println(dataReader.readUTF());
  }

  /**
   * Close the client socket and the data stream
   *
   * @param socket           client socket
   * @param dataInputStream  client stream reader
   * @param dataOutputStream client stream writer
   */
  public static void closeSocketAndStream(Socket socket, DataInputStream dataInputStream,
      DataOutputStream dataOutputStream) {
    try {
      if (dataInputStream != null) {
        dataInputStream.close();
      }
      if (dataOutputStream != null) {
        dataOutputStream.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Executes client program
   *
   * @param args arguments
   * @throws IOException I/O exception
   */
  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter your username: ");
    String username = scanner.nextLine();
    Socket serverSocket = new Socket(Constants.LOCAL_HOST, Constants.SERVER_PORT);
    Client client = new Client(serverSocket, username);
    client.listenForMessage();
    client.sendMessage();
  }

  /**
   * Tests the client object equals to the passed Object o
   *
   * @param o the passed Object o
   * @return boolean that indicates the equality
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Client client = (Client) o;
    return Objects.equals(this.socket, client.socket) ||
        Objects.equals(this.dataReader, client.dataReader) ||
        Objects.equals(this.dataWriter, client.dataWriter) ||
        Objects.equals(this.clientUsername, client.clientUsername);
  }

  /**
   * Returns the hash code of the client object
   *
   * @return a hash code integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.socket, this.dataReader, this.dataWriter, this.clientUsername);
  }

  /**
   * Returns the string represents the client information
   *
   * @return a string about the client object
   */
  @Override
  public String toString() {
    return "Client: { socket: " + this.socket.toString() + "; "
        + "data reader: " + this.dataReader.toString() + "; "
        + "data writer: " + this.dataWriter.toString() + "; "
        + "client username: " + this.clientUsername + " }";
  }
}
