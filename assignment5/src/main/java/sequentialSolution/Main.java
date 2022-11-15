package sequentialSolution;

import static sequentialSolution.Constants.*;

import java.io.IOException;
import java.util.HashMap;

public class Main {

  static String coursesCsvPath;
  static String studentVleCsvPath;
  static String summaryOutputPath;
  static HashMap<String, Course> coursesMap;

  public static void main(String[] args) throws IOException {
    if (args.length != THREE) {
      throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
    }
    coursesCsvPath = args[ZERO].trim();
    studentVleCsvPath = args[ONE].trim();
    summaryOutputPath = args[TWO].trim();
    coursesMap = new HashMap<>();
    DatasetReader datasetReader = new DatasetReader();

    // Read courses.csv and establish courses map
    datasetReader.readCoursesCsv(coursesCsvPath, coursesMap);

    // Sequentially read studentVle.csv and update the courses map (sum clicks)
    datasetReader.readStudentVleCsv(studentVleCsvPath, coursesMap);

    // output the summary
    SummaryGenerator summaryGenerator = new SummaryGenerator(summaryOutputPath, coursesMap);
    summaryGenerator.generateSummary();
  }
}
