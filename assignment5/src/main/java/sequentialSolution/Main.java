package sequentialSolution;

import java.io.IOException;
import java.util.HashMap;

public class Main {
  static String coursesCsvPath;
  static String studentVleCsvPath;
  static String summaryOutputPath;
  static HashMap<String, Course> coursesMap; // module_presentation -> Course Object

  public static void main(String[] args) throws IOException {
    coursesCsvPath = args[0].trim();
    studentVleCsvPath = args[1].trim();
    summaryOutputPath = args[2].trim();
    FileReader fileReader = new FileReader();
    SummaryGenerator summaryGenerator = new SummaryGenerator();

    coursesMap = fileReader.readCoursesCsv(coursesCsvPath); // Read courses.csv and establish courses map
    fileReader.readStudentVleCsv(studentVleCsvPath, coursesMap); // Sequentially read studentVle.csv and update the courses map
    summaryGenerator.generateSummary(summaryOutputPath, coursesMap); // output the summary
  }
}
