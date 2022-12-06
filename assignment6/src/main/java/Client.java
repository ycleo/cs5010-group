import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String clientUsername;

  public Client(Socket socket, String clientUsername) {
    try {
      this.socket = socket;
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.clientUsername = clientUsername;
    } catch (IOException e) {
      closeSocketAndBuffer(socket, bufferedReader, bufferedWriter);
    }
  }

  public void sendMessage() {
    try {
      bufferedWriter.write(clientUsername);
      bufferedWriter.newLine();
      bufferedWriter.flush();

      Scanner scanner = new Scanner(System.in);
      while (socket.isConnected()) {
        String messageToSend = scanner.nextLine();
        bufferedWriter.write(clientUsername + ": " + messageToSend);
        bufferedWriter.newLine();
        bufferedWriter.flush();
      }
    } catch (IOException e) {
      closeSocketAndBuffer(socket, bufferedReader, bufferedWriter);
    }
  }

  public void listenForMessage() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        String messageFromChat = null;
        try {
          messageFromChat = bufferedReader.readLine();
          System.out.println(messageFromChat);
        } catch (IOException e) {
          closeSocketAndBuffer(socket, bufferedReader, bufferedWriter);
        }
      }
    }).start();
  }

  public void closeSocketAndBuffer(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
    try {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (bufferedWriter != null) {
        bufferedWriter.close();
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
    Socket serverSocket = new Socket("localhost", 1234);
    Client client = new Client(serverSocket, username);
    client.listenForMessage();
    client.sendMessage();
  }

}
