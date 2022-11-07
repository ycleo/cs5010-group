package assignment4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SentenceGeneratorTest {

  private SentenceGenerator sentenceGenerator;
  private Grammar grammar;
  private List<String> start = new ArrayList<>();
  private Scanner scanner;
  private Map<String, Grammar> grammars;

  @BeforeEach
  void setUp() {
    start.add("john");
    start.add("hexa");
    start.add("a");
    grammar = new Grammar("abc", "def", start);
    sentenceGenerator = new SentenceGenerator();
    scanner = new Scanner(System.in);
    grammars = new HashMap<>() {{
      put("abc", grammar);
    }};
  }

  @Test
  void validateGrammar() {
    sentenceGenerator.validateGrammar(grammar);
    assertEquals("abc", grammar.getGrammarTitle());
    assertEquals("def", grammar.getGrammarDesc());
  }

  @Test
  void validateGrammarWithException() {
    grammar = new Grammar("abc", "def", null);
    Assertions.assertThrows(InvalidJsonException.class,
        () -> sentenceGenerator.validateGrammar(grammar));
  }

  @Test
  void generateSentences() {
    assertEquals("john\nhexa\na", sentenceGenerator.generateSentences(grammar));
  }

  @Test
  void handleGrammarNotSelected_Quit() {
    String quitCommand = "q";
    scanner = new Scanner(new ByteArrayInputStream(quitCommand.getBytes()));
    assertNull(sentenceGenerator.handleGrammarNotSelected(scanner, grammars));
  }

  @Test
  void handleGrammarNotSelected_NotANumber() {
    String notANumberCommand = "not a number";
    scanner = new Scanner(new ByteArrayInputStream(notANumberCommand.getBytes()));
    assertNull(sentenceGenerator.handleGrammarNotSelected(scanner, grammars));
  }

  @Test
  void handleGrammarNotSelected_OutOfBound() {
    String outOfBoundCommand = "100000";
    scanner = new Scanner(new ByteArrayInputStream(outOfBoundCommand.getBytes()));
    assertNull(sentenceGenerator.handleGrammarNotSelected(scanner, grammars));
  }

  @Test
  void handleGrammarNotSelected_SmallerThanOne() {
    String smallerThanOneCommand = "0";
    scanner = new Scanner(new ByteArrayInputStream(smallerThanOneCommand.getBytes()));
    assertNull(sentenceGenerator.handleGrammarNotSelected(scanner, grammars));
  }

  @Test
  void handleGrammarNotSelected_ReturnValidGrammar() {
    String smallerThanOneCommand = "1";
    scanner = new Scanner(new ByteArrayInputStream(smallerThanOneCommand.getBytes()));
    assertEquals(grammar, sentenceGenerator.handleGrammarNotSelected(scanner, grammars));
  }

  @Test
  void handleSelectedGrammar_YesCommand() {
    String smallerThanOneCommand = "1";
    scanner = new Scanner(new ByteArrayInputStream(smallerThanOneCommand.getBytes()));
    Grammar selectedGrammar = sentenceGenerator.handleGrammarNotSelected(scanner, grammars);

    String yesCommand = "y";
    scanner = new Scanner(new ByteArrayInputStream(yesCommand.getBytes()));
    assertEquals(selectedGrammar,
        sentenceGenerator.handleSelectedGrammar(selectedGrammar, scanner));
  }

  @Test
  void handleSelectedGrammar_NoCommand() {
    String smallerThanOneCommand = "1";
    scanner = new Scanner(new ByteArrayInputStream(smallerThanOneCommand.getBytes()));
    Grammar selectedGrammar = sentenceGenerator.handleGrammarNotSelected(scanner, grammars);

    String noCommand = "n";
    scanner = new Scanner(new ByteArrayInputStream(noCommand.getBytes()));
    assertNull(sentenceGenerator.handleSelectedGrammar(selectedGrammar, scanner));
  }

  @Test
  void handleSelectedGrammar_default() {
    String smallerThanOneCommand = "1";
    scanner = new Scanner(new ByteArrayInputStream(smallerThanOneCommand.getBytes()));
    Grammar selectedGrammar = sentenceGenerator.handleGrammarNotSelected(scanner, grammars);

    String noCommand = "neitherYNorN";
    scanner = new Scanner(new ByteArrayInputStream(noCommand.getBytes()));
    assertEquals(selectedGrammar,
        sentenceGenerator.handleSelectedGrammar(selectedGrammar, scanner));
  }
}
