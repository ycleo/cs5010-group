package assignment4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApplicationTest {

  private String[] testArgs;

  @BeforeEach
  void setUp() {
    testArgs = new String[]{"./testFiles/grammars"};
  }

  @Test
  void argsLengthGreaterThanOne() {
    testArgs = new String[]{"firstArg", "secondArg"};
    Throwable exception = assertThrows(InvalidJsonException.class,
        () -> Application.main(testArgs));
    assertEquals("Invalid input path", exception.getMessage());
  }

  @Test
  void noArgs() {
    testArgs = new String[]{};
    Throwable exception = assertThrows(InvalidJsonException.class,
        () -> Application.main(testArgs));
    assertEquals("Invalid input path", exception.getMessage());
  }

}
