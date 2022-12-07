import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * Server class that accept connection from clients and instantiate corresponding client handlers
 */
public class Server {

  final private String connectedMessage = "A new client has connected!";
  private ServerSocket serverSocket;

  /**
   * Constructs server instance
   *
   * @param serverSocket server socket
   */
  public Server(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  /**
   * Starts the server
   */
  public void startServer() {
    try {
      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept();
        System.out.println(connectedMessage);
        ClientHandler clientHandler = new ClientHandler(socket);

        Thread thread = new Thread(clientHandler);
        thread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Closes the server
   */
  public void closeServerSocket() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Executes the server program
   *
   * @param args arguments
   * @throws IOException I/O exception
   */
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
    Server server = new Server(serverSocket);
    server.startServer();
  }

  /**
   * Tests the server object equals to the passed Object o
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
    Server server = (Server) o;
    return Objects.equals(this.serverSocket, server.serverSocket);
  }

  /**
   * Returns the hash code of the server object
   *
   * @return a hash code integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.serverSocket);
  }

  /**
   * Returns the string represents the server information
   *
   * @return a string about the server object
   */
  @Override
  public String toString() {
    return "Server: { server socket: " + this.serverSocket.toString() + " }";
  }

}
