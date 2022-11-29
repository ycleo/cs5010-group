package assignment4;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * The class for generate sentence and validate grammar
 */
public class SentenceGenerator {

  private static final String NON_TERMINAL_FORMAT = "\\<%s\\>";
  private static final String NON_TERMINAL_CHECK_REGEX = "\\<[a-zA-Z0-9-_]*\\>";
  private static final String PARSE_REGEX = "\\<|\\>";
  private static final String YES = "y";
  private static final String NO = "n";

  private StringUtils stringUtils;

  /**
   * Constructor for SentenceGenerator
   */
  public SentenceGenerator() {
    stringUtils = new StringUtils();
  }

  /**
   * Validate grammar input
   *
   * @param rawGrammar input grammar
   * @return validated grammar
   */
  public Grammar validateGrammar(final Grammar rawGrammar) {
    if (!rawGrammar.validateGrammar()) {
      throw new InvalidJsonException(
          String.format("Invalid grammar title or start in json: %s", rawGrammar));
    }
    return rawGrammar;
  }

  /**
   * Generate sentences
   *
   * @param grammar selected grammar
   * @return String with all sentences
   */
  public String generateSentences(final Grammar grammar) {
    final List<String> sentenceList = new ArrayList<>();
    for (final String start : grammar.getStart()) {
      sentenceList.add(generateSentence(start, grammar.getNonTerminal()));
    }
    return String.join("\n", sentenceList);
  }

  /**
   * Handle select grammar
   *
   * @param scanner  input scanner
   * @param grammars grammar map
   * @return grammar
   */
  public Grammar handleGrammarNotSelected(final Scanner scanner,
      final Map<String, Grammar> grammars) {
    System.out.println("The following grammars are available:\n");
    final List<String> grammarTitles = grammars.keySet().stream().toList();
    for (int i = 0; i < grammars.size(); i++) {
      System.out.printf("%s. %s\n%n", i + 1, grammarTitles.get(i));
    }

    System.out.println("Which would you like to use? (q to quit)\n");
    String command = scanner.nextLine().trim();
    if (stringUtils.matchQuit(command.trim().toLowerCase(Locale.US))) {
      return null;
    } else if (!stringUtils.matchNumber(command)
        || Integer.parseInt(command) > grammars.size()
        || Integer.parseInt(command) <= 0) {
      System.out.println("Invalid number!\n");
      return null;
    } else {
      final Grammar selectedGrammar = grammars.get(
          grammarTitles.get(Integer.parseInt(command) - 1));
      System.out.println(generateSentences(selectedGrammar));
      return selectedGrammar;
    }
  }

  /**
   * handle user input after grammar is selected
   *
   * @param grammar selected grammar
   * @param scanner input scanner
   * @return grammar
   */
  public Grammar handleSelectedGrammar(final Grammar grammar, final Scanner scanner) {
    System.out.println("\nWould you like another? (y/n)\n");
    final String command = scanner.nextLine().trim();
    switch (command) {
      case YES:
        System.out.println(generateSentences(grammar));
        return grammar;
      case NO:
        return null;
      default:
        System.out.println("\nPlease input y/n");
        return grammar;
    }
  }

  private String generateSentence(final String start,
      final Map<String, List<String>> nonTerminalMap) {
    String finalSentence = String.valueOf(start);
    final Random random = new Random();
    while (stringUtils.matchRegex(finalSentence, NON_TERMINAL_CHECK_REGEX)) {
      final List<String> matchedNonTerminals = stringUtils
          .getMatchedString(finalSentence, NON_TERMINAL_CHECK_REGEX, PARSE_REGEX);
      for (final String nonTerminal : matchedNonTerminals) {
        final String sanitizedNonTerminal = String.valueOf(nonTerminal).toLowerCase(Locale.US);
        if (nonTerminalMap.containsKey(sanitizedNonTerminal)
            && nonTerminalMap.get(sanitizedNonTerminal).size() > 0) {
          final List<String> nonTerminalList = nonTerminalMap.get(sanitizedNonTerminal);
          finalSentence = finalSentence.replaceAll(
              String.format(NON_TERMINAL_FORMAT, nonTerminal),
              nonTerminalList.get(random.nextInt(nonTerminalList.size())));
        } else {
          finalSentence = finalSentence.replaceAll(
              String.format(NON_TERMINAL_FORMAT, nonTerminal), "");
        }
      }
    }
    return finalSentence;
  }

}
