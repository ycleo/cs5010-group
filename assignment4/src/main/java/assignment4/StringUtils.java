package assignment4;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The class for all string util class
 */
public class StringUtils {
  private static final String QUIT = "q";

  /**
   * match the input with input regex
   * @param input input string
   * @param regex input regex
   * @return boolean
   */
  public boolean matchRegex(final String input, final String regex) {
    final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    final Matcher matcher = pattern.matcher(input);
    return matcher.find();
  }

  /**
   * Get matches string
   * @param input input
   * @param regex regex
   * @param parseRegex parse Regex
   * @return list of matched string
   */
  public List<String> getMatchedString(final String input, final String regex, final String parseRegex) {
    final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    final Matcher matcher = pattern.matcher(input);
    return matcher.results()
        .map(MatchResult::group)
        .map(string -> string.replaceAll(parseRegex, ""))
        .collect(Collectors.toList());
  }

  /**
   * match the quit command
   * @param input input
   * @return boolean
   */
  public boolean matchQuit(final String input) {
    return QUIT.equals(input);
  }

  /**
   * Check if input is number
   * @param input input
   * @return true if input is number
   */
  public boolean matchNumber(final String input) {
    return matchRegex(input, "\\d+");
  }

}
