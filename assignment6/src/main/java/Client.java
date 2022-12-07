import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  private Socket socket;
  private DataInputStream dataReader;
  private DataOutputStream dataWriter;
  private String clientUsername;

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
          sendQueryAllUsers();
        } else if (command.substring(0, 1).equals(Constants.INSULT)) {
          String recipientUsername = command.substring(1);
          sendInsultMessage(recipientUsername);
        } else if (command.substring(0, 1).equals(Constants.TO)) {
          String message = command.substring(command.indexOf(" "));
          if (command.substring(1, 4).equals(Constants.ALL)) {
            sendBroadcastMessage(message);
          } else {
            String recipientUsername = command.substring(1, command.indexOf(" "));
            sendDirectMessage(recipientUsername, message);
          }
        } else {
          System.out.println("Please type in a valid command.");
        }
      }
    } catch (IOException e) {
      closeSocketAndStream(socket, dataReader, dataWriter);
    }
  }

  public void sendUsername() {
    try {
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeInt(clientUsername.length());
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeUTF(clientUsername);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendConnectMessage() throws IOException {
    dataWriter.writeInt(Constants.CONNECT_MESSAGE);
    sendUsername();
  }

  public void sendDisconnectMessage() throws IOException {
    dataWriter.writeInt(Constants.DISCONNECT_MESSAGE);
    sendUsername();
  }

  public void sendQueryAllUsers() throws IOException {
    dataWriter.writeInt(Constants.QUERY_CONNECTED_USERS);
    sendUsername();
  }

  public void sendInsultMessage(String recipientUsername) throws IOException {
    dataWriter.writeInt(Constants.SEND_INSULT);
    sendUsername();
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeInt(recipientUsername.length());
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeUTF(recipientUsername);
  }

  public void sendBroadcastMessage(String message) throws IOException {
    dataWriter.writeInt(Constants.BROADCAST_MESSAGE);
    sendUsername();
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeInt(message.length());
    dataWriter.writeChar(Constants.SPACE);
    dataWriter.writeUTF(message);
  }

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
                receiveQueryUserResponse();
                break;
              case Constants.FAILED_MESSAGE, Constants.BROADCAST_MESSAGE, Constants.DIRECT_MESSAGE, Constants.SEND_INSULT:
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

  public void receiveConnectResponse() throws IOException {
    boolean success = dataReader.readBoolean();
    dataReader.readChar();
    int messageLength = dataReader.readInt();
    dataReader.readChar();
    String message = dataReader.readUTF();
    System.out.println(message);
  }

  public void receiveQueryUserResponse() throws IOException {
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

  public void receiveMessage() throws IOException {
    dataReader.readInt();
    dataReader.readChar();
    System.out.println(dataReader.readUTF());
  }

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

  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter your username: ");
    String username = scanner.nextLine();
    Socket serverSocket = new Socket(Constants.LOCAL_HOST, Constants.SERVER_PORT);
    Client client = new Client(serverSocket, username);
    client.listenForMessage();
    client.sendMessage();
  }
}
