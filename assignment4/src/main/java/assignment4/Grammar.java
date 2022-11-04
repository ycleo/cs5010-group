package assignment4;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * This is the data type for grammar
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Grammar {
  private String grammarTitle;
  private String grammarDesc;
  private List<String> start;
  private Map<String, List<String>> nonTerminal = new HashMap<>();

  /**
   * Default constructor for Grammar
   */
  public Grammar() {
    super();
  }

  /**
   * Constructor for helping the testing
   * @param grammarTitle grammar title
   * @param grammarDesc grammar desc
   * @param start start
   */
  public Grammar(final String grammarTitle,
      final String grammarDesc,
      final List<String> start){
    this.grammarTitle = grammarTitle;
    this.grammarDesc = grammarDesc;
    this.start = start;
  }

  /**
   * Get grammar desc
   * @return string
   */
  public String getGrammarDesc() {
    return this.grammarDesc;
  }

  /**
   * get grammar title
   * @return string
   */
  public String getGrammarTitle() {
    return this.grammarTitle;
  }

  /**
   * get non terminal map
   * @return non terminal map
   */
  public Map<String, List<String>> getNonTerminal() {
    return this.nonTerminal;
  }

  /**
   * get start
   * @return start
   */
  public List<String> getStart() {
    return this.start;
  }

  /**
   * modify grammar desc
   * @param grammarDesc input grammar desc
   */
  public void modifyGrammarDesc(final String grammarDesc) {
    this.setGrammarDesc(grammarDesc);
  }

  /**
   * modify grammar title
   * @param grammarTitle input grammar title
   */
  public void modifyGrammarTitle(final String grammarTitle) {
    this.setGrammarTitle(grammarTitle);
  }

  /**
   * modify start
   * @param start input start
   */
  public void modifyStart(final List<String> start) {
    this.setStart(start);
  }

  /**
   * validate grammar
   * @return boolean
   */
  public boolean validateGrammar() {
    return this.start != null
        && !this.start.isEmpty()
        && this.grammarTitle != null
        && !this.grammarTitle.isEmpty();
  }

  /**
   * Add more entry in the non-terminal
   * @param key key for the entry
   * @param value the list of string
   */
  @JsonAnySetter
  public void setNonTerminal(final String key, final List<String> value) {
    this.getNonTerminal().put(key.trim().toLowerCase(Locale.US), value);
  }

  private void setGrammarDesc(final String grammarDesc) {
    this.grammarDesc = grammarDesc;
  }

  private void setGrammarTitle(final String grammarTitle) {
    this.grammarTitle = grammarTitle;
  }

  private void setStart(final List<String> start) {
    this.start = start;
  }

  /**
   * overridden to string method
   * @return string
   */
  @Override
  public String toString() {
    return String.format("Grammar[grammarTitle: %s, grammarDesc: %s, start: %s, nonTerminalMap: %s]",
        this.getGrammarTitle(),
        this.getGrammarDesc(),
        this.getStart(),
        this.getNonTerminal());
  }

  /**
   * overridden equals method
   * @param obj the right obj
   * @return boolean
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final Grammar grammar = (Grammar) obj;
    return Objects.equals(this.getGrammarTitle(), grammar.getGrammarTitle())
        && Objects.equals(this.getGrammarDesc(), grammar.getGrammarDesc())
        && Objects.equals(this.getStart(), grammar.getStart())
        && Objects.equals(this.getNonTerminal(), grammar.getNonTerminal());

  }

  /**
   * overridden hashCode method
   * @return int
   */
  @Override
  public int hashCode() {
    return this.grammarTitle.length();
  }
}
