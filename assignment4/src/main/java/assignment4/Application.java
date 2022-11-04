package assignment4;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This class includes the main class for running the sentence generator tool
 */
public class Application {
  private static final Integer ARGS_NUMBER = 1;
  private static final String YES = "y";
  private static final String NO = "n";

  /**
   * Main method for running the tool
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
        System.out.println("The following grammars are available:\n");
        final List<String> grammarTitles = grammars.keySet().stream().toList();
        for (int i = 0; i < grammars.size(); i++) {
          System.out.printf("%s. %s\n%n", i+1, grammarTitles.get(i));
        }

        System.out.println("Which would you like to use? (q to quit)\n");
        command = scanner.nextLine().trim();
        if (stringUtils.matchQuit(command.trim().toLowerCase(Locale.US))) {
          return;
        } else if (!stringUtils.matchNumber(command)
            || Integer.parseInt(command) > grammars.size()
            || Integer.parseInt(command) <= 0) {
          System.out.println("Invalid number!\n");
        } else {
          selectedGrammar = grammars.get(grammarTitles.get(Integer.parseInt(command) - 1));
          System.out.println(sentenceGenerator.generateSentences(selectedGrammar));
        }
      } else {
        System.out.println("\nWould you like another? (y/n)\n");
        command = scanner.nextLine().trim();
        switch (command) {
          case YES:
            System.out.println(sentenceGenerator.generateSentences(selectedGrammar));
            break;
          case NO:
            selectedGrammar = null;
            break;
          default:
            System.out.println("\nPlease input y/n");
        }
      }
    }
  }
}
