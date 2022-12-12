import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientTest {
  private static final String USER_NAME = "AAA";
  private static final String RECIPIENT_NAME = "BBB";
  private static final String MESSAGE = "message";

  @Mock
  private Socket socket;
  @Mock
  private InputStream inputStream;
  @Mock
  private OutputStream outputStream;
  @InjectMocks
  private Client client;

  @BeforeEach
  void setUp() throws IOException {
    when(socket.getInputStream()).thenReturn(inputStream);
    when(socket.getOutputStream()).thenReturn(outputStream);
    client = new Client(socket, USER_NAME);
  }

  @AfterEach
  public void cleanUp() throws InterruptedException {
    Client.closeSocketAndStream(socket, client.getDataReader(), client.getDataWriter());
    Thread.currentThread().yield();
  }

  @Test
  public void testConstructor_inputStreamIOException() throws IOException {
    final IOException exception = Mockito.spy(new IOException());
    doThrow(exception).when(socket).getInputStream();

    client = new Client(socket, USER_NAME);

    verify(socket).close();
  }

  @Test
  public void testConstructor_socketCloseException() throws IOException {
    final IOException exception = Mockito.spy(new IOException());
    when(socket.getInputStream()).thenThrow(exception);
    doThrow(exception).when(socket).close();

    client = new Client(socket, USER_NAME);

    verify(exception).printStackTrace();
  }

  @Test
  public void testConstructor_outputStreamException() throws IOException {
    final IOException exception = Mockito.spy(new IOException());
    when(socket.getInputStream()).thenReturn(inputStream);
    when(socket.getOutputStream()).thenThrow(exception);

    client = new Client(socket, USER_NAME);

    verify(socket).close();
    verify(inputStream).close();
  }

  @Test
  public void sendMessage_help() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    InputStream stdin = new ByteArrayInputStream(Constants.HELP.getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(outputStream, times(5)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendMessage_exception() throws IOException {
    doThrow(IOException.class).when(outputStream).write(any(), anyInt(), anyInt());
    InputStream stdin = new ByteArrayInputStream(Constants.HELP.getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(socket).close();
    verify(inputStream).close();
    verify(outputStream).close();
  }

  @Test
  public void sendMessage_logOff() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    InputStream stdin = new ByteArrayInputStream(Constants.LOGOFF.getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(outputStream, times(10)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendMessage_listAllUser() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    InputStream stdin = new ByteArrayInputStream(Constants.LIST_ALL_USERS.getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(outputStream, times(10)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendMessage_insult() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    InputStream stdin = new ByteArrayInputStream((Constants.INSULT + RECIPIENT_NAME).getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(outputStream, times(14)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendMessage_to() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    InputStream stdin = new ByteArrayInputStream((Constants.TO + RECIPIENT_NAME + " " + MESSAGE)
        .getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(outputStream, times(18)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendMessage_all() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    InputStream stdin = new ByteArrayInputStream((Constants.TO + Constants.ALL + " " + MESSAGE)
        .getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(outputStream, times(14)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendMessage_invalid() throws IOException {
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    InputStream stdin = new ByteArrayInputStream(USER_NAME.getBytes());
    System.setIn(stdin);

    client.sendMessage();

    verify(outputStream, times(5)).write(any(), eq(0), anyInt());
  }

  @Test
  public void testSendUsername() throws IOException {
    client.sendUsername();

    verify(outputStream, times(4)).write(any(), eq(0), anyInt());
  }

  @Test
  public void testSendConnectMessage() throws IOException {
    client.sendConnectMessage();

    verify(outputStream, times(5)).write(any(), eq(0), anyInt());
  }

  @Test
  public void testSendDisconnectMessage() throws IOException {
    client.sendDisconnectMessage();

    verify(outputStream, times(5)).write(any(), eq(0), anyInt());
  }

  @Test
  public void testSendQueryConnectedUsers() throws IOException {
    client.sendQueryConnectedUsers();

    verify(outputStream, times(5)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendInsultMessage() throws IOException {
    client.sendInsultMessage(RECIPIENT_NAME);

    verify(outputStream, times(9)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendBroadcastMessage() throws IOException {
    client.sendBroadcastMessage(MESSAGE);

    verify(outputStream, times(9)).write(any(), eq(0), anyInt());
  }

  @Test
  public void sendDirectMessage() throws IOException {
    client.sendDirectMessage(RECIPIENT_NAME, MESSAGE);

    verify(outputStream, times(13)).write(any(), eq(0), anyInt());
  }

  @Test
  public void listenForMessage() throws IOException {
    System.out.println(Thread.currentThread().getName());
    client = new Client(socket, USER_NAME);
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    client.listenForMessage();

    verify(inputStream, times(0)).read();
  }

  @Test
  public void listenForMessage_query() throws IOException {
    System.out.println(Thread.currentThread().getName());
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(23)
        .thenReturn(0);
    client.listenForMessage();

    verify(inputStream, times(10)).read();
  }

  @Test
  public void listenForMessage_connect() throws IOException {
    System.out.println(Thread.currentThread().getName());
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(20)
        .thenReturn(0);
    client.listenForMessage();

    verify(inputStream, times(0)).read();
  }

  @Test
  public void listenForMessage_exception() throws IOException {
    System.out.println(Thread.currentThread().getName());
    final IOException exception = Mockito.spy(new IOException());
    when(socket.isConnected()).thenReturn(true)
        .thenReturn(false);
    when(inputStream.read()).thenThrow(exception);
    client.listenForMessage();

    verify(exception, times(0)).printStackTrace();
    verify(inputStream).close();
    verify(outputStream).close();
    verify(socket).close();
  }

  @Test
  public void receiveConnectResponse() throws IOException {
    client.receiveConnectResponse();

    verify(inputStream, times(11)).read();
  }

  @Test
  public void receiveQueryUsersResponse() throws IOException {
    when(inputStream.read()).thenReturn(0)
        .thenReturn(0)
        .thenReturn(0)
        .thenReturn(1)
        .thenReturn(0);
    client.receiveQueryUsersResponse();

    verify(inputStream, times(14)).read();
  }

  @Test
  public void receiveMessage() throws IOException {
    client.receiveMessage();

    verify(inputStream, times(8)).read();
  }

  @Test
  public void testCloseSocketAndStream() throws IOException {
    Client.closeSocketAndStream(socket, client.getDataReader(), client.getDataWriter());

    verify(socket).close();
    verify(inputStream).close();
    verify(outputStream).close();
  }

  @Test
  public void testCloseSocketAndStream_nullSocket() throws IOException {
    Client.closeSocketAndStream(null, client.getDataReader(), client.getDataWriter());

    verify(socket, times(0)).close();
    verify(inputStream).close();
    verify(outputStream).close();
  }

  @Test
  public void testGetDataReader() {
    assertNotNull(client.getDataReader());
  }

  @Test
  public void testGetDataWriter() {
    assertNotNull(client.getDataWriter());
  }


  @Test
  public void testEquals() {
    assertTrue(client.equals(client));
    assertFalse(client.equals(null));
    assertFalse(client.equals(new Object()));
    assertFalse(client.equals(new Client(socket, null)));
    assertFalse(client.equals(new Client(new Socket(), null)));
    assertTrue(client.equals(new Client(socket, USER_NAME)));
  }

  @Test
  public void testHashCode() {
    int hashCode = client.hashCode();
    assertEquals(hashCode, client.hashCode());
    assertInstanceOf(Integer.class, client.hashCode());
  }

  @Test
  public void testToString() {
    assertTrue(client.toString().contains(USER_NAME));
  }
}