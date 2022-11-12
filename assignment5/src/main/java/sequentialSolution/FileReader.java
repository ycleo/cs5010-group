package sequentialSolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileReader {
  final private static String INFO_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
  final private static String underline = "_";
  final private static int ZERO = 0;
  final private static int ONE = 1;
  final private static int FOUR = 4;
  final private static int FIVE = 5;
  final private static String QUOTATION_MARK = "\"";
  final private static String EMPTY = "";
  private Scanner csvScanner;
  private String[] courseFormat;
  private String[] studentVleFormat;

  public FileReader() {}
  public HashMap<String, Course> readCoursesCsv(String path) throws FileNotFoundException {
    HashMap<String, Course> coursesMap = new HashMap<>();
    this.csvScanner = new Scanner(new File(path));
    this.courseFormat = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
//    for (String str : this.courseFormat) {
//      System.out.println(str);
//    }
    while(this.csvScanner.hasNext()) {
      String[] courseInfo = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
      Course newCourse = new Course(courseInfo);
      coursesMap.put(courseInfo[ZERO] + underline + courseInfo[ONE], newCourse);
    }
//    for (Entry<String, Course> entry : coursesMap.entrySet()) {
//      String key = entry.getKey();
//      System.out.println(key);
//    }
    this.csvScanner.close();
    return coursesMap;
  }

  public void readStudentVleCsv(String path, HashMap<String, Course> coursesMap) throws FileNotFoundException {
    this.csvScanner = new Scanner(new File(path));
    this.studentVleFormat = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
    while(this.csvScanner.hasNext()) {
      String[] studentVle = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));

      // Produce course's date and sum clicks
      Course course = coursesMap.get(studentVle[ZERO] + underline + studentVle[ONE]);
      course.updateSumClicks(studentVle[FOUR], studentVle[FIVE]);
    }
    this.csvScanner.close();
  }

  /**
   * Trims the quotation mark extracting from the csv file
   *
   * @param csvRow A certain row from the csv file
   * @return The row that the quotation marks of every element has been removed
   */
  private String[] trimQuotationMark(String[] csvRow) {
    for (int i = ZERO; i < csvRow.length; i++) {
      csvRow[i] = csvRow[i].replaceAll(QUOTATION_MARK, EMPTY);
    }
    return csvRow;
  }
}
