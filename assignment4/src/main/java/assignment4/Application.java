package assignment4;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This class includes the main class for running the sentence generator tool
 */
public class Application {

  private static final Integer ARGS_NUMBER = 1;

  /**
   * Main method for running the tool
   *
   * @param args input path
   */
  public static void main(final String[] args) {
    if (args.length != ARGS_NUMBER) {
      throw new InvalidJsonException("Invalid input path");
    }

    System.out.println("Loading grammars...\n\n");
    final FileUtils fileUtils = new FileUtils();
    final List<String> paths = fileUtils.getPaths(args[0]);
    final SentenceGenerator sentenceGenerator = new SentenceGenerator();
    final Map<String, Grammar> grammars = paths.stream()
        .map(path -> fileUtils.readGrammar(path))
        .map(rawGrammar -> sentenceGenerator.validateGrammar(rawGrammar))
        .collect(Collectors.toMap(grammar -> grammar.getGrammarTitle(), grammar -> grammar));

    final StringUtils stringUtils = new StringUtils();
    final Scanner scanner = new Scanner(System.in);
    String command = "";
    Grammar selectedGrammar = null;
    while (!stringUtils.matchQuit(command)) {
      if (selectedGrammar == null) {
        selectedGrammar = sentenceGenerator.handleGrammarNotSelected(scanner, grammars);
      } else {
        selectedGrammar = sentenceGenerator.handleSelectedGrammar(selectedGrammar, scanner);
      }
    }
  }
}
