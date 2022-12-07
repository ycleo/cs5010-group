import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable{

  public static HashMap<String, ClientHandler> clientHandlers = new HashMap<>();
  private Socket socket;
  private DataInputStream dataReader;
  private DataOutputStream dataWriter;
  private String clientUsername;

  public ClientHandler(Socket socket) {
    try {
      this.socket = socket;
      this.dataReader = new DataInputStream(socket.getInputStream());
      this.dataWriter = new DataOutputStream(socket.getOutputStream());
      this.clientUsername = dataReader.readUTF();
    } catch (IOException e) {
      removeClientHandler();
    }
  }

  @Override
  public void run() {
    while (socket.isConnected()) {
      try {
        int messageIdentifier = dataReader.readInt();
        dataReader.readChar();
        switch (messageIdentifier) {
          case Constants.CONNECT_MESSAGE:
            handleConnectMessage();
            break;
          case Constants.DISCONNECT_MESSAGE:
            handleDisconnectMessage();
            break;
          case Constants.QUERY_CONNECTED_USERS:
            handleQueryConnectedUsers();
            break;
          case Constants.BROADCAST_MESSAGE:
            handleBroadcastMessage();
            break;
          case Constants.DIRECT_MESSAGE:
            handleDirectMessage();
            break;
          case Constants.SEND_INSULT:
            break;
        }
      } catch (IOException e) {
        removeClientHandler();
        break;
      }
    }
  }

  public void handleConnectMessage() throws IOException {
    try {
      int usernameLength = dataReader.readInt();
      dataReader.readChar();
      clientUsername = dataReader.readUTF();
      clientHandlers.put(clientUsername, this);

      dataWriter.writeInt(Constants.CONNECT_RESPONSE);
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeBoolean(true);
      dataWriter.writeChar(Constants.SPACE);
      String response = "There are " + clientHandlers.size() + " other connected clients.";
      dataWriter.writeInt(response.length());
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeUTF(response);
    } catch (IOException e){
      dataWriter.writeInt(Constants.CONNECT_RESPONSE);
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeBoolean(false);
      dataWriter.writeChar(Constants.SPACE);
      String response = e.toString();
      dataWriter.writeInt(response.length());
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeUTF(response);
      removeClientHandler();
    }
  }

  public void handleDisconnectMessage() throws IOException {
    try {
      int usernameLength = dataReader.readInt();
      dataReader.readChar();
      clientUsername = dataReader.readUTF();
      clientHandlers.remove(clientUsername);

      dataWriter.writeInt(Constants.CONNECT_RESPONSE);
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeBoolean(true);
      dataWriter.writeChar(Constants.SPACE);
      String response = "You are no longer connected.";
      dataWriter.writeInt(response.length());
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeUTF(response);

      removeClientHandler();
    } catch (IOException e){
      dataWriter.writeInt(Constants.CONNECT_RESPONSE);
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeBoolean(false);
      dataWriter.writeChar(Constants.SPACE);
      String response = e.toString();
      dataWriter.writeInt(response.length());
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeUTF(response);
    }
  }

  public void handleQueryConnectedUsers() throws IOException {
    try {
      int usernameLength = dataReader.readInt();
      dataReader.readChar();
      clientUsername = dataReader.readUTF();

      dataWriter.writeInt(Constants.QUERY_USER_RESPONSE);
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeInt(clientHandlers.size() - 1); // number of other users

      for (String username : clientHandlers.keySet()) {
        if (!clientUsername.equals(username)) {
          dataWriter.writeChar(Constants.SPACE);
          dataWriter.write(username.length());
          dataWriter.writeChar(Constants.SPACE);
          dataWriter.writeUTF(username);
        }
      }
    } catch (IOException e){
      dataWriter.writeInt(Constants.QUERY_USER_RESPONSE);
      dataWriter.writeChar(Constants.SPACE);
      dataWriter.writeInt(0);
    }
  }

  public void handleBroadcastMessage() throws IOException {
      int usernameLength = dataReader.readInt();
      dataReader.readChar();
      String senderUsername = dataReader.readUTF();

      if (clientHandlers.containsKey(senderUsername)) {
        dataReader.readChar();
        int messageLength = dataReader.readInt();
        dataReader.readChar();
        String message = dataReader.readUTF();

        for (ClientHandler clientHandler: clientHandlers.values()) {
            clientHandler.dataWriter.writeUTF(senderUsername + " -> all: " + message + "\n");
        }
      } else {
        responseFailedMessage(senderUsername);
      }
  }

  public void handleDirectMessage() throws IOException {
      int senderUsernameLength = dataReader.readInt();
      dataReader.readChar();
      String senderUsername = dataReader.readUTF();
      dataReader.readChar();
      int recipientUsernameLength = dataReader.readInt();
      dataReader.readChar();
      String recipientUsername = dataReader.readUTF();
      dataReader.readChar();
      int messageLength = dataReader.readInt();
      dataReader.readChar();
      String message = dataReader.readUTF();

      if (clientHandlers.containsKey(senderUsername)) {
        ClientHandler recipientClientHandler = clientHandlers.get(recipientUsername);
        recipientClientHandler.dataWriter.writeUTF(senderUsername + " -> " + recipientUsername + ": " + message + "\n");
      } else {
        responseFailedMessage(senderUsername);
      }
  }

  public void responseFailedMessage(String senderUsername) throws IOException {
    ClientHandler senderClientHandler = clientHandlers.get(senderUsername);
    senderClientHandler.dataWriter.writeInt(Constants.FAILED_MESSAGE);
    senderClientHandler.dataWriter.writeChar(Constants.SPACE);
    String failedMessage = "Invalid sender username";
    senderClientHandler.dataWriter.writeInt(failedMessage.length());
    senderClientHandler.dataWriter.writeChar(Constants.SPACE);
    senderClientHandler.dataWriter.writeUTF(failedMessage);
  }

  public void removeClientHandler() {
    clientHandlers.remove(this);
    Client.closeSocketAndStream(socket, dataReader, dataWriter);
  }
}
