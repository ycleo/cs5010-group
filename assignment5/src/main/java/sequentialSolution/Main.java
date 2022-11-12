package sequentialSolution;

import static sequentialSolution.Constants.ONE;
import static sequentialSolution.Constants.THREE;
import static sequentialSolution.Constants.TWO;
import static sequentialSolution.Constants.ZERO;

import java.io.IOException;
import java.util.HashMap;

public class Main {
  static String coursesCsvPath;
  static String studentVleCsvPath;
  static String summaryOutputPath;
  static HashMap<String, Course> coursesMap; // module_presentation -> Course Object

  public static void main(String[] args) throws IOException {
    if (args.length != THREE) {
      throw new IllegalArgumentException();
    }
    coursesCsvPath = args[ZERO].trim();
    studentVleCsvPath = args[ONE].trim();
    summaryOutputPath = args[TWO].trim();
    FileReader fileReader = new FileReader();
    SummaryGenerator summaryGenerator = new SummaryGenerator();

    coursesMap = fileReader.readCoursesCsv(coursesCsvPath); // Read courses.csv and establish courses map
    fileReader.readStudentVleCsv(studentVleCsvPath, coursesMap); // Sequentially read studentVle.csv and update the courses map
    summaryGenerator.generateSummary(summaryOutputPath, coursesMap); // output the summary
  }
}
