package assignment4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvalidJsonExceptionTest {

  private InvalidJsonException invalidJsonException;

  @BeforeEach
  void setUp() {
    invalidJsonException = new InvalidJsonException("Exception");
    invalidJsonException = new InvalidJsonException("Exception", new Throwable());
  }

  @Test
  void jsonException() {

  }
}