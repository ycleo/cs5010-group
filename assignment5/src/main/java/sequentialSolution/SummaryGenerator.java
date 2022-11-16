package sequentialSolution;

import static sequentialSolution.Constants.COMMA;
import static sequentialSolution.Constants.CSV_EXTENSION;
import static sequentialSolution.Constants.NEXT_LINE;
import static sequentialSolution.Constants.OUTPUT_ROW_FORMAT;
import static sequentialSolution.Constants.QUOTE;
import static sequentialSolution.Constants.SLASH;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Class summary generator will help transform the courses map information into summary files and
 * output to the designated directory
 */
public class SummaryGenerator {

  private String summaryOutputPath;
  private Map<String, Course> coursesMap;

  /**
   * Constructs the summary generator object
   *
   * @param summaryOutputPath the path of the output summary files
   * @param coursesMap        courses map that contains course name and corresponding course
   *                          information
   */
  public SummaryGenerator(String summaryOutputPath, Map<String, Course> coursesMap) {
    this.summaryOutputPath = summaryOutputPath;
    this.coursesMap = coursesMap;
  }

  /**
   * Generates the output summary files to the designated directory
   *
   * @throws IOException exception when the I/O operation is failed or interrupted
   */
  public void generateSummary() throws IOException {
    // iterate through courses map
    Iterator courseIterator = this.coursesMap.entrySet().iterator();
    while (courseIterator.hasNext()) {
      Map.Entry coursePair = (Map.Entry) courseIterator.next();

      String courseModuleAndPresentation = (String) coursePair.getKey();
      Course course = (Course) coursePair.getValue();

      // set up the file writing path and write the format (first row)
      FileWriter summaryWriter = new FileWriter(
          new File(summaryOutputPath + SLASH + courseModuleAndPresentation + CSV_EXTENSION));
      summaryWriter.append(OUTPUT_ROW_FORMAT + NEXT_LINE);

      // iterate the information tree map of the current course
      TreeMap<Integer, Integer> dateToSumClicks = new TreeMap<>(course.getDateToSumClicks());
      for (Map.Entry<Integer, Integer> entry : dateToSumClicks.entrySet()) {
        String date = String.valueOf(entry.getKey());
        String sumClicks = String.valueOf(entry.getValue());
        summaryWriter.append(QUOTE + date + QUOTE + COMMA + QUOTE + sumClicks + QUOTE + NEXT_LINE);
      }

      summaryWriter.flush();
      summaryWriter.close();
    }
  }

  /**
   * Tests the summary generator object equals to the passed Object o
   *
   * @param o the passed Object o
   * @return boolean that indicates the equality
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    SummaryGenerator summaryGenerator = (SummaryGenerator) o;
    return Objects.equals(this.summaryOutputPath, summaryGenerator.summaryOutputPath) ||
        Objects.equals(this.coursesMap, summaryGenerator.coursesMap);
  }

  /**
   * Returns the hash code of the summary generator object
   *
   * @return a hash code integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.summaryOutputPath, this.coursesMap);
  }

  /**
   * Returns the string represents the summary generator information
   *
   * @return a string about the summary generator object
   */
  @Override
  public String toString() {
    return "Summary Generator: { Summary Output Path: " + this.summaryOutputPath + "; "
        + "Courses Map: " + this.coursesMap.toString() + " }";
  }

}
