package assignment4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvalidCommandExceptionTest {

  private InvalidCommandException invalidCommandException;

  @BeforeEach
  void setUp() {
    invalidCommandException = new InvalidCommandException("Exception");
    invalidCommandException = new InvalidCommandException("Exception", new Throwable());
  }

  @Test
  void cmdException() {

  }
}