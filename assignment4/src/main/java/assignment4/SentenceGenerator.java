package assignment4;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * The class for generate sentence and validate grammar
 */
public class SentenceGenerator {
  private static final String NON_TERMINAL_FORMAT = "\\<%s\\>";
  private static final String NON_TERMINAL_CHECK_REGEX = "\\<[a-zA-Z0-9-_]*\\>";
  private static final String PARSE_REGEX = "\\<|\\>";

  private StringUtils stringUtils;

  /**
   * Constructor for SentenceGenerator
   */
  public SentenceGenerator() {
    stringUtils = new StringUtils();
  }

  /**
   * Validate grammar input
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
   * @param grammar selected grammar
   * @return String with all sentences
   */
  public String generateSentences(final Grammar grammar) {
    final List<String> sentenceList = new ArrayList<>();
    for (final String start: grammar.getStart()) {
      sentenceList.add(generateSentence(start, grammar.getNonTerminal()));
    }
    return String.join("\n", sentenceList);
  }

  private String generateSentence(final String start, final Map<String, List<String>> nonTerminalMap) {
    String finalSentence = String.valueOf(start);
    final Random random = new Random();
    while (stringUtils.matchRegex(finalSentence, NON_TERMINAL_CHECK_REGEX)) {
      final List<String> matchedNonTerminals = stringUtils
          .getMatchedString(finalSentence, NON_TERMINAL_CHECK_REGEX, PARSE_REGEX);
      for (final String nonTerminal: matchedNonTerminals) {
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
