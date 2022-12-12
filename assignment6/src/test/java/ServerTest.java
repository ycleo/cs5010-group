import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerTest {
  @Mock
  private ServerSocket serverSocket;
  @Mock
  private Socket socket;
  @InjectMocks
  private Server server;
  @Spy
  private IOException ioException;

  @BeforeEach
  void setUp() {
    server = new Server(serverSocket);
  }

  @Test
  void main() {
  }

  @Test
  void startServer() throws IOException {
    when(serverSocket.isClosed()).thenReturn(false)
            .thenReturn(true);
    when(serverSocket.accept()).thenReturn(socket);
    server.startServer();
  }

  @Test
  void startServer_exception() throws IOException {
    when(serverSocket.isClosed()).thenReturn(false)
        .thenReturn(true);
    when(serverSocket.accept()).thenThrow(ioException);
    server.startServer();

    verify(ioException).printStackTrace();
  }

  @Test
  void closeServerSocket() throws IOException {
    server.closeServerSocket();

    verify(serverSocket).close();
  }

  @Test
  void closeServerSocket_null() throws IOException {
    new Server(null).closeServerSocket();
  }

  @Test
  void closeServerSocket_exception() throws IOException {
    doThrow(IOException.class).when(serverSocket).close();

    server.closeServerSocket();
  }

  @Test
  void testEquals() {
    assertTrue(server.equals(server));
    assertFalse(server.equals(null));
    assertFalse(server.equals(new Object()));
    assertTrue(server.equals(new Server(serverSocket)));
  }

  @Test
  void testHashCode() {
    int hashCode = server.hashCode();
    assertEquals(hashCode, server.hashCode());
    assertInstanceOf(Integer.class, server.hashCode());
  }

  @Test
  void testToString() {
    assertTrue(server.toString().contains(serverSocket.toString()));
  }
}