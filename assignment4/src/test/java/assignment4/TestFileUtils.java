package assignment4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFileUtils {

  private String testGrammarDirectoryPath;
  private FileUtils testFileUtils;
  private List<String> expectedPaths;
  private Grammar expectedGrammar;

  @BeforeEach
  void setUp() {
    testGrammarDirectoryPath = "./testFiles/grammars";
    testFileUtils = new FileUtils();
    expectedPaths = new ArrayList<>(
        List.of(
            "./testFiles/grammars/insult_grammar.json",
            "./testFiles/grammars/poem_grammar.json",
            "./testFiles/grammars/term_paper_grammar.json")
    );

    expectedGrammar = new Grammar("Term Paper Generator",
        "A grammar that generates term papers. ",
        new ArrayList<String>(
            List.of("<intro> ",
                "<body1> ",
                "<body2>",
                "<conclusion> "))
    );
  }

  @Test
  void getPaths() {
    assertEquals(expectedPaths, testFileUtils.getPaths(testGrammarDirectoryPath));
  }

  @Test
  void getPaths_InvalidCommandException() {
    Throwable exception = assertThrows(InvalidCommandException.class,
        () -> testFileUtils.getPaths("invalid path"));
    assertEquals("Invalid path for getting grammar files", exception.getMessage());
  }

  @Test
  void readGrammar() {
    FileUtils expectedFileUtils = new FileUtils();
    expectedGrammar = expectedFileUtils.readGrammar("./testFiles/grammars/term_paper_grammar.json");
    assertEquals(expectedGrammar,
        testFileUtils.readGrammar("./testFiles/grammars/term_paper_grammar.json"));
  }

  @Test
  void readGrammar_InvalidPathException() {
    Throwable exception = assertThrows(InvalidJsonException.class,
        () -> testFileUtils.readGrammar("invalid path"));
    assertEquals("Invalid path for reading grammar file: invalid path", exception.getMessage());
  }
}
