package assignment4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGrammar {
  private String expectedGrammarTitle;
  private String expectedGrammarDesc;
  private List<String> expectedStart;
  private Map<String, List<String>> expectedNonTerminal;
  private String expectedToString;
  private int expectedHashCode;
  private Grammar testGrammar;
  private Grammar testGrammar1;

  @BeforeEach
  void setUp() {
    expectedGrammarTitle = "Term Paper Generator";
    expectedGrammarDesc = "A grammar that generates term papers. ";
    expectedStart = new ArrayList<>(
        List.of("<intro> ",
                "<body1> ",
                "<body2>",
                "<conclusion> "));
    expectedNonTerminal = new HashMap<>();
    testGrammar = new Grammar("Term Paper Generator",
        "A grammar that generates term papers. ",
        new ArrayList<String>(
          List.of("<intro> ",
                  "<body1> ",
                  "<body2>",
                  "<conclusion> "))
    );
    testGrammar1 = new Grammar("Term Paper Generator",
        "A grammar that generates term papers. ",
        new ArrayList<String>(
            List.of("<intro> ",
                "<body1> ",
                "<body2>",
                "<conclusion> "))
    );
  }

  @Test
  void getGrammarDesc() {
    assertEquals(expectedGrammarDesc, testGrammar.getGrammarDesc());
  }

  @Test
  void getGrammarTitle() {
    assertEquals(expectedGrammarTitle, testGrammar.getGrammarTitle());
  }

  @Test
  void getNonTerminal() {
    assertEquals(expectedNonTerminal, testGrammar.getNonTerminal());
  }

  @Test
  void getStart() {
    assertEquals(expectedStart, testGrammar.getStart());
  }

  @Test
  void modifyGrammarDesc() {
    expectedGrammarDesc = "modified description";
    testGrammar.modifyGrammarDesc("modified description");
    assertEquals(expectedGrammarDesc, testGrammar.getGrammarDesc());
  }

  @Test
  void modifyGrammarTitle() {
    expectedGrammarTitle = "modified title";
    testGrammar.modifyGrammarTitle("modified title");
    assertEquals(expectedGrammarTitle, testGrammar.getGrammarTitle());
  }

  @Test
  void modifyStart() {
    expectedStart = new ArrayList<>();
    testGrammar.modifyStart(new ArrayList<>());
    assertEquals(expectedStart, testGrammar.getStart());
  }

  @Test
  void validateGrammar_NullStart() {
    testGrammar.modifyStart(null);
    assertFalse(testGrammar.validateGrammar());
  }

  @Test
  void validateGrammar_EmptyStart() {
    testGrammar.modifyStart(new ArrayList<>());
    assertFalse(testGrammar.validateGrammar());
  }

  @Test
  void validateGrammar_NullTitle() {
    testGrammar.modifyGrammarTitle(null);
    assertFalse(testGrammar.validateGrammar());
  }

  @Test
  void validateGrammar_EmptyTitle() {
    testGrammar.modifyGrammarTitle("");
    assertFalse(testGrammar.validateGrammar());
  }

  @Test
  void validateGrammar_ValidGrammar() {
    assertTrue(testGrammar.validateGrammar());
  }

  @Test
  void setNonTerminal() {
    expectedNonTerminal = new HashMap<>() {{
      put("key", new ArrayList<>());
    }};
    testGrammar.setNonTerminal("key", new ArrayList<>());
    assertEquals(expectedNonTerminal, testGrammar.getNonTerminal());
  }

  @Test
  void TestToString() {
    expectedToString = String.format("Grammar[grammarTitle: %s, grammarDesc: %s, start: %s, nonTerminalMap: %s]",
        expectedGrammarTitle,
        expectedGrammarDesc,
        expectedStart,
        expectedNonTerminal);
    assertEquals(expectedToString, testGrammar.toString());
  }

  @Test
  void equals_SameObject() {
    assertTrue(testGrammar.equals(testGrammar));
  }

  @Test
  void equals_NullObject() {
    assertFalse(testGrammar.equals(null));
  }

  @Test
  void equals_DifferentClass() {
    assertFalse(testGrammar.equals(expectedGrammarDesc));
  }

  @Test
  void equals_DifferentTitle() {
    testGrammar1.modifyGrammarTitle("modified title");
    assertFalse(testGrammar.equals(testGrammar1));
  }

  @Test
  void equals_DifferentDesc() {
    testGrammar1.modifyGrammarDesc("modified description");
    assertFalse(testGrammar.equals(testGrammar1));
  }

  @Test
  void equals_DifferentStart() {
    testGrammar1.modifyStart(new ArrayList<>());
    assertFalse(testGrammar.equals(testGrammar1));
  }

  @Test
  void equals_DifferentNonTerminal() {
    testGrammar1.setNonTerminal("key", new ArrayList<>());
    assertFalse(testGrammar.equals(testGrammar1));
  }

  @Test
  void equals_SameGrammar() {
    assertTrue(testGrammar.equals(testGrammar1));
  }

  @Test
  void TestHashCode() {
    expectedHashCode = expectedGrammarTitle.length();
    assertEquals(expectedHashCode, testGrammar.hashCode());
  }
}
