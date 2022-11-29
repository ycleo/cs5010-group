package sequentialSolution;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SummaryGeneratorTest {

  private SummaryGenerator summaryGenerator1;
  private SummaryGenerator summaryGenerator2;
  private String outputPath1 = "testFiles/output/";
  private String outputPath2 = "testFiles/outputFiles/";
  private HashMap<String, Course> coursesMap = new HashMap<>();
  @BeforeEach
  void setUp() {
    summaryGenerator1 = new SummaryGenerator(outputPath1, coursesMap);
    summaryGenerator2 = new SummaryGenerator(outputPath2, coursesMap);
  }

  @Test
  void testEquals() {
    assertTrue(summaryGenerator1.equals(summaryGenerator1));
    assertFalse(summaryGenerator1.equals(null));
    assertFalse(summaryGenerator1.equals(outputPath1));
    assertTrue(summaryGenerator1.equals(summaryGenerator2));
    summaryGenerator2 = new SummaryGenerator(outputPath1, coursesMap);
    assertTrue(summaryGenerator1.equals(summaryGenerator2));
  }

  @Test
  void testHashCode() {
    assertEquals(summaryGenerator1.hashCode(), summaryGenerator1.hashCode());
  }

  @Test
  void testToString() {
    assertEquals(summaryGenerator1.toString(), summaryGenerator1.toString());
  }
}