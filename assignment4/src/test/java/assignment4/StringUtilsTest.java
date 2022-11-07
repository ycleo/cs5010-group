package assignment4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

  private StringUtils stringUtils;

  @BeforeEach
  void setUp() {
    stringUtils = new StringUtils();
  }

  @Test
  void matchRegex() {
    Assertions.assertTrue(stringUtils.matchRegex("abc", "abc"));
  }

  @Test
  void getMatchedString() {
    stringUtils.getMatchedString("abc", "\\<[a-zA-Z0-9-_]*\\>", "abc");
  }

  @Test
  void matchQuit() {
    Assertions.assertTrue(stringUtils.matchQuit("q"));
  }

  @Test
  void matchNumber() {
    Assertions.assertTrue(stringUtils.matchNumber("1"));
  }
}