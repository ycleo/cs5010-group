package assignment4;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SentenceGeneratorTest {

  private SentenceGenerator sentenceGenerator;
  private Grammar grammar;
  private List<String> start = new ArrayList<>();

  @BeforeEach
  void setUp() {
    start.add("john");
    start.add("hexa");
    start.add("a");
    grammar = new Grammar("abc", "def", start);
    sentenceGenerator = new SentenceGenerator();
  }

  @Test
  void validateGrammar() {
    sentenceGenerator.validateGrammar(grammar);
    Assertions.assertEquals("abc", grammar.getGrammarTitle());
    Assertions.assertEquals("def", grammar.getGrammarDesc());
  }

  @Test
  void validateGrammarWithException() {
    grammar = new Grammar("abc", "def", null);
    Assertions.assertThrows(InvalidJsonException.class, () -> sentenceGenerator.validateGrammar(grammar));
  }

  @Test
  void generateSentences() {
    Assertions.assertEquals("john\nhexa\na", sentenceGenerator.generateSentences(grammar));
  }
}