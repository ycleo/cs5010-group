package sequentialSolution;

import static sequentialSolution.Constants.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class SummaryGenerator {

  private String summaryOutputPath;
  private HashMap<String, Course> coursesMap;

  public SummaryGenerator(String summaryOutputPath, HashMap<String, Course> coursesMap) {
    this.summaryOutputPath = summaryOutputPath;
    this.coursesMap = coursesMap;
  }

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
      TreeMap<Integer, Integer> dateToSumClicks = course.getDateToSumClicks();
      for (Map.Entry<Integer, Integer> entry : dateToSumClicks.entrySet()) {
        String date = String.valueOf(entry.getKey());
        String sumClicks = String.valueOf(entry.getValue());
        summaryWriter.append(date + COMMA + sumClicks + NEXT_LINE);
      }

      summaryWriter.flush();
      summaryWriter.close();
      courseIterator.remove();
    }
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(this.summaryOutputPath, this.coursesMap);
  }

  @Override
  public String toString() {
    return "Summary Generator: { Summary Output Path: " + this.summaryOutputPath + "; "
        + "Courses Map: " + this.coursesMap.toString() + " }";
  }

}
