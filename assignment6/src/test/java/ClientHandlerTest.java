import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientHandlerTest {
  private static final String USER_NAME = "";

  @Mock
  private Socket socket;
  @Mock
  private InputStream inputStream;
  @Mock
  private OutputStream outputStream;

  @InjectMocks
  private ClientHandler clientHandler;

  @BeforeEach
  void setUp() throws IOException {
    when(socket.getInputStream()).thenReturn(inputStream);
    when(socket.getOutputStream()).thenReturn(outputStream);
    clientHandler = new ClientHandler(socket);
  }

  @AfterEach
  public void cleanUp() {
    ClientHandler.clientHandlers.clear();
  }

  @Test
  public void testConstructor() throws IOException {
    when(socket.getInputStream()).thenThrow(new IOException());

    clientHandler = new ClientHandler(socket);

    verify(socket).close();
    verify(inputStream, times(0)).close();
  }

  @Test
  void run_exception() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenThrow(new IOException());
    clientHandler.run();

    assertTrue(ClientHandler.clientHandlers.isEmpty());
    verify(socket).close();
  }

  @Test
  void run_connect() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(19)
        .thenReturn(0);
    clientHandler.run();

    verify(outputStream, times(6)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(14)).read();
  }

  @Test
  void run_disconnect() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(21)
        .thenReturn(0);
    clientHandler.run();

    verify(outputStream, times(6)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(14)).read();
  }

  @Test
  void run_query() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(22)
        .thenReturn(0);
    clientHandler.run();

    verify(outputStream, times(3)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(14)).read();
  }

  @Test
  void run_broadcast() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(24)
        .thenReturn(0);
    clientHandler.run();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(14)).read();
  }

  @Test
  void run_direct() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(25)
        .thenReturn(0);
    clientHandler.run();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(34)).read();
  }

  @Test
  void run_send() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(27)
        .thenReturn(0);
    clientHandler.run();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(24)).read();
  }

  @Test
  void handleConnectMessage() throws IOException {
    clientHandler.handleConnectMessage();

    verify(outputStream, times(6)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(8)).read();
  }

  @Test
  void handleConnectMessage_exception() throws IOException {
    when(inputStream.read()).thenThrow(new IOException());

    clientHandler.handleConnectMessage();

    verify(inputStream, times(1)).read();
    verify(outputStream, times(6)).write(any(), anyInt(), anyInt());
  }

  @Test
  void handleDisconnectMessage() throws IOException {
    clientHandler.handleDisconnectMessage();

    verify(outputStream, times(6)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(8)).read();
  }

  @Test
  void handleDisconnectMessage_exception() throws IOException {
    when(inputStream.read()).thenThrow(new IOException());

    clientHandler.handleDisconnectMessage();

    verify(inputStream, times(1)).read();
    verify(outputStream, times(6)).write(any(), anyInt(), anyInt());
  }

  @Test
  void handleQueryConnectedUsers() throws IOException {
    ClientHandler.clientHandlers.put("USER_NAME", clientHandler);

    clientHandler.handleQueryConnectedUsers();

    verify(outputStream, times(7)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(8)).read();
  }

  @Test
  void handleQueryConnectedUsers_userEqualsSender() throws IOException {
    ClientHandler.clientHandlers.put(USER_NAME, clientHandler);

    clientHandler.handleQueryConnectedUsers();

    verify(outputStream, times(3)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(8)).read();
  }

  @Test
  void handleQueryConnectedUsers_exception() throws IOException {
    when(inputStream.read()).thenThrow(new IOException());

    clientHandler.handleQueryConnectedUsers();

    verify(inputStream, times(1)).read();
    verify(outputStream, times(3)).write(any(), anyInt(), anyInt());
  }

  @Test
  void handleBroadcastMessage_noHandler() throws IOException {

    clientHandler.handleBroadcastMessage();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(8)).read();
  }

  @Test
  void handleBroadcastMessage_hasHandler() throws IOException {
    ClientHandler.clientHandlers.put(USER_NAME, clientHandler);

    clientHandler.handleBroadcastMessage();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(18)).read();
  }

  @Test
  void handleDirectMessage_noSenderNoRequester() throws IOException {
    clientHandler.handleDirectMessage();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(28)).read();
  }

  @Test
  void handleDirectMessage_hasSenderhasRequester() throws IOException {
    ClientHandler.clientHandlers.put(USER_NAME, clientHandler);

    clientHandler.handleDirectMessage();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(28)).read();
  }

  @Test
  void handleInsultMessage_noSenderNoRequester() throws IOException {
    clientHandler.handleInsultMessage();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(18)).read();
  }

  @Test
  void handleInsultMessage_hasSenderhasRequester() throws IOException {
    ClientHandler.clientHandlers.put(USER_NAME, clientHandler);
    clientHandler.handleInsultMessage();

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(18)).read();
  }

  @Test
  void responseFailedMessage() throws IOException {
    clientHandler.responseFailedMessage(USER_NAME);

    verify(outputStream, times(5)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(0)).read();
  }

  @Test
  void removeClientHandler() throws IOException {
    ClientHandler.clientHandlers.put(USER_NAME, clientHandler);

    clientHandler.removeClientHandler();

    assertTrue(ClientHandler.clientHandlers.isEmpty());
    verify(outputStream, times(0)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(0)).read();
    verify(socket).close();
  }

  @Test
  void removeClientHandler_noHandler() throws IOException {
    ClientHandler.clientHandlers.put(USER_NAME, new ClientHandler(new Socket()));

    clientHandler.removeClientHandler();

    assertFalse(ClientHandler.clientHandlers.isEmpty());
    verify(outputStream, times(0)).write(any(), anyInt(), anyInt());
    verify(inputStream, times(0)).read();
    verify(socket).close();
  }

  @Test
  void testEquals() throws IOException {
    assertTrue(clientHandler.equals(clientHandler));
    assertFalse(clientHandler.equals(null));
    assertFalse(clientHandler.equals(new Object()));
    assertFalse(clientHandler.equals(new ClientHandler(new Socket())));
    assertTrue(clientHandler.equals(new ClientHandler(socket)));
    clientHandler.handleConnectMessage();
    assertFalse(clientHandler.equals(new ClientHandler(socket)));
  }

  @Test
  void testHashCode() {
    int hashCode = clientHandler.hashCode();
    assertEquals(hashCode, clientHandler.hashCode());
    assertInstanceOf(Integer.class, clientHandler.hashCode());
  }

  @Test
  void testToString() {
    assertTrue(clientHandler.toString().contains(socket.toString()));
  }
}