package sequentialSolution;

import static sequentialSolution.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class dataset reader that helps to read and process the CSV files
 */
public class DatasetReader {

  private Scanner csvScanner;
  private String[] courseFormat;
  private String[] studentVleFormat;

  /**
   * Reads and processes the course.csv file to establish the courses map
   *
   * @param path       the path of the course.csv file
   * @param coursesMap courses map that contains course name and corresponding course information
   * @throws FileNotFoundException exception when the file can not be found
   */
  public void readCoursesCsv(String path, Map<String, Course> coursesMap)
      throws FileNotFoundException {
    this.csvScanner = new Scanner(new File(path));
    this.courseFormat = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));

    while (this.csvScanner.hasNext()) {
      String[] courseInfo = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
      Course newCourse = new Course(courseInfo, new HashMap<>());
      coursesMap.put(courseInfo[ZERO] + UNDERLINE + courseInfo[ONE],
          newCourse); // module_presentation
    }

    this.csvScanner.close();
  }

  /**
   * Reads and processes the studentVle.csv file to update the courses map
   *
   * @param path       the path of the studentVle.csv file
   * @param coursesMap courses map that contains course name and corresponding course information
   * @throws FileNotFoundException exception when the file can not be found
   */
  public void readStudentVleCsv(String path, Map<String, Course> coursesMap)
      throws FileNotFoundException {
    this.csvScanner = new Scanner(new File(path));
    this.studentVleFormat = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));
    while (this.csvScanner.hasNext()) {
      String[] studentVle = trimQuotationMark(this.csvScanner.nextLine().split(INFO_DELIMITER));

      // Produce course's date and sum clicks
      String course = studentVle[ZERO] + UNDERLINE + studentVle[ONE]; // module_presentation
      String date = studentVle[FOUR];
      String sumClicks = studentVle[FIVE];
      coursesMap.get(course).updateSumClicks(date, sumClicks);
    }
    this.csvScanner.close();
  }

  /**
   * Trims the quotation mark extracting from the csv file
   *
   * @param csvRow A certain row from the csv file
   * @return The row that the quotation marks of every element has been removed
   */
  public static String[] trimQuotationMark(String[] csvRow) {
    for (int i = ZERO; i < csvRow.length; i++) {
      csvRow[i] = csvRow[i].replaceAll(QUOTE, EMPTY);
    }
    return csvRow;
  }

  /**
   * Gets the course format
   *
   * @return the string array that contains the course format
   */
  public String[] getCourseFormat() {
    return this.courseFormat;
  }

  /**
   * Gets the student data format
   *
   * @return the string array that contains the student data
   */
  public String[] getStudentVleFormat() {
    return this.studentVleFormat;
  }

  /**
   * Tests the Dataset Reader object equals to the passed Object o
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
    DatasetReader datasetReader = (DatasetReader) o;
    return Objects.equals(this.csvScanner, datasetReader.csvScanner) ||
        Objects.equals(this.courseFormat, datasetReader.courseFormat) ||
        Objects.equals(this.studentVleFormat, datasetReader.studentVleFormat);
  }

  /**
   * Returns the hash code of the Dataset Reader object
   *
   * @return a hash code integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.csvScanner, this.courseFormat, this.studentVleFormat);
  }

  /**
   * Returns the string represents the Dataset Reader information
   *
   * @return a string about the Dataset Reader object
   */
  @Override
  public String toString() {
    return "FileReader: { CSV Scanner: " + this.csvScanner.toString() + "; "
        + "Course Format: " + this.courseFormat.toString() + "; "
        + "StudentVle Format: " + this.studentVleFormat.toString() + " }";
  }

}
